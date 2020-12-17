V=$1
OUT=~/Desktop/$V-pro.zip

cp ./android-en-pro/build/outputs/apk/release/android-en-pro-universal-release.apk ~/Desktop/android-en-pro-release.apk
cp ./android-en-pro/build/outputs/bundle/release/android-en-pro.aab ~/Desktop/android-en-pro-release.aab
cp ./android-ru-pro/build/outputs/apk/release/android-ru-pro-universal-release.apk ~/Desktop/android-ru-pro-release.apk
cp ./android-ru-pro/build/outputs/bundle/release/android-ru-pro.aab ~/Desktop/android-ru-pro-release.aab

cd ~/Desktop

zip -r $OUT android-en-pro-release.apk android-en-pro-release.aab android-ru-pro-release.apk android-ru-pro-release.aab

echo "Release packed into: $OUT"