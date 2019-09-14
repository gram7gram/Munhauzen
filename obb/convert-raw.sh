#!/bin/bash

SRC_DIR="/Users/master/Projects/Munhauzen/obb/raw"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

cd "$SRC_DIR"

for dir in *; do
    if [[ -d "$dir" ]]; then

        cd "$SRC_DIR/$dir"

        mkdir -p "$OBB_PATH/hdpi/$dir"
        mkdir -p "$OBB_PATH/mdpi/$dir"

        echo "|- Converting $dir..."
        for file in *; do
            if [[ -f "$file" ]]; then

                echo "|-- $dir/$file"

                IMG="$SRC_DIR/$dir/$file"

                convert $IMG -resize 100% +profile "icc" $OBB_PATH/hdpi/$dir/$file
                convert $IMG -resize 75% +profile "icc" $OBB_PATH/mdpi/$dir/$file

            fi
        done

        cd "$SRC_DIR"

    fi
done

echo "=> Finished"