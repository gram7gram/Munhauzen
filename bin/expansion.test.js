const fs = require('fs-extra')
const path = require('path')

const dpis = [
'mdpi',
'hdpi'
]
const parts = [
'Part_demo',
'Part_1',
'Part_2',
]

const testAssetsExist = async (file, part, dpi) => {

    const obbDir = path.resolve(__dirname, '../obb/build')

    const content = fs.readFileSync(file)

    const json = JSON.parse(content)

    const locale = json.locale

    const version = locale + '-' + dpi + '-' + part

    const buildDir = `${obbDir}/${version}/test`

    console.log("\n\n=> Testing version", version)

    let scenarios = JSON.parse(fs.readFileSync(path.resolve(`../android-${locale}/internal-assets/scenario.json`)))
    const images = JSON.parse(fs.readFileSync(path.resolve(`../android-${locale}/internal-assets/images.json`)))
    const audio = JSON.parse(fs.readFileSync(path.resolve(`../android-${locale}/internal-assets/audio.json`)))

    if (part === 'Part_demo') {
        scenarios = scenarios.filter(item => item.expansion === 'Part_demo')
    } else if (part === 'Part_1') {
        scenarios = scenarios.filter(item => ['Part_demo', 'Part_1'].indexOf(item.expansion) !== -1)
    }

    let hasErrors = false
    const withImages = scenarios.filter(item => item.images && item.images.length > 0)
    for (const scenarioWithImage of withImages) {

        scenarioWithImage.images = scenarioWithImage.images.filter(item => item.image !== 'Last')

        for (const scenarioImage of scenarioWithImage.images) {

            const image = images.find(item => item.name === scenarioImage.image)
            if (!image) {
                console.error(`Not found ${scenarioImage.image}`)
                hasErrors = true
            } else {
                const exists = fs.existsSync(`${buildDir}/${image.file}`)
                if (!exists) {
                    console.error('Missing: ', image.file)
                    hasErrors = true
                }
            }

        }
    }

    const withAudio = scenarios.filter(item => item.audio && item.audio.length > 0)
    for (const scenariowithAudio of withAudio) {

        scenariowithAudio.audio = scenariowithAudio.audio.filter(item => item.image !== 'Last')

        for (const scenarioAudio of scenariowithAudio.audio) {

            const item = audio.find(item => item.name === scenarioAudio.audio)
            if (!item) {
                console.error(`Not found ${item.audio}`)
                hasErrors = true
            } else {
                const exists = fs.existsSync(`${buildDir}/${item.file}`)
                if (!exists) {
                    console.error('Missing: ', item.file)
                    hasErrors = true
                }
            }

        }
    }

    if (!hasErrors) {
        console.log('all good')
    }
}

const testLinks = file => {
    const content = fs.readFileSync(file)

    const json = JSON.parse(content)

    json.version = 12

    if (!json.parts || !json.parts.items || json.parts.items.length < 5) {
        throw new Error(`Invalid part count in file: ${file}`)
    }

    json.parts.count = json.parts.items.length

    for (const item of json.parts.items) {

        item.url = item.url
            .replace('https://drive.google.com/file/d/', '')
            .replace('/view?usp=sharing', '')

//              item.url = ''

        if (item.url.length !== 33) {
            throw new Error(`Invalid url in part #${item.part} ${file}: ${item.url.length}`)
        }
    }

    fs.writeFileSync(file, JSON.stringify(json, null, 2))
}

for (const dpi of dpis) {
    for (const part of parts) {

        const en1File = path.resolve(`../android-en/internal-assets/${dpi}-${part}-expansion.json`)
        const ru1File = path.resolve(`../android-ru/internal-assets/${dpi}-${part}-expansion.json`)
        const en2File = path.resolve(`../ios-en/internal-assets/${dpi}-${part}-expansion.json`)
        const ru2File = path.resolve(`../ios-ru/internal-assets/${dpi}-${part}-expansion.json`)

        testAssetsExist(en1File, part, dpi)
        testAssetsExist(ru1File, part, dpi)
        testAssetsExist(en2File, part, dpi)
        testAssetsExist(ru2File, part, dpi)

        testLinks(ru1File)
        testLinks(ru2File)
        testLinks(en1File)
        testLinks(en2File)

    }
}