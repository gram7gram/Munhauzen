#!/bin/bash

SRC_DIR="/Users/master/Projects/Munhauzen/obb/raw"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

#mkdir -p $OBB_PATH/xxxhdpi/fails
#mkdir -p $OBB_PATH/xxhdpi/fails
#mkdir -p $OBB_PATH/xhdpi/fails
mkdir -p $OBB_PATH/hdpi/fails
#mkdir -p $OBB_PATH/mdpi/fails
#mkdir -p $OBB_PATH/ldpi/fails

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