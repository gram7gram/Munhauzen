const fs = require('fs');
const path = require('path');
const musicData = require('music-metadata');

const jsons = [
    '../android-en/internal-assets/scenario.json',
    '../android-ru/internal-assets/scenario.json',
]

for (const json of jsons) {
    const items = JSON.parse(fs.readFileSync(json))

    const scenario = items.filter(item => item.chapter === "intro")

    const resources = scenario.map(item => {
        return {
            images: (item.images || []).map(a => a.image),
            audio: (item.audio || []).map(a => a.audio),
        }
    })

    const images = resources.reduce((sum, item) => {
        return sum.concat(item.images)
    }, [])

    const audio = resources.reduce((sum, item) => {
        return sum.concat(item.audio)
    }, [])

    console.log(`[+] File ${json}`)
    console.log(`Images:\n${images}`)
    console.log(`Audio:\n${audio}`)
}

console.log('Completed')

process.exit(0)

