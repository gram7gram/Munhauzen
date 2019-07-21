const fs = require('fs-extra')
const archiver = require('archiver');
const md5 = require('md5');

const obbDir = "/Users/master/Projects/Munhauzen/obb"
const audioDir = "/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL"
const picturesDir = "/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL"
const internalAssetsDir = "/Users/master/Projects/Munhauzen/core/assets"
const apiResourcesDir = "/Users/master/Projects/munhauzen-web/api/src/server/resources"

const PARTS = 10;
const VERSION = 1;
const LOCALE = 'en';
const DEVICE = 'phone';
const DPI = 'hdpi';

const VERSION_NAME = VERSION + "-" + LOCALE + "-" + DEVICE + "-" + DPI

console.log(`=> Splitting expansion ${VERSION_NAME} in ${PARTS} parts`)

console.log('=> Processing audio part 1...')

let currentPart = 1

fs.readdirSync(audioDir + "/Part_1").forEach(file => {

    const dest = "/tmp/part" + currentPart + "/audio"
    const source = audioDir + "/Part_1/"  + file

    fs.ensureDir(dest, () => {})

    fs.copySync(source, dest + "/" + file)

    currentPart += 1

    if (currentPart > PARTS) currentPart = 1

})



console.log('=> Processing audio part 2...')

fs.readdirSync(audioDir + "/Part_2").forEach(file => {

    const dest = "/tmp/part" + currentPart + "/audio"
    const source = audioDir + "/Part_2/"  + file

    fs.ensureDir(dest, () => {})

    fs.copySync(source, dest + "/" + file)

    currentPart += 1

    if (currentPart > PARTS) currentPart = 1
})



console.log('=> Processing interaction assets...')

const interactions = [
    "/timer",
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

fs.readdirSync(picturesDir + "/drawable").forEach(file => {

    const dest = "/tmp/part" + currentPart + "/images"
    const source = picturesDir + "/drawable/"  + file

    fs.ensureDir(dest, () => {})

    fs.copySync(source, dest + "/" + file)

    currentPart += 1

    if (currentPart > PARTS) currentPart = 1
})

fs.readdirSync(picturesDir + "/drawable-horizontal").forEach(file => {

    const dest = "/tmp/part" + currentPart + "/images"
    const source = picturesDir + "/drawable-horizontal/"  + file

    fs.ensureDir(dest, () => {})

    fs.copySync(source, dest + "/" + file)

    currentPart += 1

    if (currentPart > PARTS) currentPart = 1
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

    fs.writeFileSync(apiResourcesDir + "/" + VERSION_NAME + '-expansion.json', JSON.stringify(expansion))

    console.log(JSON.stringify(expansion))

    cleanUp();
}

for (let part = 1; part <= PARTS; part++) {

    const dest = obbDir + `/${VERSION_NAME}/`
    const output = `${dest}/part${part}.zip`

    fs.ensureDir(dest, () => {})

    const archive = archiver('zip', {
      zlib: {level: 9}
    });

    archive.on('end', function () {

        console.log("=> Completed #" + part)

        setTimeout(() => {
            fs.readFile(output, function (err, buf) {

                if (err) throw err

                const checksum = md5(buf)

                completed.push({
                    size: archive.pointer(),
                    part,
                    checksum
                });

                if (completed.length === PARTS) {
                    onComplete()
                }
            });
        }, 100)
    });

    archive.directory(`/tmp/part${part}`, "")

    archive.pipe(fs.createWriteStream(output));

    archive.finalize();
}
