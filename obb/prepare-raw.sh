#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL/drawable"
SRC_DIR2="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL/drawable-horizontal"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

mkdir -p $OBB_PATH/ru/raw/images
mkdir -p $OBB_PATH/en/raw/images

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

    echo "=> $file"

    convert $file -resize 1600 -quality 80 -colors 256 $OBB_PATH/ru/raw/images/$file
    test $? -gt 0 && exit 1

    cp $OBB_PATH/ru/raw/images/$file $OBB_PATH/en/raw/images/$file

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

    echo "=> $file"

    convert $file -resize x1600 -quality 80 -colors 256 $OBB_PATH/ru/raw/images/$file
    test $? -gt 0 && exit 1

    cp $OBB_PATH/ru/raw/images/$file $OBB_PATH/en/raw/images/$file

done

echo "=> Finished"