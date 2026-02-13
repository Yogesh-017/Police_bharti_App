# 🚔 Police Bharti PYQ - Previous Year Questions Practice App

A comprehensive Android application for practicing **Maharashtra Police Bharti** previous year question papers. Built with modern Android technologies for an engaging and effective exam preparation experience.

---

## 📱 Features

- 📝 **PYQ Practice** — Solve previous year question papers with a realistic exam interface
- ⏱️ **90-Minute Timer** — Persistent countdown timer that remembers your progress even if you close the app
- 🔖 **Bookmarks** — Save important questions for quick revision later
- 📦 **Content Packs** — Download question packs for offline access (with expiry management)
- ⏸️ **Pause & Resume** — Pause your test and come back to it anytime
- 📊 **Results Screen** — Detailed results showing marks scored, correct/incorrect answers
- 🤖 **AI Help** — Get AI-powered explanations for difficult questions
- 👍 **Vote System** — Vote on questions to help improve content quality
- 🎨 **Premium UI** — Beautiful Material 3 design with Jetpack Compose

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.22 | Primary language |
| **Jetpack Compose** | BOM 2024.01.00 | Modern UI toolkit |
| **Material 3** | Latest | Design system |
| **Room Database** | 2.6.1 | Local SQLite database |
| **Navigation Compose** | 2.7.6 | Screen navigation |
| **DataStore** | 1.0.0 | Timer state persistence |
| **WorkManager** | 2.9.0 | Background pack expiry scheduling |
| **Retrofit** | 2.9.0 | Network API calls |
| **Coroutines** | 1.7.3 | Async operations |
| **KSP** | 1.9.22-1.0.17 | Annotation processing |
| **Gradle** | 9.0.0 | Build system |
| **AGP** | 8.2.2 | Android Gradle Plugin |

---

## 📋 Prerequisites

Before you begin, make sure you have the following installed:

- ✅ **Android Studio** — Hedgehog (2023.1.1) or newer recommended
- ✅ **JDK 17** — Required (the project uses Java 17 compatibility)
- ✅ **Android SDK 34** — compileSdk and targetSdk is API 34
- ✅ **Min SDK 21** — App supports Android 5.0 (Lollipop) and above
- ✅ **At least 8 GB RAM** recommended for smooth Gradle builds
- ✅ **~5 GB free disk space** for SDK, Gradle cache, and build files

---

## 🚀 How to Run This Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/Police_bharti_App.git
```

### Step 2: Open in Android Studio

1. Open **Android Studio**
2. Click **File → Open**
3. Navigate to the cloned `Police_bharti_App` folder and select it
4. Click **OK** and wait for Gradle sync to complete (this may take a few minutes on first run)

### Step 3: Wait for Gradle Sync

- Android Studio will automatically start downloading dependencies
- You'll see a progress bar at the bottom — **wait for it to finish**
- If you get any SDK errors, go to **File → Settings → SDK Manager** and install **API 34**

### Step 4: Setup an Emulator or Device

**Option A: Physical Device (Recommended for low-end laptops)**
1. Enable **Developer Options** on your Android phone
2. Enable **USB Debugging** in Developer Options
3. Connect your phone via USB cable
4. Your device should appear in the device dropdown in Android Studio

**Option B: Emulator**
1. Go to **Tools → Device Manager → Create Device**
2. Select a device (e.g., Pixel 6)
3. Download a system image for **API 34**
4. Create and launch the emulator

### Step 5: Run the App

1. Select your device/emulator from the device dropdown (top toolbar)
2. Click the **green Run ▶️ button** (or press `Shift + F10`)
3. Wait for the build and installation to complete
4. The app should launch on your device! 🎉

---

## 📁 Project Structure

```
Police_bharti_App/
├── app/
│   ├── src/main/
│   │   ├── java/com/policebharti/pyq/
│   │   │   ├── MainActivity.kt              # Entry point
│   │   │   ├── PoliceBhartiApp.kt            # Application class
│   │   │   ├── data/
│   │   │   │   ├── db/                       # Room Database (Entities, DAOs)
│   │   │   │   ├── remote/                   # API Service (Retrofit)
│   │   │   │   └── repository/               # Data repositories
│   │   │   ├── ui/
│   │   │   │   ├── Navigation.kt             # App navigation graph
│   │   │   │   ├── auth/                     # Authentication screen
│   │   │   │   ├── bookmarks/                # Bookmarks screen
│   │   │   │   ├── category/                 # Category selection
│   │   │   │   ├── components/               # Reusable UI components
│   │   │   │   ├── paused/                   # Paused tests screen
│   │   │   │   ├── results/                  # Results screen
│   │   │   │   ├── selection/                # Question selection
│   │   │   │   ├── splash/                   # Splash screen
│   │   │   │   ├── test/                     # Test screen & ViewModel
│   │   │   │   └── theme/                    # App theme & colors
│   │   │   ├── util/                         # Utility classes
│   │   │   └── worker/                       # WorkManager workers
│   │   ├── res/                              # Resources (layouts, strings, etc.)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts                      # App-level dependencies
├── build.gradle.kts                          # Project-level config
├── settings.gradle.kts                       # Project settings
├── gradle/wrapper/                           # Gradle wrapper
└── templates/                                # Question templates (CSV, JSON schema)
```

---

## ⚠️ Troubleshooting

| Problem | Solution |
|---------|----------|
| Gradle sync fails | Make sure you have **JDK 17** set in **File → Settings → Build → Gradle → Gradle JDK** |
| SDK not found | Install **API 34** from **File → Settings → SDK Manager** |
| Build is very slow | Close other apps, increase Gradle memory in `gradle.properties`: `org.gradle.jvmargs=-Xmx4096m` |
| Emulator is slow | Use a **physical device** instead, or enable **hardware acceleration (HAXM)** |
| `local.properties` error | Delete `local.properties` file — Android Studio will regenerate it with your local SDK path |

---

## 🤝 Contributing

1. Fork this repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

## 👨‍💻 Author

**Yogesh Chavan**

---

## 📄 License

This project is for educational purposes. All rights reserved.

---

> ⭐ If this project helps you in your Police Bharti preparation, give it a star!
