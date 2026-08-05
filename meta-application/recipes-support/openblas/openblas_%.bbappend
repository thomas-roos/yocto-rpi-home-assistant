# Newer GCC (pulled in by the oe-core "wrynose" bump) made -Wincompatible-pointer-types
# an error by default. OpenBLAS 0.3.31's bfloat16 gemm interface (sbgemm.o,
# cblas_sbgemm.o) passes gemm_thread_mn a function pointer with a mismatched
# signature - an upstream bug, not something to fix here. Downgrade it back to
# a warning so the build isn't blocked on it.
CFLAGS:append = " -Wno-error=incompatible-pointer-types"
