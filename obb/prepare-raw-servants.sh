#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/interaction SERVANTS"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

cd $SRC_DIR

convert inter_servants_fond.jpg -resize x1600 -quality 80 $OBB_PATH/ru/raw/images/inter_servants_fond.jpg

cp $OBB_PATH/en/raw/servants/inter_servants_fond.jpg $OBB_PATH/ru/raw/servants/inter_servants_fond.jpg


cd "$SRC_DIR/servants_1"

echo "=> Converting sources..."
for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -quality 80 -colors 256 $OBB_PATH/ru/raw/servants/$file

        cp $OBB_PATH/ru/raw/servants/$file $OBB_PATH/en/raw/servants/$file

    fi
done

cd "$SRC_DIR/servants_2"

for file in *.jpg; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -quality 80 -colors 256 $OBB_PATH/en/raw/servants/$file

        cp $OBB_PATH/en/raw/servants/$file $OBB_PATH/ru/raw/servants/$file

    fi
done

echo "=> Finished"