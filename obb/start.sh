#!/usr/bin/env bash

OBB_PATH="/Users/master/Projects/Munhauzen/obb"

bash prepare-raw.sh

bash prepare-raw-servants.sh

bash convert-raw.sh

node obb.js

md5 1-en-phone-hdpi/*