#include <vector>
#include "image_processor.h"
#include <cmath>
#include <cstring>
#include <algorithm>

// ─── Clamp helper ────────────────────────────────────────────────────────────
static inline uint8_t clamp8(float v) {
    if (v <= 0.f) return 0;
    if (v >= 255.f) return 255;
    return static_cast<uint8_t>(v + 0.5f);
}

// ─── LUT Builders ────────────────────────────────────────────────────────────
extern "C" void alpha_build_exposure_lut(float exposure, uint8_t* lut) {
    // Map [-1, +1] to EV stops: multiply by 2^ev
    float ev = exposure * 2.0f; // ±2 stops range
    float gain = std::pow(2.0f, ev);
    for (int i = 0; i < 256; ++i) {
        lut[i] = clamp8(i * gain);
    }
}

extern "C" void alpha_build_contrast_lut(float contrast, uint8_t* lut) {
    // S-curve: factor in [0.5, 2.0], pivot at 128
    float factor = (contrast >= 0.f)
        ? 1.0f + contrast          // [1.0 .. 2.0]
        : 1.0f / (1.0f - contrast); // [0.5 .. 1.0]
    for (int i = 0; i < 256; ++i) {
        float v = (i - 128.f) * factor + 128.f;
        lut[i] = clamp8(v);
    }
}

// ─── RGB <-> YCbCr helpers (BT.601, full-range) ──────────────────────────────
static inline void rgb_to_ycbcr(uint8_t r, uint8_t g, uint8_t b,
                                  float& Y, float& Cb, float& Cr) {
    Y  =  0.299f*r + 0.587f*g + 0.114f*b;
    Cb = -0.16874f*r - 0.33126f*g + 0.5f*b + 128.f;
    Cr =  0.5f*r - 0.41869f*g - 0.08131f*b + 128.f;
}

static inline void ycbcr_to_rgb(float Y, float Cb, float Cr,
                                  uint8_t& r, uint8_t& g, uint8_t& b) {
    float cb = Cb - 128.f, cr = Cr - 128.f;
    r = clamp8(Y + 1.40200f * cr);
    g = clamp8(Y - 0.34414f * cb - 0.71414f * cr);
    b = clamp8(Y + 1.77200f * cb);
}

// ─── Shadows / Highlights tone adjustment ────────────────────────────────────
static inline float tone_adjust(float luma, float shadows, float highlights) {
    // Shadows affect [0..128], highlights affect [128..255]
    float t = luma / 255.f;
    float shadowMask    = std::max(0.f, 1.f - t * 2.f);       // 1 at 0, 0 at 128+
    float highlightMask = std::max(0.f, t * 2.f - 1.f);       // 0 at 127-, 1 at 255
    float lift = shadows    * shadowMask    * 64.f;
    float drop = highlights * highlightMask * 64.f;
    return std::clamp(luma + lift + drop, 0.f, 255.f);
}

// ─── Warmth (R/B channel bias) ────────────────────────────────────────────────
static inline void apply_warmth(uint8_t& r, uint8_t& g, uint8_t& b, float warmth) {
    float bias = warmth * 20.f;
    r = clamp8(r + bias);
    b = clamp8(b - bias);
}

