const xlsx = require('xlsx');
const fs = require('fs');

const escapeSpecialChars = function(str) {
    return (str + "").replace(/\\n/g, "\\n")
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

  Service.prototype.scenario = function (sheet, prefix) {

      const json = xlsx.utils.sheet_to_json(sheet);

      const headers = [
        "id_option","id_chapter","description_option_eng","id_audio","id_picture","duration_picture","Interaction","action","id_decisions","decision_order","inventory_required","inventory_abscent","transition_picture"
      ]

      const rows = [headers.join(',')]

      json.forEach(item => {

        if (item.id_option && item.id_option.indexOf('_proxy') === -1) {
          item.id_option += "_proxy"
        }

        const row = []

        item.id_audio = ''
        item.id_picture = ''
        item.duration_picture = ''

        headers.forEach(name => {
          row.push(item[name] || '')
        })

        rows.push(row.map(item => '"' + escapeSpecialChars(item) + '"').join(','))
      })

      fs.writeFile(`./${prefix}-proxy.csv`, rows.join("\r\n"), () => {

      })
  }

  Service.prototype.parse = function (file) {

    const workbook = xlsx.readFile(file)

    if (workbook.Sheets['scenario_1']) {
      this.scenario(workbook.Sheets['scenario_1'], 'scenario_1')
    }

    if (workbook.Sheets['scenario_2']) {
      this.scenario(workbook.Sheets['scenario_2'], 'scenario_2')
    }

  }

  return new Service();
})()

ImportService.parse('/Users/master/Projects/MunhauzenDocs/IOS Task/Scenario Pictures Audio Inventory CHapters Chronicl inter.xlsx')


