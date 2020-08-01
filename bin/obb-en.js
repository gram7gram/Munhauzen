const fs = require('fs-extra');
const archiver = require('archiver');

const obbDir = "/Users/master/Projects/Munhauzen/obb";
const buildDir = obbDir + "/build";
const audioDir = "/Users/master/Projects/MunhauzenDocs/Elements/AUDIO_FINAL";

const PARTS = 5;

const VERSION = 13;
const LOCALE = 'en';

const DLC = {
  Part_demo: {
    audio: [ "/Part_demo", "/Fails_Eng", "/Sfx_Eng" ],
    images: [ "/Part_demo" ],
    localeAssets: [ "/Part_demo_Eng" ],
    otherAssets: [
      '/authors',
      '/gallery',
      '/menu',
      '/GameScreen',
      '/ui',
      '/saves',
      '/fails',
      '/victory',
    ],
    interactions: [
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
      "/balloons" ]
  },
  Part_1: {
    audio: [ "/Part_demo", "/Fails_Eng", "/Sfx_Eng", "/Part_1" ],
    images: [ "/Part_demo", "/Part_1" ],
    localeAssets: [ "/Part_demo_Eng" ],
    otherAssets: [
      '/authors',
      '/gallery',
      '/menu',
      '/GameScreen',
      '/ui',
      '/saves',
      '/fails',
      '/victory',
    ],
    interactions: [
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
      "/balloons" ]
  },
  Part_2: {
    audio: [ "/Part_demo", "/Fails_Eng", "/Sfx_Eng", "/Part_1", "/Part_2" ],
    images: [ "/Part_demo", "/Part_1", "/Part_2" ],
    localeAssets: [ "/Part_demo_Eng" ],
    otherAssets: [
      '/authors',
      '/gallery',
      '/menu',
      '/GameScreen',
      '/ui',
      '/saves',
      '/fails',
      '/victory',
    ],
    interactions: [
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
      "/balloons" ]
  }
};

const DPIs = [
  'mdpi',
//  'hdpi'
];

const EXPANSIONS = [
//  'Part_demo',
//  'Part_1',
  'Part_2',
];

const getDirs = source => fs.readdirSync(source).filter(dir => dir !== '.DS_Store');

const createDLC = async (DPI, EXP) => {

  const dlcConfig = DLC[EXP];

  const coreImagesDir = obbDir + "/" + EXP + "/" + DPI;

  const VERSION_NAME = LOCALE + "-" + DPI + "-" + EXP;

  console.log(`=> Splitting expansion ${VERSION_NAME} in ${PARTS} parts`);

  let currentPart = 1;

  const getTmpDir = () => buildDir + "/tmp/" + VERSION_NAME + "-part" + currentPart;

  const nextPart = () => {
    currentPart += 1;

    if (currentPart > PARTS) currentPart = 1;
  };

  dlcConfig.images.forEach(dir => {
    const sourceDir = obbDir + dir + "/" + DPI + "/images";

    getDirs(sourceDir).forEach(file => {
      const outputDir = getTmpDir();
      const destDir = outputDir + "/images";

      fs.ensureDir(destDir, () => {
      });

      fs.copySync(sourceDir + "/" + file, destDir + "/" + file);

      nextPart();
    });
  });

  dlcConfig.audio.forEach(dir => {
    const sourceDir = audioDir + dir;

    getDirs(sourceDir).forEach(file => {
      const outputDir = getTmpDir();
      const destDir = outputDir + "/audio";

      fs.ensureDir(destDir, () => {
      });

      fs.copySync(sourceDir + "/" + file, destDir + "/" + file);

      nextPart();
    });
  });

  dlcConfig.interactions.forEach(dir => {
    const sourceDir = obbDir + "/Part_demo/" + DPI + dir;

    getDirs(sourceDir).forEach(file => {
      const outputDir = getTmpDir();
      const destDir = outputDir + dir;

      fs.ensureDir(destDir, () => {
      });

      fs.copySync(sourceDir + "/" + file, destDir + "/" + file);

      nextPart();
    });
  });

  dlcConfig.otherAssets.forEach(dir => {
    const sourceDir = obbDir + "/Part_demo/" + DPI + dir;

    getDirs(sourceDir).forEach(file => {
      const outputDir = getTmpDir();
      const destDir = outputDir + dir;

      fs.ensureDir(destDir, () => {
      });

      fs.copySync(sourceDir + "/" + file, destDir + "/" + file);

      nextPart();
    });
  });

  dlcConfig.localeAssets.forEach(dir => {
    const dpiDir = obbDir + dir + "/" + DPI;

    getDirs(dpiDir).forEach(dir => {

      const sourceDir = dpiDir + "/" + dir;

      getDirs(sourceDir).forEach(file => {
        const outputDir = getTmpDir();
        const destDir = outputDir + "/" + dir;

        fs.ensureDir(destDir, () => {
        });

        fs.copySync(sourceDir + "/" + file, destDir + "/" + file);

        nextPart();
      });
    });
  });

  const completed = [];

  const cleanUp = () => {
    for (let part = 1; part <= PARTS; part++) {
      fs.removeSync(buildDir + "/tmp/" + VERSION_NAME + "-part" + part);
    }
  };

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
        items: completed
      }
    };

    console.log(`=> Completed ${VERSION_NAME}!`);

    fs.writeFileSync(`${buildDir}/${VERSION_NAME}-expansion.json`, JSON.stringify(expansion));

    cleanUp();
  };

  const createArchive = (part = 1) =>
    new Promise((resolve, reject) => {

      try {

        console.log(`=> createArchive ${VERSION_NAME} part ${part}...`);

        const dest = `${buildDir}/${VERSION_NAME}/`;
        const input = `${buildDir}/tmp/${VERSION_NAME}-part${part}`;
        const output = `${dest}/part${part}.zip`;

        fs.ensureDir(dest, () => {
        });

        const archive = archiver('zip', {
          zlib: { level: 5 }
        });

        archive.on('end', function () {

          const size = archive.pointer();

          completed.push({
            size,
            sizeMB: Number((size / 1024 / 1024).toFixed(2)),
            part,
            checksum: "",
            url: "",
          });

          setTimeout(() => {
            resolve();
          }, 100);

        });

        archive.directory(input, "");

        archive.pipe(fs.createWriteStream(output));

        archive.finalize();

      } catch (e) {
        reject(e);
      }
    });

  fs.emptyDirSync(`${buildDir}/${VERSION_NAME}/`);

  for (let part = 1; part <= PARTS; part++) {
    await createArchive(part);
  }

  onComplete();
};

const start = async () => {
  for (const DPI of DPIs) {
    for (const EXP of EXPANSIONS) {
      await createDLC(DPI, EXP);
    }
  }
};

start().catch(e => {
  console.error(e);
});


