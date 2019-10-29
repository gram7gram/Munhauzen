const fs = require('fs-extra')
const archiver = require('archiver');

const obbDir = "/Users/master/Projects/Munhauzen/obb"
const buildDir = obbDir + "/build"
const audioDir = "/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL"

const PARTS = 10;

const VERSION = 1;
const LOCALE = 'en';

const DPIs = [
  'mdpi',
  'hdpi'
]

DPIs.forEach(DPI => {

    const internalAssetsDir = obbDir + "/" + LOCALE + "/" + DPI

    const VERSION_NAME = VERSION + "-" + LOCALE + "-" + DPI

    const audioParts = [
        "/Part_1",
        "/Part_2",
        "/Part_3",
        "/Sfx_Eng",
        "/Fails_Eng",
    ]

    const picturesDir = [
        internalAssetsDir + "/images"
    ]

    const otherAssets = [
        '/gallery',
        '/menu',
        '/GameScreen',
        '/ui',
        '/saves',
        '/fails',
        '/victory',
    ]

    const interactions = [
        "/timer",
        "/timer2",
        "/hare",
        "/generals",
        "/cannons",
        "/wau",
        "/picture",
        "/servants",
        "/lions",
        "/date",
        "/horn",
        "/swamp",
        "/slap",
        "/puzzle",
        "/continue",
        "/chapter",
        "/balloons",
    ]

    console.log(`=> Splitting expansion ${VERSION_NAME} in ${PARTS} parts`)

    let currentPart = 1

    audioParts.forEach(dir => {
        fs.readdirSync(audioDir + dir).forEach(file => {

            const dest = buildDir + "/tmp/" + VERSION_NAME + "-part" + currentPart + "/audio"
            const source = audioDir + dir + "/" + file

            fs.ensureDir(dest, () => {})

            fs.copySync(source, dest + "/" + file)

            currentPart += 1

            if (currentPart > PARTS) currentPart = 1

        })
    })


    interactions.forEach(interaction => {
        const dir = internalAssetsDir + interaction

        const dest = buildDir + "/tmp/" + VERSION_NAME + "-part" + currentPart + interaction

        fs.ensureDir(dest, () => {})

        fs.readdirSync(dir).forEach(file => {
            const source = dir + "/" + file

            fs.copySync(source, dest + "/" + file)
        })

        currentPart += 1

        if (currentPart > PARTS) currentPart = 1
    })


    picturesDir.forEach(dir => {
        fs.readdirSync(dir).forEach(file => {

            const dest = buildDir + "/tmp/" + VERSION_NAME + "-part" + currentPart + "/images"
            const source = dir + "/" + file

            fs.ensureDir(dest, () => {})

            fs.copySync(source, dest + "/" + file)

            currentPart += 1

            if (currentPart > PARTS) currentPart = 1
        })
    })


    otherAssets.forEach(dir => {

        fs.readdirSync(internalAssetsDir + dir).forEach(file => {

            const dest = buildDir + "/tmp/" + VERSION_NAME + "-part" + currentPart + dir
            const source = internalAssetsDir + dir + "/" + file

            fs.ensureDir(dest, () => {})

            fs.copySync(source, dest + "/" + file)

            currentPart += 1

            if (currentPart > PARTS) currentPart = 1
        })
    })



    const completed = []

    const cleanUp = () => {
        for (let part = 1; part <= PARTS; part++) {
            fs.removeSync(buildDir + "/tmp/" + VERSION_NAME + "-part" + part)
        }
    }

    const onComplete = () => {
        const totalSize = completed.reduce((sum, part) => sum + part.size, 0);

        const expansion = {
            version: VERSION,
            locale: LOCALE,
            dpi: DPI,
            size: totalSize,
            sizeMB: Number((totalSize / 1024 / 1024).toFixed(2)),
            parts: {
                count: completed.length,
                items: completed.map(item => ({
                    ...item,
                    path: `/expansions/${VERSION_NAME}/part${item.part}.zip`
                }))
            }
        }

        console.log(`=> Completed ${VERSION_NAME}!`)

        fs.writeFileSync(`${buildDir}/${VERSION_NAME}-expansion.json`, JSON.stringify(expansion))

        cleanUp();
    }

    const createArchive = (part = 1) => {

        const dest = buildDir + `/${VERSION_NAME}/`
        const output = `${dest}/part${part}.zip`
        const input = buildDir + "/tmp/" + VERSION_NAME + "-part" + part;

        fs.ensureDir(dest, () => {})

        const archive = archiver('zip', {
          zlib: {level: 5}
        });

        archive.on('end', function () {

            const size = archive.pointer()

            completed.push({
                size,
                sizeMB: Number((size / 1024 / 1024).toFixed(2)),
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

        archive.directory(input, "")

        archive.pipe(fs.createWriteStream(output));

        archive.finalize();
    }

    fs.emptyDirSync(buildDir + `/${VERSION_NAME}/`)

    createArchive()
})
