# MusicConverter

MusicConverter changes the tempo and pitch of a song on the device and saves the result as a WAV file. It does not include any bundled music.

## Features

- Browse device folders and choose a WAV or MP3 file
- Show the file name and embedded album art when the file has one
- Tempo from 5% to 500%, and pitch about five octaves up or down
- Convert with SoundTouch. An MP3 is turned into WAV first
- Choose the folder where the converted file is saved
- How-to guide and open-source licenses

The converted file is named `<original>_converted.wav`.

## Build

Open this folder in Android Studio. JDK 11 is required. The SoundTouch native library is built with the NDK during compilation.

| Item | Value |
| --- | --- |
| Package | `com.happyhouse.music.converter` |
| Language | Java |
| compileSdk | 28 |
| minSdk | 22 |
| targetSdk | 28 |

`local.properties` is not meant to be committed. Android Studio writes the SDK path when it creates this file. This project also expects `ndk.dir` in that file, for example:

```
sdk.dir=C\:\\Users\\you\\AppData\\Local\\Android\\Sdk
ndk.dir=C\:\\Users\\you\\AppData\\Local\\Android\\Sdk\\ndk\\21.1.6352462
```

A debug build copies the APK to `H:/overman623/Release/MusicConverter` when that path is available.

## Screens

- `MainActivity` — artwork, tempo, pitch, and convert
- `MusicDisplayActivity` — folder browser for WAV and MP3 files
- `DialogSetting` — output folder
- `DialogHelp` — how to use the app, and SoundTouch and Glide licenses
