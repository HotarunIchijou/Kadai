<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="128" height="128" alt="Kadai Icon" />
</p>

<h1 align="center">Kadai</h1>

<p align="center">
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white&logoSize=auto&color=7F52FF" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Compose-34A853?style=for-the-badge&logo=jetpackcompose&logoColor=white&logoSize=auto&labelColor=4285F4&color=34A853" alt="Jetpack Compose" /></a>
  <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Android_8.1%2B-blue?style=for-the-badge&logo=android&logoColor=white&logoSize=auto&labelColor=%233DDC84&color=blue" alt="Android version" /></a>
  <a href="https://github.com/HotarunIchijou/Kadai/blob/master/LICENSE"><img src="https://img.shields.io/badge/GNU%20GPL3-blue?style=for-the-badge&logo=gplv3&logoColor=white&logoSize=auto&labelColor=%23BD0000&color=blue" alt="GNU GPLv3" /></a>
</p>

**Kadai** is a modern, lightweight, and privacy-respecting Android todo app. Built from the ground up with 100% Kotlin, Jetpack Compose, and
Material 3 Expressive design, it focuses on doing one thing exceptionally well: keeping track of what you need to do without getting in your way.

## ✨ Features

- **Material 3 Expressive UI**
    - Full support for **Dynamic Color** (Material You monet theming generated from your wallpaper)
    - Light, Dark, and System default appearance modes
    - Expressive motion physics, fluid spatial page transitions, and subtle scale-in/out animations

- **Precision Reminders & Alarms**
    - Exact background reminders scheduled with Android's `AlarmManager`
    - Automatically reschedules all pending alarms after your device restarts via `BootReceiver`

- **Thoughtful Interactions**
    - Inline date and time pickers with contextual button groups
    - Instant task search with recent search query history and quick suggestions
    - Multi-field sorting (sort by creation date or due date, ascending or descending)

- **100% Offline & Private**
    - Everything is saved locally in a Room database on your device
    - `android.permission.INTERNET` is not even declared in the manifest

---

## 🛠️ Tech Stack & Architecture

Kadai is built using the latest Android development standards and Jetpack libraries:

| Layer | Technologies |
|:---|:---|
| **Language** | [Kotlin](https://kotlinlang.org/) |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) |
| **Design** | [Material 3 Expressive](https://material.io) |
| **Navigation** | [Navigation 3](https://developer.android.com/guide/navigation)|
| **Dependency Injection** | [Dagger Hilt](https://dagger.dev/hilt/) |
| **Local Database** | [Room Database](https://developer.android.com/training/data-storage/room)|
| **Preferences** | [Jetpack DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) |
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

## ⚖️ License
This project is licensed under the GNU General Public License v3.0. see the [LICENSE](LICENSE) file for details.
