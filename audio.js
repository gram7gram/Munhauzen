const fs = require('fs');
const path = require('path');
const musicData = require('musicmetadata');
const wavInfo = require('wav-file-info');

//path.resolve(__dirname, `./Elements/Elements/AUDIO_FINAL/Part_1`)
const sources = [
	'/mnt/shared-ext4/Projects/Munhauzen/Elements/Fails_normilized/fails_Munchausen',
	'/mnt/shared-ext4/Projects/Munhauzen/Elements/Fails_normilized/fails_daughter',
	'/mnt/shared-ext4/Projects/Munhauzen/Elements/Fails_normilized/fails_bonus',
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

		const onComplete = (e, metadata) => {

			console.log('=> parsing ' + file);//, JSON.stringify(metadata))

			if (e) throw e

			const duration = Number((metadata.duration * 1000).toFixed(4))

			rows.push([file.split('.')[0], file, duration].join(','))

			//fs.writeFile('./audio-scenario.csv', rows.join("\r\n"), () => {
			fs.writeFile(`./${suffix}.csv`, rows.join("\r\n"), () => {
				
			})
		}

		if (file.indexOf('.wav') !== -1) {

			wavInfo.infoByFilename(dir + '/' + file, onComplete);

		} else {

			const audio = fs.createReadStream(dir + '/' + file)

			musicData(audio, {duration: true}, onComplete)
		}
	}
}