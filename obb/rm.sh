#!/bin/bash

FILE="$1"
if [ -z "$FILE" ]; then
	echo "Remove drawable from ldpi/mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi"
	echo "Usage: command [name]"
	exit
fi

OBB_PATH=/mnt/shared-ext4/Projects/Munhauzen/obb

declare -A DEVICE
declare -A I18N
declare -A DPI

DEVICE+=(
	["1"]="normal"
	["2"]="large"
	["3"]="xlarge"
)
I18N+=(
	["1"]="uk"
	["2"]="en"
	["3"]="ru"
)
DPI+=(
	["0"]="hdpi"
	["1"]="ldpi"
	["2"]="mdpi"
	["4"]="xhdpi"
	["5"]="xxhdpi"
	["6"]="xxxhdpi"
)

for i18n in ${!I18N[@]}; do
	for dpi in ${!DPI[@]}; do
		for device in ${!DEVICE[@]}; do
			
			P=${DPI[${dpi}]}
			D=${DEVICE[${device}]}
			L=${I18N[${i18n}]}
			
			FILE_PATH=$OBB_PATH/$L/$P/$D/drawable/$FILE
			if [ -f $FILE_PATH ]; then
				echo "=> $L/$P/$D/drawable/$FILE"
				rm $FILE_PATH
			fi
		done
	done
done

echo "=> Finished"