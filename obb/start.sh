#!/usr/bin/env bash

bash prepare-raw.sh

bash prepare-raw-servants.sh

bash convert-raw.sh

node obb.js

md5 1-en-hdpi/*

md5 1-en-mdpi/*