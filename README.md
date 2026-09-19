# 🎮 Tic-Tac-Toe Android App with Firebase Authentication

A clean, interactive Android Tic-Tac-Toe game featuring real-time user authentication via Google Firebase, custom touch-rendered canvas graphics, custom player naming, and robust turn-and-win referee logic.

---

## ✨ Features

- **🔐 Firebase Authentication**:
  - Secure user registration and sign-in backed by Firebase Auth.
  - Client-side validation: email formatting check and minimum 6-character password enforcement.
  - Auto-login: returning players are automatically recognized and navigated straight to the main menu.
  - Input sanitization and network debouncing to avoid duplicate authentication requests.
- **🎨 Custom Canvas Board Rendering**:
  - Custom `TicTacToeBoard` view dynamically calculated using Android's `Canvas` and `Paint`.
  - Rounded stroke caps (`Paint.Cap.ROUND`) for crisp, modern grid lines, X markers, and O markers.
  - Dynamic strike-through winning line automatically drawn across winning rows, columns, and diagonals.
- **👥 Player Customization**:
  - Customizable names for Player 1 and Player 2 before starting each game.
  - Graceful fallbacks ("Player 1" & "Player 2") if names are left blank.
- **⚖️ Game Engine & Rules**:
  - Complete 3x3 game referee supporting horizontal, vertical, and both diagonal winning paths.
  - Tie / Draw match detection.
  - In-game reset ("Play Again") and clean return to the main menu without memory leaks or activity stack accumulation.
- **📱 Responsive UI**:
  - Packed constraint chains for the login and registration screens ensuring consistent layout across various screen sizes and virtual keyboards.

---

## 🛠️ Tech Stack & Dependencies

- **Language**: Java 8
- **Platform**: Android (Min SDK: 23 / Android 6.0 Marshmallow, Target SDK: 30)
- **Backend / Authentication**: Firebase Authentication (`com.google.firebase:firebase-auth:21.0.1`)
- **UI Architecture**: Android XML Layouts (`ConstraintLayout 2.0.4`, Material Components `1.3.0`)
- **Build System**: Gradle Wrapper (`gradle-6.5`) with Android Gradle Plugin

---

## 📁 Project Architecture

```
TicTacToe/
├── app/
│   ├── google-services.json          # Firebase service configuration
│   ├── build.gradle                  # App-level dependencies & build rules
│   └── src/main/
│       ├── AndroidManifest.xml       # App declarations, permissions & launcher
│       ├── java/com/example/myapplication/
│       │   ├── signin.java           # Login screen with Firebase Auth & auto-login
│       │   ├── signup.java           # Account creation with validation
│       │   ├── MainActivity.java     # Home / welcome lobby screen
│       │   ├── PlayerSetup.java      # Player name configuration
│       │   ├── GameDisplay.java      # Game host activity
│       │   ├── GameLogic.java        # Turn management & win/tie referee logic
│       │   └── TicTacToeBoard.java   # Custom View rendering grid, markers & win lines
│       └── res/
│           ├── layout/               # XML layouts for screens and game board
│           ├── values/               # Colors, strings, themes & custom view attributes
│           └── drawable/             # Button styles and background assets
├── build.gradle                      # Project-level build configuration
├── gradle/wrapper/                   # Gradle wrapper binaries & properties
└── settings.gradle                   # Module include settings
```

---

## 🚀 Getting Started

### Prerequisites
1. **Android Studio**: Android Studio Arctic Fox / Flamingo / Electric Eel or newer.
2. **Java Development Kit (JDK)**: JDK 8 or JDK 11 (configured in Android Studio under *Gradle JDK*).
3. **Android Device or Emulator**: Running Android API level 23 or higher.

### 1. Clone the Repository
```bash
git clone https://github.com/deadboltt/TIC-TAC-TOE-APP-WITH-FIREBASE-LOGIN.git
cd TIC-TAC-TOE-APP-WITH-FIREBASE-LOGIN
```

### 2. Firebase Configuration
The project includes a sample `google-services.json` in the `app/` folder. To connect to your own Firebase project:
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project and add an Android app with package name:
   ```
   com.example.myapplication
   ```
3. Enable **Email/Password** authentication under **Build > Authentication > Sign-in method**.
4. Download your `google-services.json` and replace the existing file at:
   ```
   app/google-services.json
   ```

### 3. Build & Run
1. Open the project folder in **Android Studio**.
2. Let Gradle sync dependencies automatically.
3. Select an Android Virtual Device (AVD) or connect a physical phone with USB Debugging enabled.
4. Click the green **Run** button (`Shift + F10`) or run via terminal:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🎯 How to Play

1. **Sign In / Sign Up**: Launch the app and create an account or sign in with an existing email and password.
2. **Main Menu**: Tap **Play** to begin.
3. **Enter Player Names**: Type custom names for Player 1 (X) and Player 2 (O), then tap **Submit Names**.
4. **Gameplay**: Players take turns tapping empty cells on the 3x3 grid.
5. **Win / Draw**:
   - The first player to align 3 symbols horizontally, vertically, or diagonally wins, highlighted with a strike-through line.
   - If all 9 cells are filled with no match, a **Tie Game!** is declared.
6. **Next Steps**: Tap **Play Again** to rematch or **Home** to return to the main menu.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
