#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/interaction SERVANTS"
DEST_PATH="/Users/master/Projects/Munhauzen/obb/assets/servants"

cd "$SRC_DIR/servants_1"

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 800 -quality 80 $DEST_PATH/$file
        EXIT_CODE=$?
        if [[ $EXIT_CODE != 0 ]]; then
            exit $EXIT_CODE
        fi

    fi
done

cd "$SRC_DIR/servants_2"

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 800 -quality 80 $DEST_PATH/$file
        EXIT_CODE=$?
        if [[ $EXIT_CODE != 0 ]]; then
            exit $EXIT_CODE
        fi

    fi
done

echo "=> Finished"