// ─── Main pipeline ────────────────────────────────────────────────────────────
extern "C" void alpha_process_image(
    const uint8_t* __restrict__ src,
    uint8_t*       __restrict__ dst,
    int width, int height,
    const EditParams* p)
{
    const int n_pixels = width * height;

    // Pre-build LUTs on stack — 512 bytes total, hot-cache friendly
    uint8_t exposure_lut[256];
    uint8_t contrast_lut[256];
    alpha_build_exposure_lut(p->exposure, exposure_lut);
    alpha_build_contrast_lut(p->contrast, contrast_lut);

    const float sat_scale = 1.0f + p->saturation; // [0.0 .. 2.0]

    for (int i = 0; i < n_pixels; ++i) {
        // Android ARGB_8888 layout in memory: B G R A (little-endian)
        const uint8_t* s = src + i * 4;
        uint8_t*       d = dst + i * 4;

        uint8_t b_in = s[0], g_in = s[1], r_in = s[2], a = s[3];

        // 1. Exposure LUT
        uint8_t r = exposure_lut[r_in];
        uint8_t g = exposure_lut[g_in];
        uint8_t bv= exposure_lut[b_in];

        // 2. Convert to YCbCr for luma-chroma ops
        float Y, Cb, Cr;
        rgb_to_ycbcr(r, g, bv, Y, Cb, Cr);

        // 3. Shadows / Highlights on luma
        Y = tone_adjust(Y, p->shadows, p->highlights);

        // 4. Contrast LUT on luma
        Y = contrast_lut[static_cast<int>(std::clamp(Y, 0.f, 255.f))];

        // 5. Saturation — scale chroma channels around neutral (128)
        Cb = (Cb - 128.f) * sat_scale + 128.f;
        Cr = (Cr - 128.f) * sat_scale + 128.f;

        // 6. Convert back to RGB
        ycbcr_to_rgb(Y, Cb, Cr, r, g, bv);

        // 7. Warmth
        apply_warmth(r, g, bv, p->warmth);

        d[0] = bv; d[1] = g; d[2] = r; d[3] = a;
    }

    // 8. Sharpness pass (operates on luma of dst in-place via scratch)
    if (p->sharpness > 0.001f) {
        // Extract luma → scratch buffer (stack alloc only safe for small images;
        // use heap for production)
        std::vector<uint8_t> luma_src(n_pixels);
        std::vector<uint8_t> luma_dst(n_pixels);
        for (int i = 0; i < n_pixels; ++i) {
            uint8_t bv2 = dst[i*4], g2 = dst[i*4+1], r2 = dst[i*4+2];
            float Y2, Cb2, Cr2;
            rgb_to_ycbcr(r2, g2, bv2, Y2, Cb2, Cr2);
            luma_src[i] = clamp8(Y2);
        }
        alpha_sharpen_channel(luma_src.data(), luma_dst.data(), width, height, p->sharpness);
        // Blend sharpened luma back
        for (int i = 0; i < n_pixels; ++i) {
            uint8_t bv2 = dst[i*4], g2 = dst[i*4+1], r2 = dst[i*4+2];
            float Y2, Cb2, Cr2;
            rgb_to_ycbcr(r2, g2, bv2, Y2, Cb2, Cr2);
            ycbcr_to_rgb(luma_dst[i], Cb2, Cr2, dst[i*4+2], dst[i*4+1], dst[i*4+0]);
        }
    }
}

// ─── Sharpening (unsharp mask, 3×3 Laplacian) ────────────────────────────────
extern "C" void alpha_sharpen_channel(
    const uint8_t* __restrict__ src,
    uint8_t*       __restrict__ dst,
    int width, int height, float strength)
{
    // 3×3 unsharp mask kernel: center=9, neighbours=-1, corners=0
    const float k_center = 1.f + 8.f * strength;
    const float k_edge   = -strength;

    for (int y = 1; y < height - 1; ++y) {
        for (int x = 1; x < width - 1; ++x) {
            int idx = y * width + x;
            float v = k_center * src[idx]
                    + k_edge   * src[idx - 1]
                    + k_edge   * src[idx + 1]
                    + k_edge   * src[idx - width]
                    + k_edge   * src[idx + width]
                    + k_edge   * src[idx - width - 1]
                    + k_edge   * src[idx - width + 1]
                    + k_edge   * src[idx + width - 1]
                    + k_edge   * src[idx + width + 1];
            dst[idx] = clamp8(v);
        }
    }
    // Copy border pixels unchanged
    for (int x = 0; x < width; ++x) {
        dst[x]                         = src[x];
        dst[(height-1)*width + x]      = src[(height-1)*width + x];
    }
    for (int y = 0; y < height; ++y) {
        dst[y * width]                 = src[y * width];
        dst[y * width + width - 1]     = src[y * width + width - 1];
    }
}
