const fs = require('fs-extra')
const path = require('path')

const dpis = ['mdpi', 'hdpi']
const parts = ['Part_1', 'Part_2', 'Part_demo']

for (const dpi of dpis) {
    for (const part of parts) {

        const enFile = path.resolve(`../android-en/internal-assets/${dpi}-${part}-expansion.json`)
        const ruFile = path.resolve(`../android-ru/internal-assets/${dpi}-${part}-expansion.json`)

        const files = [ enFile, ruFile ]

        for (const file of files) {
            const content = fs.readFileSync(file)

            const json = JSON.parse(content)

            for (const item of json.parts.items) {
                console.log(`Testing part ${item.part} ${item.url}`)

                item.url = item.url
                    .replace('https://drive.google.com/file/d/', '')
                    .replace('/view?usp=sharing', '')

//              item.url = ''

                if (item.url.length !== 33) {
                    throw new Error(`Invalid url in part #${item.part} ${dpi}-${part}-expansion.json`)
                }
            }

            fs.writeFileSync(file, JSON.stringify(json, null, 2))
        }

    }
}

console.log("all good")