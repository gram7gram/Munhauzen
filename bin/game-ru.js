const fs = require('fs');
const path = require('path');
const musicData = require('music-metadata');

const jsons = [
    '../android-ru/internal-assets/chapters.json',
    '../android-ru/internal-assets/scenario.json',
    '../android-ru/internal-assets/images.json',
    '../android-ru/internal-assets/inventory.json',
    '../android-ru/internal-assets/audio.json',
    '../android-ru/internal-assets/audio-fails.json',
    '../ios-ru/internal-assets/chapters.json',
    '../ios-ru/internal-assets/scenario.json',
    '../ios-ru/internal-assets/images.json',
    '../ios-ru/internal-assets/inventory.json',
    '../ios-ru/internal-assets/audio.json',
    '../ios-ru/internal-assets/audio-fails.json',
]

const start = async () => {

    for (const json of jsons) {

        console.log('=> Started', json)

        const items = JSON.parse(fs.readFileSync(json))

        for (const item of items) {

            delete item._id

            if (item.statueImage) {
                item.statueImage = item.statueImage.replace('gallery/', 'statues/')
            }

            if (item.audio) {
                    for (const i of item.audio) {
                        delete i._id
                    }
            }

            if (item.images) {
                    for (const i of item.images) {
                        delete i._id
                    }
            }

            if (item.decisions) {
                    for (const i of item.decisions) {
                        delete i._id
                    }
            }
        }

        fs.writeFileSync(json, JSON.stringify(items, null, 2));

        console.log('=> Completed', json)
    }
}

start().then(() => {
    console.log('Completed')

    process.exit(0)
}).catch(e => {

    console.log(e)
})

