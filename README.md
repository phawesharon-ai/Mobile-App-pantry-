# Smart Pantry Manager

A Java Android app for tracking pantry ingredients and suggesting only recipes whose required ingredients and quantities are all available.

## Database
The app uses SQLite through `SQLiteOpenHelper`. Pantry items persist locally on the device. The database is seeded with 20 simple recipes on first launch.

## Open and run
1. Extract the ZIP file.
2. Open Android Studio.
3. Choose **Open** and select the `SmartPantryManager` folder (the folder containing `settings.gradle`).
4. Allow Gradle sync to finish. This project uses Android Gradle Plugin 8.2.2 and compile SDK 34; install the requested SDK if Android Studio asks.
5. Create/start an Android emulator in Device Manager or connect an Android phone with USB debugging enabled.
6. Press the green **Run** triangle.

## Use
- Add ingredients with a name, quantity, and unit.
- Tap a pantry item to edit it.
- Long-press a pantry item to delete it.
- Open Suggested Recipes. Only full matches with sufficient quantities appear.
- Tap a suggested recipe to see its ingredients and method.
- Change the expiry reminder preference in Settings.

## Strict matching
Each recipe ingredient is stored as `name:quantity:unit`. A recipe appears only when every required pantry ingredient has the normalized same name, the same unit, and quantity greater than or equal to the requirement. This avoids suggesting a recipe when an ingredient is missing or insufficient.

## Student
Student number: 402413319
Surname: Nephawe

## Before submission
Add your own meaningful GitHub commits throughout development, take real screenshots of the running app, write your report based on what you actually tested, and include your 5–7 minute narrated video. Do not claim features work until you have run and tested them.
