#!/usr/bin/env bash

VERSION=$1
if [[ -z "$VERSION" ]]; then
	echo "Upload expansion to production"
	echo "Usage: command [version]"
	exit
fi

SERVER=185.227.111.144

VERSION="${VERSION}-en-phone-hdpi"

echo "[+] Uploading version ${VERSION}..."

echo "[+] Sync with local server..."

mkdir -p ~/Projects/munhauzen-web/api/public/expansions/$VERSION

cp ./${VERSION}/* /Users/master/Projects/munhauzen-web/api/public/expansions/$VERSION

cp ./${VERSION}-expansion.json ~/Projects/munhauzen-web/api/src/server/resources/$VERSION-expansion.json

exit 0;

echo "[+] Sync with remote server..."

ssh root@${SERVER} "mkdir -p /var/www/munhauzen-web/api/public/expansions/${VERSION}"

scp ./${VERSION}/* \
    root@${SERVER}:/var/www/munhauzen-web/api/public/expansions/${VERSION}

echo "[+] Deploying json expansion..."

cd ~/Projects/munhauzen-web/

git add api && git commit -m "#master hotfix" && git push origin master

bash deploy.sh

echo "[+] Completed!"
