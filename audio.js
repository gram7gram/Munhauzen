const fs = require('fs');
const path = require('path');
const musicData = require('mp3-duration');
const wavInfo = require('wav-file-info');

const sources = [
	'/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_1',
	'/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_2',
	'/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_3',
	'/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Sfx',
//	'/Users/master/Projects/MunhauzenDocs/Elements/Fails_normilized/fails_Munchausen',
//	'/Users/master/Projects/MunhauzenDocs/Elements/Fails_normilized/fails_daughter',
//	'/Users/master/Projects/MunhauzenDocs/Elements/Fails_normilized/fails_bonus',
]

for (let i = 0; i < sources.length; i++) {
	const dir = sources[i]

	let suffix = dir.split('/')
	suffix = suffix[suffix.length - 1]

	const rows = ['Id_audio,file,duration_audio']

	console.log('[+] source ' + dir)

	const files = fs.readdirSync(dir)

	for (let j = 0; j < files.length; j++) {
		const file = files[j]

		const onComplete = (file, duration) => {

			rows.push([file.split('.')[0], file, duration].join(','))

			fs.writeFileSync(`./audio-${suffix}.csv`, rows.join("\r\n"))
		}

		if (file.indexOf('.wav') !== -1) {

			wavInfo.infoByFilename(dir + '/' + file, (e, metadata) => {

                if (e) throw e

                const duration = Number((metadata.duration * 1000).toFixed(4))

                onComplete(file, duration)
            });

		} else {

			musicData(dir + '/' + file, (e, duration) => {

                if (e) throw e

                onComplete(file, Number((duration * 1000).toFixed(4)))
			})
		}
	}
}