#!/usr/bin/env bash

OBB_PATH=/Users/master/Projects/Munhauzen/obb

mkdir -p $OBB_PATH/xxxhdpi/images
mkdir -p $OBB_PATH/xxhdpi/images
mkdir -p $OBB_PATH/xhdpi/images
mkdir -p $OBB_PATH/hdpi/images
mkdir -p $OBB_PATH/mdpi/images
mkdir -p $OBB_PATH/ldpi/images

mkdir -p $OBB_PATH/xxxhdpi/servants
mkdir -p $OBB_PATH/xxhdpi/servants
mkdir -p $OBB_PATH/xhdpi/servants
mkdir -p $OBB_PATH/hdpi/servants
mkdir -p $OBB_PATH/mdpi/servants
mkdir -p $OBB_PATH/ldpi/servants

rm $OBB_PATH/xxxhdpi/images/*
rm $OBB_PATH/xxhdpi/images/*
rm $OBB_PATH/xhdpi/images/*
rm $OBB_PATH/hdpi/images/*
rm $OBB_PATH/mdpi/images/*
rm $OBB_PATH/ldpi/images/*

rm $OBB_PATH/xxxhdpi/menu/*
rm $OBB_PATH/xxhdpi/menu/*
rm $OBB_PATH/xhdpi/menu/*
rm $OBB_PATH/hdpi/menu/*
rm $OBB_PATH/mdpi/menu/*
rm $OBB_PATH/ldpi/menu/*

bash dpi.sh

bash dpi-horizontal.sh

bash dpi-servants.sh

bash dpi-raw.sh

node obb.js

md5 1-en-phone-hdpi/*