const fs = require('fs-extra')
const archiver = require('archiver');

const obbDir = "/Users/master/Projects/Munhauzen/obb"

const VERSION = 1;
const LOCALE = 'en';

const PATCH_DIRS = [
    '/puzzle'
];

const DPIs = [
  'mdpi',
  'hdpi'
]

DPIs.forEach(DPI => {

    const VERSION_NAME = VERSION + "-" + LOCALE + "-" + DPI

    const tmpDir = "/tmp/" + VERSION_NAME + "-patch"
    const internalAssetsDir = obbDir + "/" + DPI

    const expansion = JSON.parse(fs.readFileSync(`./${VERSION_NAME}-expansion.json`))

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

        fs.writeFileSync(`./${VERSION_NAME}-expansion.json`, JSON.stringify(expansion))

        cleanUp();

        console.log(`=> Completed patching ${VERSION_NAME}!`)
    }

    const createArchive = (part = 1) => {

        const dest = obbDir + `/${VERSION_NAME}/`
        const output = `${dest}/part${patchPart}.zip`

        fs.ensureDir(dest, () => {})

        const archive = archiver('zip', {
          zlib: {level: 5}
        });

        archive.on('end', function () {

            patch = {
                isPatch: true,
                size: archive.pointer(),
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
