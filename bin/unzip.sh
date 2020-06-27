OBB_DIR=~/Projects/Munhauzen/obb/build

function unzipParts() {

  cd $1

  mkdir -p ./test

  unzip part1.zip -d test \
  && unzip part2.zip -d test \
  && unzip part3.zip -d test \
  && unzip part4.zip -d test \
  && unzip part5.zip -d test

}

function unzipLocale() {

  L=$1

  unzipParts $OBB_DIR/$L-mdpi-Part_demo
  test $? -gt 0 && exit 1

  unzipParts $OBB_DIR/$L-mdpi-Part_1
  test $? -gt 0 && exit 1

  unzipParts $OBB_DIR/$L-mdpi-Part_2
  test $? -gt 0 && exit 1

  unzipParts $OBB_DIR/$L-hdpi-Part_demo
  test $? -gt 0 && exit 1

  unzipParts $OBB_DIR/$L-hdpi-Part_1
  test $? -gt 0 && exit 1

  unzipParts $OBB_DIR/$L-hdpi-Part_2
  test $? -gt 0 && exit 1

}



unzipLocale 'ru'

unzipLocale 'en'