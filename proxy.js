const xlsx = require('xlsx');
const fs = require('fs');

String.prototype.escapeSpecialChars = function() {
    return this.replace(/\\n/g, "\\n")
               .replace(/\\'/g, "\\'")
               .replace(/\\"/g, '\\"')
               .replace(/\\&/g, "\\&")
               .replace(/\\r/g, "\\r")
               .replace(/\\t/g, "\\t")
               .replace(/\\b/g, "\\b")
               .replace(/\\f/g, "\\f");
};

const ImportService = (function () {

  function Service() {
  }

  Service.prototype.parse = function (file) {

    const workbook = xlsx.readFile(file)

    const sheet = workbook.Sheets['scenario_1']

    const json = xlsx.utils.sheet_to_json(sheet);

    const headers = Object.keys(json[0])

    const rows = [headers.join(',')]

    json.forEach(item => {

      if (item.id_option) {
        item.id_option = item.id_option + "_proxy"
      }

      item.id_audio  =''
      item.id_picture  =''
      item.duration_picture  =''

      const row = []

      row.push(item.id_option || '')
      row.push(item.id_chapter || '')
      row.push(item.description_option_eng || '')
      row.push(item.id_audio || '')
      row.push(item.id_picture || '')
      row.push(item.duration_picture || '')
      row.push(item.Interaction || '')
      row.push(item.action || '')
      row.push(item.id_decisions || '')

      rows.push(row.map(item => '"' + item.escapeSpecialChars() + '"').join(','))
    })

    fs.writeFile('./scenario_1-proxy.csv', rows.join("\r\n"), () => {
        
    })

  }

  return new Service();
})()

ImportService.parse('/mnt/shared-ext4/Projects/Munhauzen/Elements/IOS Task/Scenario Pictures Audio Inventory CHapters Chronicl inter.xlsx')


