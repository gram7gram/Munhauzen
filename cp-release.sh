V=$1
OUT=~/Desktop/$V.zip

cp ./android-en/build/outputs/apk/release/android-en-universal-release.apk ~/Desktop/android-en-release.apk
cp ./android-en/build/outputs/bundle/release/android-en.aab ~/Desktop/android-en-release.aab
cp ./android-ru/build/outputs/apk/release/android-ru-universal-release.apk ~/Desktop/android-ru-release.apk
cp ./android-ru/build/outputs/bundle/release/android-ru.aab ~/Desktop/android-ru-release.aab

cd ~/Desktop

zip -r $OUT android-en-release.apk android-en-release.aab android-ru-release.apk android-ru-release.aab

echo "Release packed into: $OUT"