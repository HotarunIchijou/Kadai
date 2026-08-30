# Kadai (課題)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF.svg?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3_Expressive-4285F4.svg?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Navigation 3](https://img.shields.io/badge/Navigation-Navigation_3-00C853.svg?style=flat)](https://developer.android.com/guide/navigation)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Min SDK](https://img.shields.io/badge/Min_SDK-27-informational.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target_SDK-37-informational.svg)](https://developer.android.com)

**Kadai** *(Japanese for tasks, assignments, or challenges)* is a modern, lightweight, and
privacy-respecting Android todo app. Built from the ground up with 100% Kotlin, Jetpack Compose, and
Material 3 Expressive design, it focuses on doing one thing exceptionally well: keeping track of
what you need to do without getting in your way.

No subscriptions, no accounts, no analytics, no cloud lock-in—and not even an internet permission.
Just your tasks, your schedule, and a clean UI that feels right at home on modern Android.

---

## ✨ Features

- **🎨 Material 3 Expressive UI**
    - Full support for **Dynamic Color** (Material You monet theming generated from your wallpaper).
    - Light, Dark, and System default appearance modes.
    - Expressive motion physics, fluid spatial page transitions, and subtle scale-in/out animations.
    - Floating bottom toolbar for switching between **Pending** and **Completed** tasks, plus an
      accessible modal wide navigation rail.

- **⏱️ Precision Reminders & Alarms**
    - Exact background reminders scheduled with Android's `AlarmManager` (
      `setExactAndAllowWhileIdle`).
    - Automatically reschedules all pending alarms after your device restarts via `BootReceiver`.
    - Notifications link directly into specific tasks through deep links (`kadai://task`).

- **⚡ Thoughtful Interactions**
    - Dual-page task list with smooth swipeable pagination.
    - Inline date and time pickers with contextual button groups.
    - Instant task search with recent search query history and quick suggestions.
    - Multi-field sorting (sort by creation date or due date, ascending or descending).
    - One-tap **Undo** for accidental completions, complete with smooth automatic scroll restoration
      back to the item.
    - Friendly permission guide to help configure exact alarms and notification permissions on
      Android 13+.

- **🔒 100% Offline & Private**
    - Everything is saved locally in a Room database on your device.
    - `android.permission.INTERNET` is not even declared in the manifest.

---

## 🛠️ Tech Stack & Architecture

Kadai is built using the latest Android development standards and Jetpack libraries:

| Layer                    | Technologies                                                                                                                                                                             |
|:-------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Language**             | [Kotlin](https://kotlinlang.org/) (2.4+) with Coroutines & StateFlow                                                                                                                     |
| **UI Framework**         | [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3 Expressive](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) |
| **Navigation**           | [Navigation 3](https://developer.android.com/guide/navigation) (`androidx.navigation3.ui:NavDisplay`, back stack decorators)                                                             |
| **Dependency Injection** | [Dagger Hilt](https://dagger.dev/hilt/)                                                                                                                                                  |
| **Local Database**       | [Room Database](https://developer.android.com/training/data-storage/room) via KSP (Kotlin Symbol Processing)                                                                             |
| **Preferences**          | [Jetpack DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)                                                                                    |
| **Architecture**         | Modern Android Architecture (Unidirectional Data Flow / MVVM, Repository pattern)                                                                                                        |
| **Target Platforms**     | Android 8.1 (API 27) up to Android 16+ (API 37)                                                                                                                                          |

---

## 📁 Project Structure

```text
org.kaorun.kadai
├── data
│   ├── dao           # Room DAOs (Tasks, Recent Searches)
│   ├── db            # Room Database definition & type converters
│   ├── entity        # Data entities (Task, RecentSearch)
│   ├── model         # Sort and Theme models
│   └── repository    # Task, UserPreferences, and Search repositories
├── di                # Hilt DI modules (Database, DataStore, Alarm)
├── reminder
│   ├── data          # Scheduled notification persistence
│   ├── impl          # AlarmManager scheduler & Notifier implementations
│   └── receiver      # AlarmReceiver & BootReceiver
└── ui
    ├── icons         # Custom vector icons
    ├── navigation    # Navigation 3 back stack, routes, and deep link parser
    ├── screens
    │   ├── main      # Main task list screen, floating toolbar, search bar
    │   ├── task      # Task creation & editing screen with date/time pickers
    │   ├── permission# Notification and exact alarm permission onboarding
    │   └── settings  # Appearance (theme mode, dynamic color) and About screens
    └── theme         # MaterialExpressiveTheme, colors, and typography
```

---

## 🚀 Building & Running

### Prerequisites

- **Android Studio** Ladybug (2024.2+) or newer
- **JDK 17** or higher
- **Android SDK** with API level 37 installed

### Building from Command Line

1. **Clone the repository:**
   ```bash
   git clone https://github.com/HotarunIchijou/Kadai.git
   cd Kadai
   ```

2. **Build debug APK:**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Install on a connected device / emulator:**
   ```bash
   ./gradlew installDebug
   ```

---

## 💬 Community & Contact

- **Source Code:** [GitHub Repository](https://github.com/HotarunIchijou/Kadai)
- **Bug Reports & Feature Requests:
  ** [GitHub Issues](https://github.com/HotarunIchijou/Kadai/issues)
- **Developer:** Reach out on Telegram [@KaorunIchijou](https://t.me/KaorunIchijou)

---

## 📄 License

This project is free software, licensed under the **GNU General Public License v3.0** (GPLv3). See
the [LICENSE](LICENSE) file for more details.
