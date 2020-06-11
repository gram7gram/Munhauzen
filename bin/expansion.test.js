const fs = require('fs-extra')
const path = require('path')

const dpis = ['mdpi', 'hdpi']
const parts = ['Part_1', 'Part_2', 'Part_demo']

for (const dpi of dpis) {
    for (const part of parts) {

        const enFile = path.resolve(`../android-en/internal-assets/${dpi}-${part}-expansion.json`)
        const ruFile = path.resolve(`../android-ru/internal-assets/${dpi}-${part}-expansion.json`)

        const files = [enFile, ruFile]

        for (const file of files) {
            const content = fs.readFileSync(file)

            const json = JSON.parse(content)

            for (const item of json.parts.items) {
                if (item.url.length !== 33) {
                    throw new Error(`Invalid url in part #${item.part} ${dpi}-${part}-expansion.json`)
                }
            }
        }

    }
}

console.log("all good")