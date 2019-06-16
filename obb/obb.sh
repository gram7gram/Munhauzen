#!/bin/bash

VERSION=$1
if [ -z "$VERSION" ]; then
	echo "Pack expansion files hdpi"
	echo "Usage: command [version]"
	exit
fi

OUTPUT="/mnt/shared-ext4/Projects/munhauzen-web/api/public/expansions"
   
mkdir -p $OUTPUT

AUDIO_DIR="/mnt/shared-ext4/Projects/Munhauzen/Elements/Elements/AUDIO_FINAL/Part_1"

declare -A DEVICE
declare -A I18N
declare -A DPI
declare -A TYPE

DEVICE+=(
	["1"]="normal"
	#["2"]="large"
	#["3"]="xlarge"
)
I18N+=(
	# ["1"]="uk"
	["2"]="en"
	# ["3"]="ru"
)
DPI+=(
	["0"]="hdpi"
	#["2"]="mdpi"
	#["4"]="xhdpi"
	#["5"]="xxhdpi"
	#["6"]="xxxhdpi"
)
echo "=> Clean up"

for i18n in ${!I18N[@]}; do
	L=${I18N[${i18n}]}
	for dpi in ${!DPI[@]}; do
		P=${DPI[${dpi}]}
		for device in ${!DEVICE[@]}; do
			D=${DEVICE[${device}]}

			rm -f $OUTPUT/$L/$P/$D/*.obb
		done
	done
done

for i18n in ${!I18N[@]}; do
	
	L=${I18N[${i18n}]}
	cd $L

	for dpi in ${!DPI[@]}; do
		
		P=${DPI[${dpi}]}
	
		for device in ${!DEVICE[@]}; do

			D=${DEVICE[${device}]}

			NAME="$VERSION-expansion.obb"

			echo "=> Processing $L $P $D"

			mkdir -p $AUDIO_DIR/../audio
			cp -r $AUDIO_DIR /tmp/audio
			
			cd /tmp

			zip -r -q -5 $OUTPUT/$NAME audio
			EXIT_CODE=$?
			if [[ $EXIT_CODE != 0 ]]; then
				exit $EXIT_CODE
			fi
			rm -rf /tmp/audio
			cd -

			if [ -d "$P/$D" ]; then
				cd $P/$D

			  	zip -r -q -5 $OUTPUT/$NAME images
				EXIT_CODE=$?
				if [[ $EXIT_CODE != 0 ]]; then
					exit $EXIT_CODE
				fi

				cd -
			fi

		done
	done

	cd ..
done

echo "=> Finished"
