package ua.gram.munhauzen.translator;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class RussianTranslator implements Translator {

    final HashMap<String, String> map;

    public RussianTranslator() {
        map = new HashMap<>();

        map.put("loading.retry_title", "Загрузка была прервана или отменена");
        map.put("loading.completed_btn", "Продолжить");
        map.put("loading.retry_btn", "Повторить");
        map.put("loading.cancel_btn", "Отменить");
        map.put("loading.purchases_btn", "Покупки");
        map.put("loading.menu_btn", "Главное меню");
        map.put("loading.download_btn", "Скачать");
        map.put("loading.title", "Загрузка ресурсов");
        map.put("loading.message", "Сейчас начнётся скачивание необходимых файлов для игры. Пожалуйста, не прерывайте Интернет соединения и дождитесь загрузки.");
        map.put("loading.quality_message", "Рекомендуемое качество текстур");
        map.put("loading.quality_high", "Высокое");
        map.put("loading.quality_medium", "Среднее");
        map.put("config_download.started", "Получение информации об игре...");
        map.put("config_download.failed", "Скачивание не удалось");
        map.put("config_download.canceled", "Скачивание было отменено");
        map.put("expansion_download.started", "Получение информации о версии файлов…");
        map.put("expansion_download.not_found", "Файлы не были найдены");
        map.put("expansion_download.failed", "Невозможно получить файлы расширения");
        map.put("expansion_download.extract_failed", "Невозможно расспаковать файлы");
        map.put("expansion_download.canceled", "Скачивание было отменено");
        map.put("expansion_download.low_memory", "Нехватает памяти, пожалуйста освободите место");
        map.put("expansion_download.completed", "Все файлы были закачаны!");
        map.put("expansion_download.extracting_part", "Распаковка части __NUM__/__TOTAL__ ...");
        map.put("expansion_download.downloading_part", "Скачивание части __NUM__/__TOTAL__\n__SPEED__ мб/с");
        map.put("expansion_download.downloading_part_failed", "Скачивание части __NUM__ не успешно");
        map.put("expansion_download.extracting_part_failed", "Распаковка части __NUM__ не успешна");
        map.put("balloons_inter.retry_btn", "Повторить");
        map.put("balloons_inter.continue_btn", "Продолжить");
        map.put("balloons_inter.title", "Слови их все!");
        map.put("balloons_inter.on_miss_title", "Ты пропустил один, эх!");
        map.put("balloons_inter.completed_title", "Есть! Успех!");
        map.put("date_inter.confirm_btn", "Подтвердить");
        map.put("date_inter.banner_yes_btn", "Да");
        map.put("date_inter.banner_no_btn", "Нет");
        map.put("date_inter.fail_banner_title", "Выбрать другую дату?");
        map.put("date_inter.fail_banner_yes", "Да");
        map.put("date_inter.fail_banner_no", "Нет");
        map.put("lions_inter.attack_btn", "Атаковать!");
        map.put("puzzle_inter.retry_btn", "Снова");
        map.put("servants_inter.back_btn", "Назад");
        map.put("servants_inter.goto_servants_btn", "Слуги");
        map.put("servants_inter.banner_yes_btn", "Да");
        map.put("servants_inter.banner_no_btn", "Нет");
        map.put("servants_inter.fire_banner_title", "Уволить компаньйона?");
        map.put("servants_inter.fire_banner_yes_btn", "Да");
        map.put("servants_inter.fire_banner_no_btn", "Нет");
        map.put("servants_inter.hire_banner_title", "Нанять его в свою свиту?");
        map.put("servants_inter.hire_banner_yes_btn", "Да");
        map.put("servants_inter.hire_banner_no_btn", "Нет");
        map.put("servants_inter.discard_btn", "Очистить");
        map.put("servants_inter.complete_banner_title", "Отправится в Египет?");
        map.put("servants_inter.complete_banner_yes", "Да");
        map.put("servants_inter.complete_banner_no", "Нет");
        map.put("slap_inter.start_btn", "Врежь ему!");
        map.put("save_load_banner.yes_btn", "Да");
        map.put("save_load_banner.no_btn", "Нет");
        map.put("save_load_banner.title", "Рассказ продолжиться с выбранного сохранения. \n Продолжить?");
        map.put("save_options_banner.save_btn", "Сохранить");
        map.put("save_options_banner.load_btn", "Загрузить");
        map.put("save_options_banner.title", "Что вы хотите сделать?");
        map.put("save_save_banner.yes_btn", "Да");
        map.put("save_save_banner.no_btn", "Нет");
        map.put("save_save_banner.title", "Текущий прогресс будет сохранён тут. \n Продолжить?");
        map.put("demo_banner.buy_btn", "Купить");
        map.put("demo_banner.title", "Пожалуйста, приобретите полную версию аудиокниги!");
        map.put("exit_banner.yes_btn", "Да");
        map.put("exit_banner.no_btn", "Нет");
        map.put("exit_banner.title", "Хотите выйти?");
        map.put("greetings_banner.title", "Приветствуем!"
                + "\nНаша команда вложила в даную аудиокнигу свои силы и душу! Мы надеемся, что  она принесет вам много чудесных и положительных эмоций, и пусть улыбка озарится на вашем лице!"
                + "\nСлушайте и наслаждайтесь!");
        map.put("greetings_banner.btn", "Начать");
        map.put("rate_banner.btn", "Оценить");
        map.put("rate_banner.title", "Пожалуйста, оцените наше приложение и оставьте отзыв");
        map.put("pro_banner.btn", "Оценить");
        map.put("pro_banner.purchases_btn", "Мои покупки");
        map.put("pro_banner.title", "Спасибо за покупку полной версии аудиокниги!"
                + "\nПоклон!");
        map.put("thank_you_banner.btn", "Оставить отзыв");
        map.put("thank_you_banner.title", "Спасибо за покупку полной версии аудиокниги!"
                + "\nПоклон!"
                + "\nПожалуйста, оцените наше приложение и оставьте отзыв");
        map.put("logo.title", "творческая студия\n\"FingerTipsAndCompany\"\nпредставляет");
        map.put("authors.share_title", "Делитесь игрой с друзьями!");
        map.put("authors.rate_title", "Пожалуйста, оцените приложение!");
        map.put("authors.title", "Авторы");
        map.put("authors.pro_title", "Спасибо Вам за поддержку!");
        map.put("authors.demo_title", "Купите полную версию");
        map.put("fails.title", "Неудачи");
        map.put("gallery.title", "Галерея");
        map.put("gallery.bonus_title", "Картина не окончена :(");
        map.put("ending.part1", "Ко");
        map.put("ending.part2", "нец");
        map.put("ending.menu_btn", "меню");
        map.put("share_banner.title", "Расскажи друзьям о аудиокниге");
        map.put("share_banner.fb", "Фейсбук");
        map.put("share_banner.tw", "Твиттер");
        map.put("share_banner.vk", "Вконтакте");
        map.put("share_banner.in", "Инстаграм");
        map.put("saves.title", "Сохранения");
        map.put("saves.empty_save_title", "Пустой слот");
        map.put("menu.authors_btn", "Авторы");
        map.put("menu.continue_btn", "Продолжить");
        map.put("menu.gallery_btn", "Галерея");
        map.put("menu.goofs_btn", "Неудачи");
        map.put("menu.saves_btn", "Сохранения");
        map.put("menu.start_btn", "Начать");
        map.put("menu.chapters_btn", "Главы");
        map.put("painting.back_btn", "Назад");
        map.put("chapter_inter.part", "Часть");
        map.put("chapter_inter.chapter", "Глава");
        map.put("continue_inter.btn", "Продолжить");
        map.put("horn_inter.btn", "Продолжить");
        map.put("start_warning_banner.title",
                "Вы хотите начать слушать сначала?"
                        + "\nНесохраннёный прогресс будет потерян");
        map.put("start_warning_banner.yes_btn", "Да");
        map.put("start_warning_banner.no_btn", "Нет");
        map.put("authors.img_1_title", "Рудольф Эрих Распе");
        map.put("authors.img_2_title", "Андрей Кулагин");
        map.put("authors.img_3_title", "Денис Шевченко");
        map.put("authors.img_4_title", "Наталья Басинских");
        map.put("authors.img_5_title", "Дима Бондарчук");
        map.put("authors.img_6_title", "FingerTips");
        map.put("authors.img_7_title", "Илья Кошевой");
        map.put("authors.content1", "Дорогие слушатели!\nПриносим большую благодарность художнику Андрею Кулагину за его прекрасные и усердные работы лайнером, акварелью, рукой и головой. Его шедевры и безвозмездная увлечённость дарили нам вдохновение на протяжении всей работы.");
        map.put("authors.content3", "Великий поклон Денису Шевченку за великолепную начитку роли Мюнхгаузена, а Наталье Басинских – его внучки.");
        map.put("authors.content4", "Огромное спасибо Дмитрию Бондарчуку – главному разработчику – тому, кто с упорством воплотил весь наш замысел в код!");
        map.put("authors.content5", "Приношу большую благодарность Владу Подопрыгоре и Денису Лукьянчуку за тщательное тестирование аудиокниги и за режиссерскую помощь в составлении креативного сценария.");
        map.put("authors.content6", "Благодарность Руслану Хабибулину и его студии");
        map.put("authors.link2", "MelodicVoiceStudio");
        map.put("authors.content7", "за услуги звукообработки.");
        map.put("authors.content8", "Спасибо команде монтажа ролика! Особенно хочу отметить актёров (Дениса и Злату Шевченко), оператора (Антона Борисюка),  режиссера монтажа (Оксану Войтенко), колориста (Кирилла Кужалёва), звукорежиссера (Леонида Лысенка) и художника-аниматора (Андрея Кулагина).");
        map.put("authors.content9", "Выражаю искреннюю благодарность Илье Кошевому за написание музыки и помощи по всяким мелочам!");
        map.put("authors.content10", "Спасибо организатору!");
        map.put("authors.content11", "Ну и спасибо, конечно же, самому Распе!\nВеликолепно!");
        map.put("authors.content12", "Спасибо большое Пыщеву Олександру за озвучивание второстепенного персонажа, а Сергею Голумбовскому – племянника барона!");
        map.put("gallery_banner.title", "Добро пожаловать в Галерею!!");
        map.put("gallery_banner.btn", "Ура!");
        map.put("gallery_banner.content", "Все картины и элементы дизайна написаны профессиональным художником Андреем Кулагином специально для нашей аудиокниги!");
        map.put("goofs_banner.title", "Добро пожаловать в Неудачи!");
        map.put("goofs_banner.btn", "Ура!");
        map.put("goofs_banner.content", "Тут вы можете прослушать наши самые смешные неудачные дубли и импровизационные шутки как барона Мюнхгаузена так и внучки");
        map.put("legal.title", "Дорогие слушатели!");
        map.put("legal.content", "Много рассказов барона Мюнхгаузена касаются охоты на животных. Если Вы либо Ваш ребенок восприимчивы к смертям животных, то попросим вас удержаться от данного классического произведения.");
        map.put("legal.btn", "Продолжить");
        map.put("version_screen.title", "Новая версия!");
        map.put("version_screen.content", "Ваша игра не последней версии. Пожалуйста, скачайте новее");
        map.put("version_screen.ignore_btn", "Игнорировать");
        map.put("version_screen.confirm_btn", "Скачать");
        map.put("purchase_screen.restore_btn", "Вернуть");
        map.put("purchase_screen.processing", "Обработка...");
        map.put("purchase_screen.full_discount", "20% СКИДКА");
        map.put("purchase_screen.title", "Покупки");
        map.put("purchase_screen.unavailable", "Недоступно");
        map.put("purchase_screen.already_purchased", "Куплено!");
        map.put("purchase_screen.download", "Скачать");
        map.put("purchase_screen.free_title", "Демо версия");
        map.put("purchase_screen.free_price", "Бесплатно");
        map.put("purchase_screen.free_description", "Более 1 часа аудиокниги!\n10 разделов!\n31 иллюстрация!");
        map.put("purchase_screen.full_title", "Полная версия");
        map.put("purchase_screen.full_description", "Более 8 часов аудиокниги!\nВсе 69 разделов!\nВсе 250+ иллюстраций!");
        map.put("purchase_screen.part1_title", "Часть 1");
        map.put("purchase_screen.part1_description", "Около 4 часов аудиокниги!\n34 раздела!\n100+ иллюстраций!");
        map.put("purchase_screen.part2_title", "Часть 2");
        map.put("purchase_screen.part2_description", "Около 4 часов аудиокниги!\n35 раздела!\n150+ иллюстраций!");

        map.put("error_screen.title", "Неожиданная ошибка :(");
        map.put("error_screen.subtitle", "Самая вероятная причина ошибки - нехватка памяти из-за чего содержимое не было отображено.\nОтчёт был отправлен ответственному человеку для исправления ошибки и восстановления Вашего наслаждения игрой.\nПожалуйста, дайте игре еще один шанс.");
        map.put("error_screen.to_menu", "В меню");
        map.put("error_screen.show_error", "Показать ошибку");
        map.put("error_screen.reason", "Причина");
        map.put("chapter_screen.title", "Главы");
        map.put("chapter_banner.title", "Загрузить выбранную главу?");
        map.put("chapter_banner.content", "Вы не потеряете текущий прогресс");
        map.put("chapter_banner.yes_btn", "Да");
        map.put("chapter_banner.no_btn", "Нет");
        map.put("tutorial_banner.title", "Видеообучение");
        map.put("tutorial_banner.content", "Мы рекомендуем посмотреть Видеообучение");
        map.put("tutorial_banner.ok_btn", "YouTube");
        map.put("tutorial_banner.skip_btn", "Пропустить");

        map.put("debug_screen.endings", "Концовки");
        map.put("debug_screen.open_audio_test", "Открыть тест аудио");
        map.put("debug_screen.open_statues", "Открыть статуетки");
        map.put("debug_screen.open_fails", "Открыть все фейлы");
        map.put("debug_screen.open_gallery", "Открыть всю галерею");
        map.put("debug_screen.purge_history", "Очистить историю");
        map.put("debug_screen.purge_all", "Удалить ВСЕ");
        map.put("debug_screen.updates", "Скачать обновления");
        map.put("debug_screen.start", "Начать");
        map.put("debug_screen.menu", "Меню");
        map.put("debug_screen.inventory", "Инвентарь");
        map.put("debug_screen.scenario", "Сценарий");
        map.put("debug_screen.interaction", "Фишки");
        map.put("debug_screen.open_scenario", "Открыть все сценарии");
        map.put("debug_screen.open_chapters", "Открыть все главы");

        map.put("adult_gate_screen.incorrect_title", "Не правильно, повторить?");
        map.put("adult_gate_screen.incorrect_yes", "Да");
        map.put("adult_gate_screen.incorrect_no", "Нет");

        map.put("adult_gate_screen.title", "Родительский контроль");
        map.put("adult_gate_screen.subtitle", "Вы видите это сообщение так как книга опубликована в разделе \"Для детей\"");
        map.put("adult_gate_screen.q1a1", "а) 16");
        map.put("adult_gate_screen.q1a2", "б) -5");
        map.put("adult_gate_screen.q1a3", "в) 4");
        map.put("adult_gate_screen.q1a4", "г) 10");

        map.put("adult_gate_screen.q2a1", "а) 25");
        map.put("adult_gate_screen.q2a2", "б) -13");
        map.put("adult_gate_screen.q2a3", "в) 5");
        map.put("adult_gate_screen.q2a4", "г) 9");

        map.put("adult_gate_screen.q3a1", "а) 36");
        map.put("adult_gate_screen.q3a2", "б) 0");
        map.put("adult_gate_screen.q3a3", "в) 6");
        map.put("adult_gate_screen.q3a4", "г) -7");

        map.put("adult_gate_screen.q4a1", "а) 81");
        map.put("adult_gate_screen.q4a2", "б) 7");
        map.put("adult_gate_screen.q4a3", "в) 9");
        map.put("adult_gate_screen.q4a4", "г) -10");

        map.put("menu_achievement.progress", "Прогрес");
        map.put("menu_achievement_banner.title", "Новый трофей открыт в гостиной!\nПоздравляем");
        map.put("menu_achievement_banner.btn", "Ура! Ура!");

    }

    @Override
    public String t(String key) {

        if (map.containsKey(key)) return map.get(key);

        throw new GdxRuntimeException("Translation was not found by key: " + key);
    }
}
