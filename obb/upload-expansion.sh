#!/usr/bin/env bash

VERSION=$1
if [[ -z "$VERSION" ]]; then
	echo "Upload expansion to production"
	echo "Usage: command [version]"
	exit
fi

SERVER=185.227.111.144

function syncLocal() {

    SYNC_VERSION=$1

    echo "[+] Sync with local server ${SYNC_VERSION}..."

    mkdir -p ~/Projects/munhauzen-web/api/public/expansions/$SYNC_VERSION

    cp ./build/${SYNC_VERSION}/* /Users/master/Projects/munhauzen-web/api/public/expansions/$SYNC_VERSION

    cp ./build/${SYNC_VERSION}-expansion.json ~/Projects/munhauzen-web/api/src/server/resources/$SYNC_VERSION-expansion.json
}

function syncRemote() {

    SYNC_VERSION=$1

    echo "[+] Sync with remote server ${SYNC_VERSION}..."

    ssh root@${SERVER} "mkdir -p /var/www/munhauzen-web/api/public/expansions/${SYNC_VERSION}"

    ssh root@${SERVER} "rm /var/www/munhauzen-web/api/public/expansions/${SYNC_VERSION}/*"

    scp ./build/${SYNC_VERSION}/* \
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

syncLocal "$VERSION-en-mdpi"
syncLocal "$VERSION-en-hdpi"

syncLocal "$VERSION-ru-mdpi"
syncLocal "$VERSION-ru-hdpi"

#syncRemote "$VERSION-en-mdpi"
#syncRemote "$VERSION-en-hdpi"
#
#syncRemote "$VERSION-ru-mdpi"
#syncRemote "$VERSION-ru-hdpi"
#
#deploy

echo "[+] Completed!"
