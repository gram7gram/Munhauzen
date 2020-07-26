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

const scenario = function (sheet, prefix) {

  const json = xlsx.utils.sheet_to_json(sheet);

  const headers = [
    "id_option","id_chapter"
    ,"description_option_eng","description_option_ru"
    ,"id_audio","id_picture"
    ,"duration_picture_eng","duration_picture_ru"
    ,"Interaction","action","id_decisions","decision_order"
    ,"inventory_required","inventory_abscent","transition_picture",
    "Purchase"
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

  fs.writeFile(`./${prefix}-proxy.csv`, rows.join("\r\n"), (e) => {
    if (e) throw e
  })
}

const parse = function (file) {

    const workbook = xlsx.readFile(file)

    if (workbook.Sheets['scenario_1']) {
      scenario(workbook.Sheets['scenario_1'], 'scenario_1')
    }

    if (workbook.Sheets['scenario_2']) {
      scenario(workbook.Sheets['scenario_2'], 'scenario_2')
    }

    if (workbook.Sheets['scenario_3']) {
      scenario(workbook.Sheets['scenario_3'], 'scenario_3')
    }
}


parse('/Users/master/Projects/MunhauzenDocs/IOS Task/РАЗРАБОТКА/Scenario Pictures Audio Inventory CHapters Chronicl inte new Demo.xlsx')


