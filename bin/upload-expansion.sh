#!/usr/bin/env bash

VERSION=$1
if [[ -z "$VERSION" ]]; then
	echo "Upload expansion to production"
	echo "Usage: command [version]"
	exit
fi

OBB_DIR="/Users/master/Projects/Munhauzen/obb"
SERVER="185.227.111.144"

function syncLocal() {

    SYNC_VERSION=$1

    echo "[+] Sync with local server ${SYNC_VERSION}..."

    mkdir -p ~/Projects/munhauzen-web/api/public/expansions/$SYNC_VERSION

    cp $OBB_DIR/build/${SYNC_VERSION}/* /Users/master/Projects/munhauzen-web/api/public/expansions/$SYNC_VERSION

    cp $OBB_DIR/build/${SYNC_VERSION}-expansion.json ~/Projects/munhauzen-web/api/src/server/resources/$SYNC_VERSION-expansion.json
}

function syncRemote() {

    SYNC_VERSION=$1

    echo "[+] Sync with remote server ${SYNC_VERSION}..."

    ssh root@${SERVER} "mkdir -p /var/www/munhauzen-web/api/public/expansions/${SYNC_VERSION} && rm -f /var/www/munhauzen-web/api/public/expansions/${SYNC_VERSION}/*"

    scp $OBB_DIR/build/${SYNC_VERSION}/* \
        root@${SERVER}:/var/www/munhauzen-web/api/public/expansions/${SYNC_VERSION}
}

function deploy() {

    echo "[+] Deploying json expansion..."

    cd ~/Projects/munhauzen-web/

    git add api/src/server/resources \
        && git commit -m "#master deploy expansion" \
        && git push origin master

    bash deploy.sh

}

syncLocal "$VERSION-en-mdpi-Part_demo"
syncLocal "$VERSION-en-mdpi-Part_1"
syncLocal "$VERSION-en-mdpi-Part_2"

syncLocal "$VERSION-en-hdpi-Part_demo"
syncLocal "$VERSION-en-hdpi-Part_1"
syncLocal "$VERSION-en-hdpi-Part_2"

syncLocal "$VERSION-ru-mdpi-Part_demo"
syncLocal "$VERSION-ru-mdpi-Part_1"
syncLocal "$VERSION-ru-mdpi-Part_2"

syncLocal "$VERSION-ru-hdpi-Part_demo"
syncLocal "$VERSION-ru-hdpi-Part_1"
syncLocal "$VERSION-ru-hdpi-Part_2"

syncRemote "$VERSION-en-mdpi-Part_demo"
syncRemote "$VERSION-en-mdpi-Part_1"
syncRemote "$VERSION-en-mdpi-Part_2"

syncRemote "$VERSION-en-hdpi-Part_demo"
syncRemote "$VERSION-en-hdpi-Part_1"
syncRemote "$VERSION-en-hdpi-Part_2"

syncRemote "$VERSION-ru-mdpi-Part_demo"
syncRemote "$VERSION-ru-mdpi-Part_1"
syncRemote "$VERSION-ru-mdpi-Part_2"

syncRemote "$VERSION-ru-hdpi-Part_demo"
syncRemote "$VERSION-ru-hdpi-Part_1"
syncRemote "$VERSION-ru-hdpi-Part_2"

deploy

echo "[+] Completed!"
