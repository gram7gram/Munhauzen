const fs = require('fs');
const path = require('path');
const musicData = require('music-metadata');

const jsons = [
    '../android-en/internal-assets/audio.json',
    '../android-en/internal-assets/audio-fails.json',
]

const sources = [
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_1',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_2',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_demo',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Sfx_Eng',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Fails_Eng',
];

const start = async () => {

    for (const json of jsons) {

        console.log('=> Started', json)

        const audios = JSON.parse(fs.readFileSync(json))

        for (const audio of audios) {

            let name = audio.file.split('/')
            name = name[name.length - 1]

            const match = sources.find(source => fs.existsSync(source + "/" + name))

            if (!match) throw new Error(`No source match for audio ${audio.name}`)

//            const metadata = await musicData.parseFile(match + "/" + name, { duration: true })
//
//            const duration = Number((metadata.format.duration * 1000).toFixed(0));
//
//            audio.duration = duration

            delete audio._id
        }

        fs.writeFileSync(json, JSON.stringify(audios, null, 2));

        console.log('=> Completed', json)
    }
}

start().then(() => {
    console.log('Completed')

    process.exit(0)
}).catch(e => {

    console.log(e)
})

