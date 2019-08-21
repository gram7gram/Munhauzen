const fs = require('fs-extra')
const archiver = require('archiver');

const obbDir = "/Users/master/Projects/Munhauzen/obb"
const audioDir = "/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL"
const internalAssetsDir = obbDir + "/assets"

const PARTS = 1;
const VERSION = 1;
const LOCALE = 'en';
const DEVICE = 'phone';
const DPI = 'hdpi';

const VERSION_NAME = VERSION + "-" + LOCALE + "-" + DEVICE + "-" + DPI

const audioParts = [
//    "/Part_1",
//    "/Part_2",
//    "/Part_3",
//    "/Sfx",
]

const picturesDir = [
//    obbDir + "/" + LOCALE + "/images"
]

const otherAssets = [
//    '/gallery',
//    '/menu',
//    '/GameScreen',
    '/ui',
//    '/saves',
]

const interactions = [
//    "/timer",
//    "/timer2",
//    "/hare",
//    "/generals",
//    "/cannons",
//    "/wau",
//    "/picture",
//    "/servants",
//    "/lions",
//    "/date",
//    "/horn",
//    "/swamp",
//    "/slap",
//    "/puzzle",
//    "/continue",
//    "/chapter",
//    "/balloons",
]

console.log(`=> Splitting expansion ${VERSION_NAME} in ${PARTS} parts`)

let currentPart = 1

console.log('=> Processing audio...')
audioParts.forEach(dir => {
    fs.readdirSync(audioDir + dir).forEach(file => {

        const dest = "/tmp/part" + currentPart + "/audio"
        const source = audioDir + dir + "/" + file

        fs.ensureDir(dest, () => {})

        fs.copySync(source, dest + "/" + file)

        currentPart += 1

        if (currentPart > PARTS) currentPart = 1

    })
})


console.log('=> Processing interaction assets...')
interactions.forEach(interaction => {
    const dir = internalAssetsDir + interaction

    const dest = "/tmp/part" + currentPart + interaction

    fs.ensureDir(dest, () => {})

    fs.readdirSync(dir).forEach(file => {
        const source = dir + "/" + file

        fs.copySync(source, dest + "/" + file)
    })

    currentPart += 1

    if (currentPart > PARTS) currentPart = 1
})


console.log('=> Processing images...')
picturesDir.forEach(dir => {
    fs.readdirSync(dir).forEach(file => {

        const dest = "/tmp/part" + currentPart + "/images"
        const source = dir + "/" + file

        fs.ensureDir(dest, () => {})

        fs.copySync(source, dest + "/" + file)

        currentPart += 1

        if (currentPart > PARTS) currentPart = 1
    })
})


console.log('=> Processing other assets...')
otherAssets.forEach(dir => {

    fs.readdirSync(internalAssetsDir + dir).forEach(file => {

        const dest = "/tmp/part" + currentPart + dir
        const source = internalAssetsDir + dir + "/" + file

        fs.ensureDir(dest, () => {})

        fs.copySync(source, dest + "/" + file)

        currentPart += 1

        if (currentPart > PARTS) currentPart = 1
    })
})



console.log('=> Creating archive...')

const completed = []

const cleanUp = () => {
    console.log(" => Clean up...")

    for (let part = 1; part <= PARTS; part++) {
        fs.removeSync("/tmp/part" + part)
    }
}

const onComplete = () => {
    const totalSize = completed.reduce((sum, part) => sum + part.size, 0);

    const expansion = {
        version: VERSION,
        locale: LOCALE,
        device: DEVICE,
        dpi: DPI,
        size: totalSize,
        parts: {
            count: completed.length,
            items: completed.map(item => ({
                ...item,
                path: `/expansions/${VERSION_NAME}/part${item.part}.zip`
            }))
        }
    }

    console.log(" => Completed!")

    fs.writeFileSync(`./${VERSION_NAME}-expansion.json`, JSON.stringify(expansion))

    console.log(JSON.stringify(expansion))

    cleanUp();
}

const createArchive = (part = 1) => {

    const dest = obbDir + `/${VERSION_NAME}/`
    const output = `${dest}/part${part}.zip`

    fs.ensureDir(dest, () => {})

    const archive = archiver('zip', {
      zlib: {level: 9}
    });

    archive.on('end', function () {

        console.log("=> Completed #" + part)

        completed.push({
            size: archive.pointer(),
            part,
            checksum: ""
        });

        setTimeout(() => {
            if (completed.length === PARTS) {
                onComplete()
            } else {
                createArchive(++part)
            }
        }, 100)
    });

    archive.directory(`/tmp/part${part}`, "")

    archive.pipe(fs.createWriteStream(output));

    archive.finalize();
}

fs.emptyDirSync(obbDir + `/${VERSION_NAME}/`)

createArchive()
