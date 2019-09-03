#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/interaction SERVANTS"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

cd "$SRC_DIR/servants_1"

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 2048 -quality 80 $OBB_PATH/xxxhdpi/servants/$file
        convert $file -resize 1536 -quality 80 $OBB_PATH/xxhdpi/servants/$file
        convert $file -resize 1080 -quality 80 $OBB_PATH/xhdpi/servants/$file
        convert $file -resize 750 -quality 80 $OBB_PATH/hdpi/servants/$file
        convert $file -resize 640 -quality 80 $OBB_PATH/mdpi/servants/$file
        convert $file -resize 480 -quality 80 $OBB_PATH/ldpi/servants/$file

    fi
done

cd "$SRC_DIR/servants_2"

for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 2048 -quality 80 $OBB_PATH/xxxhdpi/servants/$file
        convert $file -resize 1536 -quality 80 $OBB_PATH/xxhdpi/servants/$file
        convert $file -resize 1080 -quality 80 $OBB_PATH/xhdpi/servants/$file
        convert $file -resize 750 -quality 80 $OBB_PATH/hdpi/servants/$file
        convert $file -resize 640 -quality 80 $OBB_PATH/mdpi/servants/$file
        convert $file -resize 480 -quality 80 $OBB_PATH/ldpi/servants/$file

    fi
done

echo "=> Finished"