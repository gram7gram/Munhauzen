V=$1
OUT=~/Desktop/$V-test.zip

cp ./android-en-pro/build/outputs/apk/staging/android-en-pro-universal-staging.apk ~/Desktop/android-en-pro-staging.apk
cp ./android-ru-pro/build/outputs/apk/staging/android-ru-pro-universal-staging.apk ~/Desktop/android-ru-pro-staging.apk

cd ~/Desktop

zip -r $OUT android-en-pro-staging.apk android-ru-pro-staging.apk

echo "Release packed into: $OUT"