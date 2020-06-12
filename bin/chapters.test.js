const fs = require('fs-extra')
const path = require('path')

const enFile = path.resolve(`../android-en/internal-assets/chapters.json`)
const ruFile = path.resolve(`../android-ru/internal-assets/chapters.json`)

const files = [enFile, ruFile]

for (const file of files) {
    const content = fs.readFileSync(file)

    const json = JSON.parse(content)

    const map = {}

    for (const item of json) {
        if (map[item.number] !== undefined) {
            throw new Error(`Chapter number already exists ${item.name}`)
        }

        map[item.number] = item
    }
}

console.log("all good")