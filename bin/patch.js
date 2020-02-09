const fs = require('fs-extra')
const archiver = require('archiver');

const obbDir = "/Users/master/Projects/Munhauzen/obb"
const buildDir = obbDir + "/build"

const VERSION = 2;
const LOCALE = 'ru';

const PATCH_DIRS = [
//    '/audio',
//    '/ui',
];

const DPIs = [
  'mdpi',
  'hdpi'
]

DPIs.forEach(DPI => {

    const VERSION_NAME = VERSION + "-" + LOCALE + "-" + DPI

    const tmpDir = buildDir + "/tmp/" + VERSION_NAME + "-patch"
    const internalAssetsDir = obbDir + "/" + LOCALE + "/patch"

    const expansion = JSON.parse(fs.readFileSync(`${buildDir}/${VERSION_NAME}-expansion.json`))

    const patchPart = expansion.parts.count + 1;

    let patch;

    console.log(`=> Patching expansion ${VERSION_NAME} with part ${patchPart}`)

    const cleanUp = () => {
        fs.removeSync(tmpDir)
    }

    const onComplete = () => {
        expansion.parts.items.push(patch)
        expansion.parts.count = expansion.parts.items.length

        const totalSize = expansion.parts.items.reduce((sum, part) => sum + part.size, 0);
        expansion.size = totalSize
        expansion.sizeMB = Number((totalSize / 1024 / 1024).toFixed(2)),

        fs.writeFileSync(`${buildDir}/${VERSION_NAME}-expansion.json`, JSON.stringify(expansion))

        cleanUp();

        console.log(`=> Completed patching ${VERSION_NAME}!`)
    }

    const createArchive = (part = 1) => {

        const dest = buildDir + `/${VERSION_NAME}/`
        const output = `${dest}/part${patchPart}.zip`

        fs.ensureDir(dest, () => {})

        const archive = archiver('zip', {
          zlib: {level: 5}
        });

        archive.on('end', function () {

            const size = archive.pointer()

            patch = {
                isPatch: true,
                size,
                sizeMB: Number((size / 1024 / 1024).toFixed(2)),
                part: patchPart,
                checksum: "",
                path: `/expansions/${VERSION_NAME}/part${patchPart}.zip`
            }

            setTimeout(() => {
                onComplete()
            }, 100)
        });

        archive.directory(tmpDir, "")

        archive.pipe(fs.createWriteStream(output));

        archive.finalize();
    }

    cleanUp();


    PATCH_DIRS.forEach(path => {

        const dir = internalAssetsDir + path
        const dest = tmpDir + path

        fs.ensureDir(dest, () => {})

        fs.readdirSync(dir).forEach(file => {
            const source = dir + "/" + file

            fs.copySync(source, dest + "/" + file)
        })
    })

    createArchive();
})
