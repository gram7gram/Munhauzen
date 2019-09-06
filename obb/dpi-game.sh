#!/bin/bash

SRC_DIR="/Users/master/Projects/Munhauzen/obb/raw"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

#mkdir -p $OBB_PATH/xxxhdpi/GameScreen
#mkdir -p $OBB_PATH/xxhdpi/GameScreen
#mkdir -p $OBB_PATH/xhdpi/GameScreen
mkdir -p $OBB_PATH/hdpi/GameScreen
#mkdir -p $OBB_PATH/mdpi/GameScreen
#mkdir -p $OBB_PATH/ldpi/GameScreen

cd "$SRC_DIR/GameScreen"

echo "=> Converting GameScreen..."
for file in *.png; do
    if [[ -f "$file" ]]; then

        echo "=> $file"

        convert $file -resize 50% -quality 80 -colors 256 $OBB_PATH/hdpi/GameScreen/$file

    fi
done

echo "=> Finished"