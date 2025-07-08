# 📅 DateFinder – Android OCR Date Detection App

An AI-powered Android app that extracts the **first relevant date** from an image using **ML Kit OCR** and reads it aloud using **Text-to-Speech (TTS)**.

---

## 🚀 Features

- Pick an image from your gallery
- Detect printed dates in formats like:
  - `dd/MM/yyyy`, `dd/MM/yy`, `yyyy/MM/dd`, `dd-MM-yyyy`,`dd-MM-yy`,`yyyy-MM-dd`, `dd.MM.yyyy`,`dd.MM.yy`,`yyyy.MM.dd` etc.
- Automatically speak the detected date
- User-friendly interface and accessible design

---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **ML OCR:** Google ML Kit – Text Recognition  
- **Speech:** Android TextToSpeech API  
- **UI:** XML layout with Material 3 components  

---

## 📦 Project Structure

```
DateFinder/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/android_development/datefinder/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── DateExtract.kt
│   │   │   │   └── SplashScreen.kt
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml
│   │   │       │   └── activity_splash.xml
│   │   │       ├── drawable/
│   │   │       └── values/
│   └── build.gradle
└── README.md
```


---

## 📱 How to Run

1. Clone or download the project:
   ```bash
   git clone https://github.com/Vanshika521/DateFinder.git
   
2. Open in Android Studio (Arctic Fox or newer)

3. Allow Gradle to sync

4. Run the app on an emulator or real device
   

## 🧪 How to Test
  1. Click “Choose Image”

  2. Select an image containing a printed date like 15/08/2025

  3. App will:

     • Detect and display the first valid date

     • Speak it aloud using Text-to-Speech

 ## 📄 Supported Date Formats

| Format Type   | Example      |
|---------------|--------------|
| dd/MM/yyyy    | 15/08/2025   |
| dd/MM/yy      | 15/08/25     |
| yyyy/MM/dd    | 2025/08/15   |
| dd-MM-yyyy    | 15-08-2025   |
| dd-MM-yy      | 15-08-25     |
| yyyy-MM-dd    | 2025-08-15   |
| dd.MM.yyyy    | 15.08.2025   |
| dd.MM.yy      | 15.08.25     |
| yyyy.MM.dd    | 2025.08.15   |

## 📦 APK Download

You can download and install the latest APK for testing:

👉 [Download APK](https://gofile.io/d/fnZMgv)

## 📚 Future Improvements
  •  Support for month-name formats like 01-Jan-2025

  •  Camera-based image input

  •  Multi-date extraction and highlighting


## 📃 License

This project was developed as part of an assignment and is intended for educational use only.  
Not licensed for commercial distribution or reuse.


    




