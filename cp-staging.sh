V=$1
OUT=~/Desktop/$V-test.zip

cp ./android-en/build/outputs/apk/staging/android-en-universal-staging.apk ~/Desktop/android-en-staging.apk
cp ./android-ru/build/outputs/apk/staging/android-ru-universal-staging.apk ~/Desktop/android-ru-staging.apk

cd ~/Desktop

zip -r $OUT android-en-staging.apk android-ru-staging.apk

echo "Release packed into: $OUT"