#!/usr/bin/env bash

IN="/Users/master/Projects/Munhauzen/an_starts_2.gif"
OUT="/Users/master/Projects/Munhauzen/obb/an_stars.png"

montage $IN -tile 10x -geometry +0+0 -alpha On -background "rgba(0, 0, 0, 0.0)" -quality 80 $OUT