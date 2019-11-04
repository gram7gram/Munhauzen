#!/bin/bash

OBB_PATH="/Users/master/Projects/Munhauzen/obb"

function convertRaw() {

    LOCALE=$1

    RAW_DIR="$OBB_PATH/$LOCALE/raw"
    HDPI_DIR="$OBB_PATH/$LOCALE/hdpi"
    MDPI_DIR="$OBB_PATH/$LOCALE/mdpi"

#    rm -rf $HDPI_DIR
#    rm -rf $MDPI_DIR

    cd $RAW_DIR
    for dir in *; do
#        dir="authors"
        echo "|- Converting $LOCALE/raw/$dir..."

        mkdir -p "$HDPI_DIR/$dir"
        mkdir -p "$MDPI_DIR/$dir"

        cd "$RAW_DIR/$dir"
        for file in *; do
            if [[ -f "$file" ]]; then

                #echo "|-- $dir/$file"

                IMG="$RAW_DIR/$dir/$file"

                convert $IMG -resize 100% +profile "icc" $HDPI_DIR/$dir/$file
                test $? -gt 0 && exit 1

                convert $IMG -resize 75% +profile "icc" $MDPI_DIR/$dir/$file
                test $? -gt 0 && exit 1
            fi
        done
    done
}

convertRaw "ru"

convertRaw "en"

echo "=> Finished"