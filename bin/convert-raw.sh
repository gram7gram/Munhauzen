#!/bin/bash

OBB_PATH="/Users/master/Projects/Munhauzen/obb"

function convertRaw() {

    echo "|- $1"

    inputDir="$OBB_PATH$1"

    RAW_DIR="$inputDir/raw"
    HDPI_DIR="$inputDir/hdpi"
    MDPI_DIR="$inputDir/mdpi"

#    rm -rf $HDPI_DIR
#    rm -rf $MDPI_DIR

    cd $RAW_DIR
    for dir in *; do
#        dir="authors"
        echo "|-- Converting $dir ..."

        currentDir="$RAW_DIR/$dir"

        mkdir -p "$HDPI_DIR/$dir"
        mkdir -p "$MDPI_DIR/$dir"

        cd $currentDir
        for file in *; do
            if [[ -f "$file" ]]; then

                #echo "|-- $dir/$file"

                currentFile="$currentDir/$file"

                convert $currentFile -quality 80 -colors 256 +profile "icc" $HDPI_DIR/$dir/$file
                test $? -gt 0 && exit 1

                convert $currentFile -quality 80 -colors 256 -resize 75% +profile "icc" $MDPI_DIR/$dir/$file
                test $? -gt 0 && exit 1
            fi
        done
    done
}

convertRaw "/Part_demo"

convertRaw "/Part_1"

convertRaw "/Part_2"

echo "=> Finished"