#pragma once
#include <cstdint>
#include <cstddef>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * AlphaVision Native Image Processing Engine
 * All pixel buffers are ARGB_8888 (4 bytes per pixel, Android convention).
 * Width & height describe the buffer dimensions.
 * Stride = width * 4 (tightly packed, no row padding assumed).
 *
 * Memory contract:
 *   - Caller allocates dst buffer (width * height * 4 bytes).
 *   - Engine writes to dst; src is never modified.
 *   - No heap allocations inside hot paths; uses stack-local LUTs.
 */

typedef struct {
    float exposure;    /* -1.0 .. +1.0 */
    float contrast;    /* -1.0 .. +1.0 */
    float saturation;  /* -1.0 .. +1.0 */
    float warmth;      /* -1.0 .. +1.0  (color temperature shift) */
    float highlights;  /* -1.0 .. +1.0 */
    float shadows;     /* -1.0 .. +1.0 */
    float sharpness;   /*  0.0 .. +1.0 */
} EditParams;

/**
 * Apply all edit parameters to src → dst.
 * Pipeline: Exposure → Shadows/Highlights → Contrast → Saturation → Warmth → Sharpen
 */
void alpha_process_image(
    const uint8_t* __restrict__ src,
    uint8_t*       __restrict__ dst,
    int width,
    int height,
    const EditParams* params
);

/**
 * Build a 256-entry gamma-corrected exposure LUT.
 * out_lut must point to a caller-allocated uint8_t[256].
 */
void alpha_build_exposure_lut(float exposure, uint8_t* out_lut);

/**
 * Build a 256-entry contrast S-curve LUT.
 */
void alpha_build_contrast_lut(float contrast, uint8_t* out_lut);

/**
 * 3x3 unsharp-mask sharpening pass (single channel, applied to Y in YCbCr).
 * src and dst may NOT alias.
 */
void alpha_sharpen_channel(
    const uint8_t* __restrict__ src,
    uint8_t*       __restrict__ dst,
    int width,
    int height,
    float strength
);

#ifdef __cplusplus
} // extern "C"
#endif
