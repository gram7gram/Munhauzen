#!/bin/bash

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
	#["1"]="uk"
	#["2"]="en"
	["3"]="ru"
)
DPI+=(
	["0"]="hdpi"
	["2"]="mdpi"
	["4"]="xhdpi"
	["5"]="xxhdpi"
	["6"]="xxxhdpi"
)

echo "=> Create directories..."
for i18n in ${!I18N[@]}; do
	for dpi in ${!DPI[@]}; do
		for device in ${!DEVICE[@]}; do
			P=${DPI[${dpi}]}
			D=${DEVICE[${device}]}
			L=${I18N[${i18n}]}
			mkdir -p $L/$P/$D/drawable
		done
	done
done

echo "=> Create nodpi copy"
for i18n in ${!I18N[@]}; do
	L=${I18N[${i18n}]}
	if [ -d ./$L ]; then
		cd $L
		for dpi in ${!DPI[@]}; do
			for device in ${!DEVICE[@]}; do
				P=${DPI[${dpi}]}
				D=${DEVICE[${device}]}
				if [ -d "nodpi" ]; then
					echo "copy to $P/$D/drawable" 
					cp -f nodpi/* $P/$D/drawable
				fi
			done
		done
		cd  ..
	fi
done

echo "=> Finished"