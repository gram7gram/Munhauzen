#!/bin/bash

SRC_DIR=$1
if [[ -z "SRC_DIR" ]]; then
	echo "Convert drawables"
	echo "Usage: command [source_directory]"
	exit
fi

OBB_PATH=/Users/master/Projects/Munhauzen/obb

cd $SRC_DIR

echo "=> Renaming sources..."
for file in *.jpg; do

	newFile="${file// /_}"

	if [[ "$newFile" != "$file" ]]; then

		echo "Renaming: $file => $newFile"

		mv "$SRC_DIR/$file" "$SRC_DIR/$newFile"

	fi
done;

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        mkdir -p $OBB_PATH/en/images

        convert $file -resize 800 -quality 80 $OBB_PATH/en/images/$file
        EXIT_CODE=$?
        if [[ $EXIT_CODE != 0 ]]; then
            exit $EXIT_CODE
        fi

    fi
done

echo "=> Finished"