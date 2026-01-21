# Python 3.14+ Compatibility Fixes for ha-image

This document describes the fixes applied to build ha-image with Python 3.14+.

## Fixed Packages

### 1. python3-geojson
**Issue**: Python version check limited to 3.7-3.13  
**Fix**: Removed version restriction in setup.py  
**Files**:
- `recipes-devtools/python/python3-geojson_%.bbappend`
- `recipes-devtools/python/python3-geojson/0001-Remove-Python-version-restriction-for-compatibility-.patch`

### 2. python3-html5lib
**Issue**: `ast.Str` removed in Python 3.14  
**Fix**: Changed `ast.Str` → `ast.Constant` and `a.value.s` → `a.value.value`  
**Files**:
- `recipes-devtools/python/python3-html5lib_%.bbappend`
- `recipes-devtools/python/python3-html5lib/0001-Fix-ast.Str-compatibility-for-Python-3.14.patch`

### 3. python3-aiohttp-sse
**Issue**: `node.value.s` attribute removed in Python 3.14  
**Fix**: Changed `node.value.s` → `node.value.value`  
**Files**:
- `recipes-devtools/python/python3-aiohttp-sse_%.bbappend`
- `recipes-devtools/python/python3-aiohttp-sse/0001-Fix-ast-compatibility-for-Python-3.14.patch`

### 4. python3-pcodec
**Issue**: PyO3 0.24.2 doesn't support Python 3.14  
**Fix**: Set `PYO3_USE_ABI3_FORWARD_COMPATIBILITY=1`  
**Files**:
- `recipes-python/pcodec/python3-pcodec_%.bbappend`

### 5. python3-orjson
**Issue**: PyO3 doesn't support Python 3.14  
**Fix**: Set `PYO3_USE_ABI3_FORWARD_COMPATIBILITY=1`  
**Files**:
- `recipes-devtools/python/python3-orjson_%.bbappend`

### 6. python3-isal
**Issue**: Cross-compilation builds x86_64 assembly for ARM target  
**Fix**: Detect cross-compilation from OECORE_TARGET_ARCH environment variable  
**Files**:
- `recipes-devtools/python/python3-isal_%.bbappend`
- `recipes-devtools/python/python3-isal/0001-Fix-cross-compilation-for-aarch64.patch`

### 7. python3-pizzapi
**Issue**: File conflicts with tests directory  
**Fix**: Remove tests directory during install  
**Files**:
- `recipes-devtools/python/python3-pizzapi_%.bbappend`

## Image Configuration

### ha-image.bb
Added `OPKG_ARGS:append = " --force-overwrite"` to handle Python namespace package conflicts.

### layer.conf
- Fixed `BBFILES` pattern to include nested image recipes
- Added `SSTATE_ALLOW_OVERLAP_FILES` for Python site-packages
- Set `LAYERSERIES_COMPAT_application = "whinlatter"`

## Copying to Another System

To apply these fixes to another system:

1. Copy the entire `meta-application/recipes-devtools/python/` directory
2. Copy the entire `meta-application/recipes-python/` directory  
3. Copy `meta-application/conf/layer.conf`
4. Update `meta-application/recipes-core/images/doorphone-image/ha-image.bb` to add:
   ```
   OPKG_ARGS:append = " --force-overwrite"
   ```

All patches were created using `devtool` and include proper `Upstream-Status` headers.
