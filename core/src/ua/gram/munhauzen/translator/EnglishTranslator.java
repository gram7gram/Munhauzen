package ua.gram.munhauzen.translator;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class EnglishTranslator implements Translator {

    final HashMap<String, String> map;

    public EnglishTranslator() {
        map = new HashMap<>();

        map.put("loading.retry_title", "Download was canceled or interrupted");
        map.put("loading.completed_btn", "Continue");
        map.put("loading.retry_btn", "Retry");
        map.put("loading.cancel_btn", "Cancel");
        map.put("loading.purchases_btn", "Purchases");
        map.put("loading.menu_btn", "Main menu");
        map.put("loading.download_btn", "Download");
        map.put("loading.title", "Downloading resources");
        map.put("loading.message", "The game will now start downloading the resources. Please, do not interrupt the Internet connection and wait for the download.");
        map.put("loading.quality_message", "Recommended texture quality");
        map.put("loading.quality_high", "High");
        map.put("loading.quality_medium", "Medium");
        map.put("config_download.started", "Fetching game info...");
        map.put("config_download.failed", "Download has failed");
        map.put("config_download.canceled", "Download was canceled");
        map.put("expansion_download.started", "Fetching expansion info…");
        map.put("expansion_download.not_found", "Expansion was not found");
        map.put("expansion_download.failed", "Unable to fetch expansion");
        map.put("expansion_download.extract_failed", "Unable to extract part");
        map.put("expansion_download.canceled", "Download was canceled");
        map.put("expansion_download.low_memory", "Not enough memory. Please, free some space for the game");
        map.put("expansion_download.completed", "Resources are loaded!");
        map.put("expansion_download.extracting_part", "Extracting part __NUM__/__TOTAL__ ...");
        map.put("expansion_download.downloading_part", "Downloading part __NUM__/__TOTAL__\n__SPEED__ mb/s");
        map.put("expansion_download.downloading_part_failed", "Downloading part __NUM__ has failed");
        map.put("expansion_download.extracting_part_failed", "Extracting part __NUM__ has failed");
        map.put("balloons_inter.retry_btn", "Retry");
        map.put("balloons_inter.continue_btn", "Continue");
        map.put("balloons_inter.title", "Catch them all!");
        map.put("balloons_inter.on_miss_title", "One was missed! Eh!");
        map.put("balloons_inter.completed_title", "There is! Success!");
        map.put("date_inter.confirm_btn", "Confirm");
        map.put("date_inter.banner_yes_btn", "Yes");
        map.put("date_inter.banner_no_btn", "No");
        map.put("date_inter.fail_banner_title", "Choose another date?");
        map.put("date_inter.fail_banner_yes", "Yes");
        map.put("date_inter.fail_banner_no", "No");
        map.put("lions_inter.attack_btn", "Attack!");
        map.put("puzzle_inter.retry_btn", "Again");
        map.put("servants_inter.back_btn", "Back");
        map.put("servants_inter.goto_servants_btn", "Servants");
        map.put("servants_inter.banner_yes_btn", "Yes");
        map.put("servants_inter.banner_no_btn", "No");
        map.put("servants_inter.fire_banner_title", "Fire selected companion?");
        map.put("servants_inter.fire_banner_yes_btn", "Yes");
        map.put("servants_inter.fire_banner_no_btn", "No");
        map.put("servants_inter.hire_banner_title", "Hire him to your retinue?");
        map.put("servants_inter.hire_banner_yes_btn", "Yes");
        map.put("servants_inter.hire_banner_no_btn", "No");
        map.put("servants_inter.discard_btn", "Discard all");
        map.put("servants_inter.complete_banner_title", "Travel to Egypt?");
        map.put("servants_inter.complete_banner_yes", "Yes");
        map.put("servants_inter.complete_banner_no", "No");
        map.put("slap_inter.start_btn", "Slap him!");
        map.put("save_load_banner.yes_btn", "Yes");
        map.put("save_load_banner.no_btn", "No");
        map.put("save_load_banner.title", "The story will start from selected save.\nProceed?");
        map.put("save_options_banner.save_btn", "Save");
        map.put("save_options_banner.load_btn", "Load");
        map.put("save_options_banner.title", "What do you wish?");
        map.put("save_save_banner.yes_btn", "Yes");
        map.put("save_save_banner.no_btn", "No");
        map.put("save_save_banner.title", "Current story progress will be saved here.\nProceed?");
        map.put("demo_banner.buy_btn", "Purchase");
        map.put("demo_banner.title", "Please, purchase the full version of the audio-book!");
        map.put("exit_banner.yes_btn", "Yes");
        map.put("exit_banner.no_btn", "No");
        map.put("exit_banner.title", "Do you want to exit?");
        map.put("greetings_banner.title", "Greetings!"
                + "\nOur team had put strength and soul into this audiobook! We hope that our it will bring a lot of wonderful and positive emotions to you, and let a smile light up your face!"
                + "\nListen and enjoy!");
        map.put("greetings_banner.btn", "Start");
        map.put("rate_banner.btn", "Rate");
        map.put("rate_banner.title", "Please, rate out application and leave a positive review");
        map.put("pro_banner.btn", "Rate app!");
        map.put("pro_banner.purchases_btn", "Purchases");
        map.put("pro_banner.title", "Thank you for purchasing the full version!"
                + "\nOur bow!");
        map.put("thank_you_banner.btn", "Feedback");
        map.put("thank_you_banner.title", "Thank you for purchasing the full version!"
                + "\nOur bow!"
                + "\nPlease, rate out application and leave a positive review");
        map.put("logo.title", "creative studio\n\"FingerTipsAndCompany\"\npresents");
        map.put("authors.share_title", "Share the game with your friends!");
        map.put("authors.rate_title", "Please, rate the app!");
        map.put("authors.title", "Creators");
        map.put("authors.pro_title", "Thank you for the support!");
        map.put("authors.demo_title", "Purchase full version");
        map.put("fails.title", "Goofs");
        map.put("gallery.title", "Gallery");
        map.put("gallery.bonus_title", "Exhibit is not completed :(");
        map.put("ending.part1", "The ");
        map.put("ending.part2", "End");
        map.put("ending.menu_btn", "menu");
        map.put("share_banner.title", "Tell your friends about audio-book");
        map.put("share_banner.fb", "Facebook");
        map.put("share_banner.tw", "Twitter");
        map.put("share_banner.vk", "Vkontakte");
        map.put("share_banner.in", "Instagram");
        map.put("saves.title", "Saves");
        map.put("saves.empty_save_title", "Empty slot");
        map.put("menu.authors_btn", "Creators");
        map.put("menu.continue_btn", "Continue");
        map.put("menu.gallery_btn", "Gallery");
        map.put("menu.goofs_btn", "Goofs");
        map.put("menu.saves_btn", "Saves");
        map.put("menu.start_btn", "New story");
        map.put("menu.chapters_btn", "Chapters");
        map.put("painting.back_btn", "Back");
        map.put("chapter_inter.part", "Part");
        map.put("chapter_inter.chapter", "Chapter");
        map.put("continue_inter.btn", "Continue");
        map.put("horn_inter.btn", "Continue");
        map.put("start_warning_banner.title",
                "Do you want to start a new game?"
                        + "\nUnsaved progress will be lost");
        map.put("start_warning_banner.yes_btn", "Yes");
        map.put("start_warning_banner.no_btn", "No");
        map.put("authors.img_1_title", "Rudolf Erich Raspe");
        map.put("authors.img_2_title", "Andrey Kulagin");
        map.put("authors.img_3_title", "Peter Hayden");
        map.put("authors.img_4_title", "Julia Lawn");
        map.put("authors.img_5_title", "Dmitriy Bondarchuk");
        map.put("authors.img_6_title", "FingerTips");
        map.put("authors.img_7_title", "Ilya Koshevoi");
        map.put("authors.content1", "Dear listeners!\nWe are very grateful to the artist Andrey Kulagin for his wonderful works with the liner, watercolors, hands and the head. His masterpieces, diligent work and gratuitous dedication gave us inspiration throughout the enire process.");
        map.put("authors.content3", "Best regards to Peter Hayden for the magnificent voice-over of the role of Munchausen, and Julia Lawn – of his granddaughter.");
        map.put("authors.content4", "Many thanks to Dmitriy Bondarchuk – the main developer! He is the one who persistently embodied our whole idea into the code!");
        map.put("authors.content5", "We also thank Vlad Podoprygora and Denis Lukyanchuk for extensive testing of the audiobook and for the help in writing the scenario.");
        map.put("authors.content6", "Thanks to Ruslan Khabibulin and his studio");
        map.put("authors.link2", "MelodicVoiceStudio");
        map.put("authors.content7", "for sound editing services.");
        map.put("authors.content8", "Thanks to the movie editing team! Especially I want to mention the actors (Denis and Zlata Shevchenko), the cameraman (Anton Borisyuk), the editing director (Oksana Voitenko), the colorist (Kirill Kuzhalyov), the sound engineer (Leonid Lysenko) and animation artist (Andrey Kulagin).");
        map.put("authors.content9", "And finally, I express my sincere gratitude to Ilya Koshevoi for writing music and helping with some little things!");
        map.put("authors.content10", "Also thanks to the organizer!");
        map.put("authors.content11", "Well, and thanks, of course, to Raspe himself!\nBrilliant!");
        map.put("authors.content12", "Great thanks to Rikki Wright, Alex Hyde-White, Paul Hernandez, Andre Refig and Shurio Pischev for the narration of Munchausen’s guests!");
        map.put("gallery_banner.title", "Welcome to Gallery!");
        map.put("gallery_banner.btn", "Yes!");
        map.put("gallery_banner.content", "All paintings and design elements are written by professional artist Andrey Kulagin specifically for our audiobook!");
        map.put("goofs_banner.title", "Welcome to Goofs!");
        map.put("goofs_banner.btn", "Yes!");
        map.put("goofs_banner.content", "Here you can listen to the funniest goofs and improvisational jokes of Baron Munchausen and granddaughter");
        map.put("legal.title", "Dear listeners!");
        map.put("legal.content", "Many stories of Baron Munchausen are related to animal hunting. If you or your child is susceptible to animal deaths, then we ask you to refrain from this classic.");
        map.put("legal.btn", "Continue");
        map.put("version_screen.title", "New version!");
        map.put("version_screen.content", "Your app is not the latest version. Please, download the newer one");
        map.put("version_screen.ignore_btn", "Ignore");
        map.put("version_screen.confirm_btn", "Download");
        map.put("purchase_screen.restore_btn", "Restore");
        map.put("purchase_screen.processing", "Processing...");
        map.put("purchase_screen.full_discount", "20% SALE");
        map.put("purchase_screen.title", "Purchase");
        map.put("purchase_screen.unavailable", "Not available");
        map.put("purchase_screen.already_purchased", "Owned!");
        map.put("purchase_screen.download", "Download");
        map.put("purchase_screen.free_title", "Demo version");
        map.put("purchase_screen.free_price", "Free");
        map.put("purchase_screen.free_description", "About 1 hour of audiobook!\n10 chapters!\n31 illustrations!");
        map.put("purchase_screen.full_title", "Full version");
        map.put("purchase_screen.full_description", "More then 8 hours of audiobook!\nAll 69 chapters!\nAll 250+ illustrations!");
        map.put("purchase_screen.part1_title", "Part 1");
        map.put("purchase_screen.part1_description", "About 4 hours of audiobook!\n34 chapters!\n100+ illustrations!");
        map.put("purchase_screen.part2_title", "Part 2");
        map.put("purchase_screen.part2_description", "About 4 hours of audiobook!\n35 chapters!\n150+ illustrations!");

        map.put("error_screen.title", "Sorry, an error :(");
        map.put("error_screen.subtitle", "The most common reason for error is a lack of memory, so the contents could not be displayed.\nThe report was sent to man in charge to fix the issue and restore your game experience.\nPlease, give the game another chance.");
        map.put("error_screen.to_menu", "To menu");
        map.put("error_screen.show_error", "Display error");
        map.put("error_screen.reason", "Reason");
        map.put("chapter_screen.title", "Chapters");
        map.put("chapter_banner.title", "Load selected chapter?");
        map.put("chapter_banner.content", "You will not loose current progress");
        map.put("chapter_banner.yes_btn", "Yes");
        map.put("chapter_banner.no_btn", "No");
        map.put("tutorial_banner.title", "Video Tutorial");
        map.put("tutorial_banner.content", "We recomend to watch Video Tutorial");
        map.put("tutorial_banner.ok_btn", "YouTube");
        map.put("tutorial_banner.skip_btn", "Skip");

        map.put("debug_screen.endings", "Endings");
        map.put("debug_screen.open_audio_test", "Open audio test");
        map.put("debug_screen.open_statues", "Unlock statues");
        map.put("debug_screen.open_fails", "Unlock all fails");
        map.put("debug_screen.open_gallery", "Unlock all gallery");
        map.put("debug_screen.purge_history", "Reset history");
        map.put("debug_screen.purge_all", "DELETE EVERYTHING");
        map.put("debug_screen.updates", "Open updates");
        map.put("debug_screen.start", "Start");
        map.put("debug_screen.menu", "Menu");
        map.put("debug_screen.interaction", "Interactions");
        map.put("debug_screen.inventory", "Inventory");
        map.put("debug_screen.scenario", "Scenario");
        map.put("debug_screen.open_scenario", "Unlock all scenario");
        map.put("debug_screen.open_chapters", "Unlock all chapters");

        map.put("adult_gate_screen.incorrect_title", "Incorrect, try again?");
        map.put("adult_gate_screen.incorrect_yes", "Yes");
        map.put("adult_gate_screen.incorrect_no", "No");

        map.put("adult_gate_screen.title", "Adult verification");
        map.put("adult_gate_screen.subtitle", "You are seing this message due to book being published in category \"For children\"");
        map.put("adult_gate_screen.q1a1", "a) 16");
        map.put("adult_gate_screen.q1a2", "b) -5");
        map.put("adult_gate_screen.q1a3", "c) 4");
        map.put("adult_gate_screen.q1a4", "d) 10");

        map.put("adult_gate_screen.q2a1", "a) 25");
        map.put("adult_gate_screen.q2a2", "b) -13");
        map.put("adult_gate_screen.q2a3", "c) 5");
        map.put("adult_gate_screen.q2a4", "d) 9");

        map.put("adult_gate_screen.q3a1", "a) 36");
        map.put("adult_gate_screen.q3a2", "b) 0");
        map.put("adult_gate_screen.q3a3", "c) 6");
        map.put("adult_gate_screen.q3a4", "d) -7");

        map.put("adult_gate_screen.q4a1", "a) 81");
        map.put("adult_gate_screen.q4a2", "b) 7");
        map.put("adult_gate_screen.q4a3", "c) 9");
        map.put("adult_gate_screen.q4a4", "d) -10");

        map.put("menu_achievement.progress", "Progress");
        map.put("menu_achievement_banner.title", "A new trophy is opened in hall!\nCongratulations!");
        map.put("menu_achievement_banner.btn", "Hooray!");

    }

    @Override
    public String t(String key) {

        if (map.containsKey(key)) return map.get(key);

        throw new GdxRuntimeException("Translation was not found by key: " + key);
    }
}
