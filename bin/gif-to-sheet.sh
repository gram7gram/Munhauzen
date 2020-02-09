#!/usr/bin/env bash

OBB_PATH="/Users/master/Projects/Munhauzen/obb"

IN="$1"
OUT="$OBB_PATH/$2"

montage $IN \
    -tile 10x -geometry +0+0 -alpha On -background "rgba(0, 0, 0, 0.0)" -quality 80 \
    $OUT