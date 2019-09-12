#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL/drawable"
SRC_DIR2="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL/drawable-horizontal"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

mkdir -p $OBB_PATH/raw/images

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

        convert $file -resize 1600 -quality 80 -colors 256 $OBB_PATH/raw/images/$file

    fi
done

cd $SRC_DIR2

echo "=> Renaming horizontal sources..."
for file in *.jpg; do

	newFile="${file// /_}"

	if [[ "$newFile" != "$file" ]]; then

		echo "Renaming: $file => $newFile"

		mv "$SRC_DIR2/$file" "$SRC_DIR2/$newFile"

	fi
done

echo "=> Converting horizontal sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize x1600 -quality 80 -colors 256 $OBB_PATH/raw/images/$file

    fi
done

echo "=> Finished"