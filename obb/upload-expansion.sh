#!/usr/bin/env bash

VERSION=$1
if [[ -z "$VERSION" ]]; then
	echo "Upload expansion to production"
	echo "Usage: command [version]"
	exit
fi

SERVER=185.227.111.144

function syncLocal() {

    VERSION=$1

    echo "[+] Sync with local server ${VERSION}..."

    mkdir -p ~/Projects/munhauzen-web/api/public/expansions/$VERSION

    cp ./${VERSION}/* /Users/master/Projects/munhauzen-web/api/public/expansions/$VERSION

    cp ./${VERSION}-expansion.json ~/Projects/munhauzen-web/api/src/server/resources/$VERSION-expansion.json
}

function syncRemote() {

    VERSION=$1

    echo "[+] Sync with remote server ${VERSION}..."

    ssh root@${SERVER} "mkdir -p /var/www/munhauzen-web/api/public/expansions/${VERSION}"

    scp ./${VERSION}/* \
        root@${SERVER}:/var/www/munhauzen-web/api/public/expansions/${VERSION}
}

function deploy() {

    echo "[+] Deploying json expansion..."

    cd ~/Projects/munhauzen-web/

    git add api && git commit -m "#master hotfix" && git push origin master

    bash deploy.sh

}

#syncLocal "$1-en-ldpi"

#syncLocal "$1-en-mdpi"

syncLocal "$1-en-hdpi"

#syncRemote "$1-en-ldpi"

#syncRemote "$1-en-mdpi"

#syncRemote "$1-en-hdpi"

#deploy

echo "[+] Completed!"
