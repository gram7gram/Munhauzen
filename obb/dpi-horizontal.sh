#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL/drawable-horizontal"
OBB_PATH=/Users/master/Projects/Munhauzen/obb

cd $SRC_DIR

echo "=> Renaming sources..."
for file in *.jpg; do

	newFile="${file// /_}"

	if [[ "$newFile" != "$file" ]]; then

		echo "Renaming: $file => $newFile"

		mv "$SRC_DIR/$file" "$SRC_DIR/$newFile"

	fi
done

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize x2048 -quality 80 $OBB_PATH/xxxhdpi/images/$file
        convert $file -resize x1536 -quality 80 $OBB_PATH/xxhdpi/images/$file
        convert $file -resize x1080 -quality 80 $OBB_PATH/xhdpi/images/$file
        convert $file -resize x750 -quality 80 $OBB_PATH/hdpi/images/$file
        convert $file -resize x640 -quality 80 $OBB_PATH/mdpi/images/$file
        convert $file -resize x480 -quality 80 $OBB_PATH/ldpi/images/$file

    fi
done

echo "=> Finished"