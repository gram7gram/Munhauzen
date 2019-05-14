#!/bin/bash

SRC_DIR=$1
if [ -z "SRC_DIR" ]; then
	echo "Convert drawables to ldpi/mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi"
	echo "Usage: command [source_directory]"
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

# echo "=> Clean up..."
# for i18n in ${!I18N[@]}; do
# 	for dpi in ${!DPI[@]}; do
# 		P=${DPI[${dpi}]}
# 		L=${I18N[${i18n}]}
# 		rm -rf $L/$P
# 	done
# done

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

echo "=> Renaming sources..."
cd $SRC_DIR 
for file in *.jpg *.png; do

	newFile="${file// /_}"

	if [ "$newFile" != "$file" ]; then

		echo "Renaming: $file => $newFile"

		mv "$SRC_DIR/$file" "$SRC_DIR/$newFile"

	fi
done;

echo "=> Converting sources..."
cd $SRC_DIR 
for i18n in ${!I18N[@]}; do
	L=${I18N[${i18n}]}
	for file in *.jpg *.png; do 

		if [ -f $file ]; then

			echo "=> $file"

			convert $file -resize 1600 -quality 90 $OBB_PATH/$L/xxxhdpi/normal/drawable/$file
			EXIT_CODE=$?
			if [[ $EXIT_CODE != 0 ]]; then
			    exit $EXIT_CODE
			fi

			# cp $OBB_PATH/$L/xxxhdpi/normal/drawable/$file $OBB_PATH/$L/xxxhdpi/xlarge/drawable/$file
			# cp $OBB_PATH/$L/xxxhdpi/normal/drawable/$file $OBB_PATH/$L/xxxhdpi/large/drawable/$file
			# cp $OBB_PATH/$L/xxxhdpi/normal/drawable/$file $OBB_PATH/$L/xxhdpi/xlarge/drawable/$file
			# cp $OBB_PATH/$L/xxxhdpi/normal/drawable/$file $OBB_PATH/$L/xxhdpi/large/drawable/$file
			
			# convert $file -resize 1200 -quality 90 $OBB_PATH/$L/xxhdpi/normal/drawable/$file
			# EXIT_CODE=$?
			# if [[ $EXIT_CODE != 0 ]]; then
			#     exit $EXIT_CODE
			# fi

			# cp $OBB_PATH/$L/xxxhdpi/normal/drawable/$file $OBB_PATH/$L/xhdpi/xlarge/drawable/$file
			# cp $OBB_PATH/$L/xxhdpi/normal/drawable/$file $OBB_PATH/$L/xhdpi/large/drawable/$file

			convert $file -resize 800 -quality 90 $OBB_PATH/$L/xhdpi/normal/drawable/$file
			EXIT_CODE=$?
			if [[ $EXIT_CODE != 0 ]]; then
			    exit $EXIT_CODE
			fi

			# cp $OBB_PATH/$L/xxhdpi/normal/drawable/$file $OBB_PATH/$L/hdpi/xlarge/drawable/$file
			# cp $OBB_PATH/$L/xhdpi/normal/drawable/$file $OBB_PATH/$L/hdpi/large/drawable/$file

			# convert $file -resize 600 -quality 90 $OBB_PATH/$L/hdpi/normal/drawable/$file
			# EXIT_CODE=$?
			# if [[ $EXIT_CODE != 0 ]]; then
			#     exit $EXIT_CODE
			# fi

			# cp $OBB_PATH/$L/xhdpi/normal/drawable/$file $OBB_PATH/$L/mdpi/xlarge/drawable/$file
			# cp $OBB_PATH/$L/hdpi/normal/drawable/$file $OBB_PATH/$L/mdpi/large/drawable/$file

			# convert $file -resize 400 -quality 90 $OBB_PATH/$L/mdpi/normal/drawable/$file
			# EXIT_CODE=$?
			# if [[ $EXIT_CODE != 0 ]]; then
			#     exit $EXIT_CODE
			# fi
		fi
	done
done

echo "=> Finished"