cmake_minimum_required(VERSION 3.22.1)

project("sari-downloader")

add_library(
    sari-downloader
    SHARED
    downloader.cpp
)

target_compile_features(
    sari-downloader
    PRIVATE
    cxx_std_17
)

target_compile_options(
    sari-downloader
    PRIVATE
    -Wall
    -Wextra
    -Wpedantic
)
