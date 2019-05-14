#!/bin/bash

VERSION=$1
if [ -z "$VERSION" ]; then
	echo "Pack expansion files ldpi/mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi"
	echo "Usage: command [version]"
	exit
fi

OUTPUT="/mnt/shared-ext4/Projects/Munhauzen/obb/expansions"
if [ -d "OUTPUT" ]; then
    mkdir -P $OUTPUT
fi

bash ./nodpi.sh

declare -A DEVICE
declare -A I18N
declare -A DPI
declare -A TYPE

DEVICE+=(
	["1"]="normal"
	["2"]="large"
	["3"]="xlarge"
)
I18N+=(
	# ["1"]="uk"
	# ["2"]="en"
	["3"]="ru"
)
DPI+=(
	["0"]="hdpi"
	["2"]="mdpi"
	["4"]="xhdpi"
	["5"]="xxhdpi"
	["6"]="xxxhdpi"
)

for i18n in ${!I18N[@]}; do
	
	L=${I18N[${i18n}]}
	cd $L

	for dpi in ${!DPI[@]}; do
		
		P=${DPI[${dpi}]}
	
		for device in ${!DEVICE[@]}; do

			D=${DEVICE[${device}]}

			NAME="$VERSION-expansion.obb"

			echo "=> Processing $L $P $D" 

			if [ -d "raw" ]; then
				mkdir -p $OUTPUT/$L/$P/$D

				zip -r -q $OUTPUT/$L/$P/$D/$NAME raw
				EXIT_CODE=$?
				if [[ $EXIT_CODE != 0 ]]; then
					exit $EXIT_CODE
				fi
			fi

			if [ -d "$P/$D" ]; then
				mkdir -p $OUTPUT/$L/$P/$D

				cd $P/$D

			  	zip -r -q $OUTPUT/$L/$P/$D/$NAME drawable
				EXIT_CODE=$?
				if [[ $EXIT_CODE != 0 ]]; then
					exit $EXIT_CODE
				fi

				cd ../..
			fi

		done
	done

	cd ..
done

echo "=> Finished"