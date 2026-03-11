cmake_minimum_required(VERSION 3.22.1)
project(alpha_vision_native CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Optimisation flags
add_compile_options(
    -O2
    -ffast-math
    -fno-exceptions   # smaller binary; we use C-style error returns
    -Wall
)

add_library(
    alpha_vision_native
    SHARED
    jni_bridge.cpp
    image_processor.cpp
)

# Android system libraries
find_library(log-lib  log)
find_library(jnigraphics-lib jnigraphics)

target_link_libraries(
    alpha_vision_native
    PRIVATE
        ${log-lib}
        ${jnigraphics-lib}
)
