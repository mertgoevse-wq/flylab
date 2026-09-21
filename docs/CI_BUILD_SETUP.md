# CI Build Setup

## GitHub Actions Workflow

The repository requires a GitHub Actions workflow for reproducible Android builds. The workflow file cannot be pushed via the current OAuth token due to workflow scope limitations.

## Workflow Specification

Create `.github/workflows/android-build.yml`:

```yaml
name: Android Build

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle
    - run: chmod +x gradlew
    - run: ./gradlew testDebugUnitTest --no-daemon --stacktrace
    - uses: actions/upload-artifact@v4
      if: always()
      with:
        name: test-reports
        path: app/build/reports/tests/

  build:
    runs-on: ubuntu-latest
    needs: test
    steps:
    - uses: actions/checkout@v4
    - uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle
    - run: chmod +x gradlew
    - run: ./gradlew assembleDebug --no-daemon --stacktrace
    - run: ./gradlew assembleRelease --no-daemon --stacktrace
    - uses: actions/upload-artifact@v4
      with:
        name: flylab-debug
        path: app/build/outputs/apk/debug/*.apk
    - uses: actions/upload-artifact@v4
      with:
        name: flylab-release
        path: app/build/outputs/apk/release/*.apk

  lint:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle
    - run: chmod +x gradlew
    - run: ./gradlew lintDebug --no-daemon --stacktrace
    - uses: actions/upload-artifact@v4
      if: always()
      with:
        name: lint-reports
        path: app/build/reports/lint-results-*.html
```

## Manual Setup

1. Create the `.github/workflows/` directory in the repository
2. Add the `android-build.yml` file with the content above
3. Commit and push using a token with `workflow` scope or through the GitHub web interface

## Local Build Limitation

The PRoot environment has an AAPT2 binary incompatibility that blocks local APK generation:
```
AAPT2 aapt2-8.1.4-10154469-linux Daemon #0: Daemon startup failed
```

This is an environment limitation, not a project defect. The CI workflow provides reproducible builds in a standard Ubuntu Android environment.

## Workaround

The `gradle.properties` file includes:
```properties
android.aapt2FromMavenOverride=/usr/lib/android-sdk/build-tools/debian/aapt2
```

This allows some Gradle tasks to run locally, but full APK assembly requires the CI environment.
