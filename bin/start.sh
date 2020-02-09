#!/usr/bin/env bash

bash prepare-raw.sh

bash prepare-raw-servants.sh

bash convert-raw.sh

node obb-en.js

node obb-ru.js

md5 build/1-en-hdpi/*

md5 build/1-en-mdpi/*

md5 build/1-ru-hdpi/*

md5 build/1-ru-mdpi/*