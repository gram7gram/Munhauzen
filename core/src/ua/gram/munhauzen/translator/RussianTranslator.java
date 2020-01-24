package ua.gram.munhauzen.translator;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class RussianTranslator implements Translator {

    final HashMap<String, String> map;

    public RussianTranslator() {
        map = new HashMap<>();

        map.put("loading.footer",
                "Итак, прослушайте мой рассказ и скажите сами, был ли когда-то в мире человек, правдивее барона Мюнхаузена."
                        + "\nНо посмотрите сюда, и вы убедитесь, что я рассказываю вам чистейшую правду: разве вы не видите своими глазами, что теперь на моей куртке осталось всего две пуговицы?"
                        + "\nНет ничего отвратительнее, если путешественник в своих рассказах нет-нет да и соврет для красного словца."
                        + "\nВпрочем, кто имеет деревянный лоб и потому не может отличить лжи и хвастовства от высказанной напрямик правды — такого человека не исправишь."
                        + "\nПоднимем же наши чашки, любезные господа, друзья и товарищи, за эту супружескую чету, за наше здоровье и вместе за утехи общества и за безусловную правдивость каждого рассказчика!"
                        + "\nУ меня имеются к твоим услугам, да и для всех других недоверчивых людей, бесстыдно сомневающихся в правдивости моих рассказов, еще много таких же веских доказательств."
                        + "\nЕсли кто-либо из нашей компаний сомневается в моей правдивости, я скажу только, что мне жалко их нехватки доверия."
                        + "\nДа, я поднял в воздух и себя, и своего коня, и если вы думаете, что это легко, попробуйте сделать это сами."
                        + "\nЛюди могут время усомниться  в  правдивости  рассказов  о моих действительных подвигах, что в высшей степени обидно и  оскорбительно для благородного кавалера, дорожащего своей честью."
                        + "\nИ если кто-нибудь сомневается в правде о том, что я говорю, то он неверен, и я буду сражаться с ним в любое время и в любом месте и с любым оружием, которое ему нравится."
                        + "\nВcё, о чем я говорил раньше  есть правда и истина, и если найдется кто-то настолько смелый, чтобы отрицать это, я готов сразиться с ним любым оружием, которое ему нравится!"
                        + "\nМои дорогие друзья и товарищи, доверяйте тому, что я говорю, и воздавайте честь рассказам о Мюнхгаузене!"
                        + "\nПутешественник имеет право связать и приукрасить свои приключения, как ему заблагорассудится, и очень невежливо отказаться от такого уважения и аплодисментов, которых они заслуживают."
                        + "\nЕсли какой-нибудь джентльмен скажет, что он сомневается в правдивости этой истории, я поставлю ему вазон лимонного сока и заставлю выпить его одним глотком."
                        + "\nВсе мои приключения правда и истина, и кто посмелиться усомниться в их подлиности, тот пусть скажет мне это лицо к лицу."
                        + "\nВы сами видите, что эта чудесная история должна быть правдива, как ни кажется она невероятной; в противном случае, как же могла бы она случиться?"
                        + "\nЯ и сам стал бы в этом сомневаться, если бы не видел зеленых железных червей и собственными глазами не убедился в их разрушительной деятельности."
                        + "\nКаждое мое слово есть чистейшая истина, а если вы не верите мне, отправляйтесь сами на Луну. Там вы увидите, что я ничего не выдумываю и рассказываю вам одну только правду."
                        + "\nЭто потому, что я люблю путешествовать и всегда ищу приключений, а вы сидите дома и ничего не видите, разве что четыре стены своей комнаты.");
        map.put("loading.retry_title", "Загрузка была прервана или отменена");
        map.put("loading.retry_btn", "Повторить");
        map.put("loading.cancel_btn", "Отменить");
        map.put("loading.purchases_btn", "Покупки");
        map.put("loading.menu_btn", "Главное меню");
        map.put("loading.download_btn", "Скачать");
        map.put("loading.title", "Загрузка ресурсов");
        map.put("loading.message", "Сейчас начнеться скачивание необходимых файлов для игры. Пожалуйста, не прерывайте Wi-Fi соединения и дождитесь загрузки.");
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
        map.put("expansion_download.downloading_part", "Скачивание части __NUM__/__TOTAL__ ...");
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
        map.put("logo.title", "творческая студия\n\"Fingertips and Company\"\nпредставляет");
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
        map.put("authors.content3", "Великий поклон Денису Шевченку за великолепную начитку роли Мюнхаузена, а Наталье Басинских – его внучки.");
        map.put("authors.content4", "Огромное спасибо Дмитрию Бондарчуку – главному разработчику – тому, кто с упорством воплотил весь наш замысел в код!");
        map.put("authors.content5", "Приношу большую благодарность Владу Подопрыгоре и Денису Лукьянчуку за тщательное тестирование аудиокниги и за режиссерскую помощь в составлении креативного сценария.");
        map.put("authors.content6", "Благодарность Руслану Хабибулину и его студии");
        map.put("authors.link2", "MelodicVoiceStudio");
        map.put("authors.content7", "за услуги звукообработки.");
        map.put("authors.content8", "Спасибо команде монтажа ролика! Особенно хочу отметить актёров (Дениса и Злату Шевченко), оператора (Антона Борисюка),  режиссера монтажа (Оксану Войтенко), колориста (Кирилла Кужалёва) звукорежиссера и художника-аниматора (Андрея Кулагина).");
        map.put("authors.content9", "Выражаю искреннюю благодарность Илья Кошевой за написание музыки и помощи по всяким мелочам!");
        map.put("authors.content10", "Спасибо организатору!");
        map.put("authors.content11", "Ну и спасибо, конечно же, самому Распе!\nВеликолепно!");
        map.put("gallery_banner.title", "Добро пожаловать в Галерею!!");
        map.put("gallery_banner.btn", "Ура!");
        map.put("gallery_banner.content", "Все картины и элементы дизайна написаны профессиональным художником Андреем Кулагином специально для нашей аудиокниги!");
        map.put("goofs_banner.title", "Добро пожаловать в Неудачи!");
        map.put("goofs_banner.btn", "Ура!");
        map.put("goofs_banner.content", "Тут вы можете прослушать наши самые смешные неудачные дубли и импровизационные шутки как барона Мюнхаузена так и внучки");
        map.put("legal.title", "Дорогие слушатели!");
        map.put("legal.content", "Много рассказов барона Мюнхаузена касаются охоты на животных. Если Вы либо Ваш ребенок восприимчивы к смертям животных, то попросим вас удержаться от данного классического произведения.");
        map.put("legal.btn", "Продолжить");
        map.put("version_screen.title", "Новая версия!");
        map.put("version_screen.content", "Ваша игра не последней версии. Пожалуйста, скачайте новее");
        map.put("version_screen.ignore_btn", "Игнорировать");
        map.put("version_screen.confirm_btn", "Скачать");
        map.put("purchase_screen.title", "Покупки");
        map.put("purchase_screen.unavailable", "Недоступно");
        map.put("purchase_screen.already_purchased", "Куплено!");
        map.put("purchase_screen.download", "Скачать");
        map.put("purchase_screen.free_title", "Демо версия");
        map.put("purchase_screen.free_price", "Бесплатно");
        map.put("purchase_screen.free_description", "1 час аудиокниги!\n8 глав!\n31 илюстраций!");
        map.put("purchase_screen.full_title", "Полная версия");
        map.put("purchase_screen.full_description", "7+ часов аудиокниги!\n69 глав!\n250+ илюстраций!");
        map.put("purchase_screen.part1_title", "Часть 1");
        map.put("purchase_screen.part1_description", "4+ часов аудиокниги!\n34 глав!\n100+ илюстраций!");
        map.put("purchase_screen.part2_title", "Часть 2");
        map.put("purchase_screen.part2_description", "3+ часов аудиокниги!\n27 глав!\n100+ илюстраций!");

    }

    @Override
    public String t(String key) {

        if (map.containsKey(key)) return map.get(key);

        throw new GdxRuntimeException("Translation was not found by key: " + key);
    }
}
