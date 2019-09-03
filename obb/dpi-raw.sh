#!/bin/bash

SRC_DIR="/Users/master/Projects/Munhauzen/obb/raw"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

mkdir -p $OBB_PATH/xxxhdpi/menu
mkdir -p $OBB_PATH/xxhdpi/menu
mkdir -p $OBB_PATH/xhdpi/menu
mkdir -p $OBB_PATH/hdpi/menu
mkdir -p $OBB_PATH/mdpi/menu
mkdir -p $OBB_PATH/ldpi/menu

mkdir -p $OBB_PATH/xxxhdpi/fails
mkdir -p $OBB_PATH/xxhdpi/fails
mkdir -p $OBB_PATH/xhdpi/fails
mkdir -p $OBB_PATH/hdpi/fails
mkdir -p $OBB_PATH/mdpi/fails
mkdir -p $OBB_PATH/ldpi/fails

cd "$SRC_DIR/menu"

echo "=> Converting menu..."
for file in *.png; do
    if [[ -f "$file" ]]; then

        echo "=> $file"
#
#        convert $file -resize x480 -quality 80 -colors 256 $OBB_PATH/xxxhdpi/menu/$file
#        convert $file -resize x400 -quality 80 -colors 256 $OBB_PATH/xxhdpi/menu/$file
#        convert $file -resize x300 -quality 80 -colors 256 $OBB_PATH/xhdpi/menu/$file
        convert $file -resize x200 -quality 80 -colors 256 $OBB_PATH/hdpi/menu/$file
#        convert $file -resize x150 -quality 80 -colors 256 $OBB_PATH/mdpi/menu/$file
#        convert $file -resize x100 -quality 80 -colors 256 $OBB_PATH/ldpi/menu/$file

    fi
done

cd "$SRC_DIR/fails"

echo "=> Converting fails..."
for file in *.png; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

#        convert $file -resize 1080 -quality 80 -colors 256 $OBB_PATH/xxxhdpi/fails/$file
#        convert $file -resize 750 -quality 80 -colors 256 $OBB_PATH/xxhdpi/fails/$file
#        convert $file -resize 500 -quality 80 -colors 256 $OBB_PATH/xhdpi/fails/$file
        convert $file -resize 375 -quality 80 -colors 256 $OBB_PATH/hdpi/fails/$file
#        convert $file -resize 320 -quality 80 -colors 256 $OBB_PATH/mdpi/fails/$file
#        convert $file -resize 240 -quality 80 -colors 256 $OBB_PATH/ldpi/fails/$file

    fi
done

echo "=> Finished"