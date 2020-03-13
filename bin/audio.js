const fs = require('fs');
const path = require('path');
const musicData = require('music-metadata');

const sources = [
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_1',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_2',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_3',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_1_Ru',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_2_Ru',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Part_3_Ru',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Sfx',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Sfx_Ru',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Fails_Eng',
  '/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL/Fails_Ru',
];

for (let i = 0; i < sources.length; i++) {
  const dir = sources[i];

  let suffix = dir.split('/');
  suffix = suffix[suffix.length - 1];

  const rows = [ 'Id_audio,file,duration_audio' ];

  console.log('[+] source ' + dir);

  const files = fs.readdirSync(dir);

  for (let j = 0; j < files.length; j++) {
    const file = files[j];

    const onComplete = (file, duration) => {

      rows.push([ file.split('.')[0], file, duration ].join(','));

      fs.writeFileSync(`./audio-${suffix}.csv`, rows.join("\r\n"));
    };

    musicData.parseFile(path.join(dir, file), { duration: true }).then(metadata => {
      const duration = Number((metadata.format.duration * 1000).toFixed(4));
      onComplete(file, duration);
    }).catch(e => console.error(e));
  }
}
