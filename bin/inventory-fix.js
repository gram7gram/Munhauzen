const fs = require('fs');
const path = require('path');
const musicData = require('music-metadata');

const jsons = [
    '../android-en/internal-assets/inventory.json',
    '../android-ru/internal-assets/inventory.json',
    '../ios-en/internal-assets/inventory.json',
    '../ios-ru/internal-assets/inventory.json',
]

const start = async () => {

    for (const json of jsons) {

        console.log('=> Started', json)

        const items = JSON.parse(fs.readFileSync(json))

        for (const item of items) {

            if (item.relatedScenario) {
                item.relatedScenario = item.relatedScenario.reduce((list, name) =>
                    list.concat([name, `${name}_proxy`]), []
                )
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

