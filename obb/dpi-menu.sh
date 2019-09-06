#!/bin/bash

SRC_DIR="/Users/master/Projects/Munhauzen/obb/raw"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

#mkdir -p $OBB_PATH/xxxhdpi/menu
#mkdir -p $OBB_PATH/xxhdpi/menu
#mkdir -p $OBB_PATH/xhdpi/menu
mkdir -p $OBB_PATH/hdpi/menu
#mkdir -p $OBB_PATH/mdpi/menu
#mkdir -p $OBB_PATH/ldpi/menu

cd "$SRC_DIR/menu"

echo "=> Converting menu..."
for file in *.png; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 50% -quality 80 -colors 256 $OBB_PATH/hdpi/menu/$file

    fi
done

echo "=> Finished"