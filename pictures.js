const fs = require('fs');
const path = require('path');

const rows = ['Id_picture,file']

const dirs = [
	`./Elements/Elements/PICTURES_FINAL/drawable`,
	`./Elements/Elements/PICTURES_FINAL/drawable-horizontal`
]

dirs.forEach(dir => {

	fs.readdir(path.resolve(__dirname, dir), (e, files) => {

		files.forEach(file => {

			rows.push([file.split('.')[0], file].join(','))

			fs.writeFile('./pictures.csv', rows.join("\r\n"), () => {
				
			})
		})
	})
})