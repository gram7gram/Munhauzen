#!/bin/bash

SRC_DIR="/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL"
OBB_PATH="/Users/master/Projects/Munhauzen/obb"

function prepare() {

    part=$1

    echo "|- $part"

    inputDir="$SRC_DIR$part"
    outputPartDir="$OBB_PATH$part"
    outputDir="$outputPartDir/raw/images"

    mkdir -p $outputDir

#    rm -f $outputDir/*

    cd $inputDir

    for file in *.jpg; do

        newFile="${file// /_}"

        if [[ "$newFile" != "$file" ]]; then

            echo "Renaming: $file => $newFile"

            mv "$inputDir/$file" "$inputDir/$newFile"

        fi
    done

    cd $inputDir

    for file in *.jpg; do

        echo "|-- $file"

        currentFile="$inputDir/$file"

        convert $currentFile -quality 80 -colors 256 +profile "icc" "$outputDir/$file"
        test $? -gt 0 && exit 1

    done
}

prepare "/Part_demo"

prepare "/Part_1"

prepare "/Part_2"