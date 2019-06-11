#!/bin/bash

SRC_DIR=$1
if [ -z "SRC_DIR" ]; then
	echo "Convert drawables to hdpi"
	echo "Usage: command [source_directory]"
	exit
fi

OBB_PATH=/mnt/shared-ext4/Projects/Munhauzen/obb

declare -A DEVICE
declare -A I18N
declare -A DPI

DEVICE+=(
	["1"]="normal"
	# ["2"]="large"
	# ["3"]="xlarge"
)
I18N+=(
	# ["1"]="uk"
	["2"]="en"
	# ["3"]="ru"
)
DPI+=(
	["0"]="hdpi"
	# ["1"]="mdpi"
	# ["2"]="xhdpi"
	# ["3"]="xxhdpi"
	# ["4"]="xxxhdpi"
)

echo "=> Clean up..."
for i18n in ${!I18N[@]}; do
	for dpi in ${!DPI[@]}; do
		P=${DPI[${dpi}]}
		L=${I18N[${i18n}]}
		rm -rf $L/$P
	done
done

echo "=> Create directories..."
for i18n in ${!I18N[@]}; do
	for dpi in ${!DPI[@]}; do
		for device in ${!DEVICE[@]}; do
			P=${DPI[${dpi}]}
			D=${DEVICE[${device}]}
			L=${I18N[${i18n}]}
			mkdir -p $L/$P/$D/images
		done
	done
done

cd $SRC_DIR 

echo "=> Renaming sources..."
for file in *.jpg *.png; do

	newFile="${file// /_}"

	if [ "$newFile" != "$file" ]; then

		echo "Renaming: $file => $newFile"

		mv "$SRC_DIR/$file" "$SRC_DIR/$newFile"

	fi
done;

echo "=> Converting sources..."
for i18n in ${!I18N[@]}; do
	L=${I18N[${i18n}]}
	for file in *.jpg *.png; do 
		if [ -f $file ]; then

			echo "=> $file"

			convert $file -resize x800 -quality 90 $OBB_PATH/$L/hdpi/normal/images/$file
			EXIT_CODE=$?
			if [[ $EXIT_CODE != 0 ]]; then
			    exit $EXIT_CODE
			fi

		fi
	done
done

echo "=> Finished"