# Mako Patcher

Mako patcher is a program that patches Mako (12+) apk.

## Features

- No ads at all
- Able to patch apks, apkm and xapk files

## Requirements
1. Python3
2. JDK 17+

## Installation

```bash
git clone https://github.com/Schwartzblat/MakoPatcher.git

cd MakoPatcher

git submodule update --init --recursive
```
Requirements installation:
```bash
pip install -r requirements.txt

# Until stitch is available on PyPI
pip install --index-url https://test.pypi.org/simple/ stitch-test
```
## Usage

```bash
python main.py -p ./mako.xapk

adb install-multiple output_bundle_apks/*.apk
```

## Contributing

If you want to add patches you can open an issue and I will implement it, or you can add patches yourself and open pull request.

## Disclaimer

For educational purpose only or something like that. I am not responsible for any misuse of this software.