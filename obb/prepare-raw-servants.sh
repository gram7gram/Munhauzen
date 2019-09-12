#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/interaction SERVANTS"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

cd "$SRC_DIR/servants_1"

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -quality 80 -colors 256 $OBB_PATH/raw/servants/$file

    fi
done

cd "$SRC_DIR/servants_2"

for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -quality 80 -colors 256 $OBB_PATH/raw/servants/$file

    fi
done

echo "=> Finished"