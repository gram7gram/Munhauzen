# Oh that Munchausen

Interactive audiobook

https://thebaronmunchausen.com

## Pro (all content unlocked)

<table style="width: 100%">
    <tbody style="width: 100%">
        <tr style="width: 100%">
            <td style="width: 50%">
                <a href="#">
                    <img src="https://thebaronmunchausen.com/assets/Elements_web/Download_on_the_App_Store_Badge.png?v=1" alt="Logo" />
                </a>
            </td>
            <td style="width: 50%">
                <a href="#">
                    <img src="https://thebaronmunchausen.com/assets/Elements_web/google-play-badge.png?v=1">
                </a>
            </td>
        </tr>
    </tbody>
</table>

## In-app purchases (free demo)

<table style="width: 100%">
    <tbody style="width: 100%">
        <tr style="width: 100%">
            <td style="width: 50%">
                <a href="https://apps.apple.com/us/app/oh-that-munchausen-tales/id1496752335?l=en&ls=1">
                    <img src="https://thebaronmunchausen.com/assets/Elements_web/Download_on_the_App_Store_Badge.png?v=1" alt="Logo" />
                </a>
            </td>
            <td style="width: 50%">
                <a href="https://play.google.com/store/apps/details?id=en.munchausen.fingertipsandcompany.full&hl=en">
                    <img src="https://thebaronmunchausen.com/assets/Elements_web/google-play-badge.png?v=1">
                </a>
            </td>
        </tr>
    </tbody>
</table>

### Releases

#### Release 3

Added standalone Pro version with all content unlocked

#### Release 2

Added in-app purchases as unlockable chapters

#### Release 1

Created audio-book in EN/RU languages and Demo/Pro versions

## Scripts

Build staging Android APK

`./gradlew :android-en:assembleStaging, :android-ru:assembleStaging`

Build release Android APK

`./gradlew :android-en:assembleRelease :android-ru:assembleRelease`

Generated APK are in directory

`android-en/build/output/generated/apk/staging/universal.apk`

`android-ru/build/output/generated/apk/staging/universal.apk`


## Notes

`keytool -genkey -v -keystore debug.keystore -alias androiddebugkey -keyalg RSA -keysize 2048 -validity 10000`

Install Android Studio RoboVM plugin (same version as in build.gradle)

http://robovm.mobidevelop.com/downloads/releases/idea/org.robovm.idea-2.3.7-plugin-dist.jar

Fix RoboVM facets

https://dkimitsa.github.io/2018/05/04/idea-fixing-android-gradle-facet3/
