const fs = require('fs-extra')
const archiver = require('archiver');
const md5 = require('md5');

const obbDir = "/Users/master/Projects/Munhauzen/obb"
const audioDir = "/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL"
const picturesDir = "/Users/master/Projects/MunhauzenDocs/Elements/PICTURES_FINAL"

const PARTS = 10;
const VERSION = 1;
const LOCALE = 'en';
const DEVICE = 'phone';
const DPI = 'hdpi';

console.log(`=> Splitting expansion in ${PARTS} parts`)

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



console.log('=> Processing images...')

currentPart = 1

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
                path: `/expansions/${VERSION}-${LOCALE}-${DEVICE}-${DPI}/part${item.part}.zip`
            }))
        }
    }

    console.log(" => Completed!")
    console.log(JSON.stringify(expansion))

    cleanUp();
}

for (let part = 1; part <= PARTS; part++) {

    const dest = obbDir + `/${VERSION}-${LOCALE}-${DEVICE}-${DPI}/`
    const output = `${dest}/part${part}.zip`

    fs.ensureDir(dest, () => {})

    const archive = archiver('zip', {
      zlib: {level: 9}
    });

    archive.on('end', function () {

        console.log(" => Completed #" + part)

        fs.readFile(output, function (err, buf) {

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
    });

    archive.directory(`/tmp/part${part}/audio`, 'audio')
    archive.directory(`/tmp/part${part}/images`, 'images')

    archive.pipe(fs.createWriteStream(output));

    archive.finalize();
}
