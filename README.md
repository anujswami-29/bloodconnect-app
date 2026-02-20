# RakhtSetu

Blood donation app connecting donors with emergencies in real time.

## World-Class Features (Demo)

- **AI donor matching** – Blood group, distance, availability, response history, rare blood priority
- **Hospital-verified requests** – Verified badge, rare blood alerts
- **Gamification** – Badges, impact score, leaderboard
- **Donor passport** – Eligibility countdown, donation history, health tips
- **Admin Command Center** – Live stats, heatmap placeholder, blood stock prediction
- **AI Ops Assistant** – Ask: inactive volunteers, pending requests, predictions
- **Predictive analytics** – Demand spikes, donor shortage by region
- **Multi-language** – Hindi strings (values-hi)

## Build APK

**From Android Studio:**
1. Open the project in Android Studio
2. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
3. APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

**From command line:**
```bash
.\gradlew assembleDebug
```
APK location: `app\build\outputs\apk\debug\app-debug.apk`

## Setup for GitHub

### Option A: Using Git (recommended)
1. Open terminal in project folder
2. `git init`
3. `git add .`
4. `git commit -m "Initial commit"`
5. Create new repository on GitHub
6. `git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git`
7. `git push -u origin main`

### Option B: Upload as ZIP
1. Right-click the `AndroidApp` folder → **Send to → Compressed (zipped) folder**
2. (Optional) Delete `build`, `.gradle`, `.idea` folders first to reduce zip size
3. On GitHub: **New repository** → **"uploading an existing file"** → drag the zip
