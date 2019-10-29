package ua.gram.munhauzen.translator;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

public class EnglishTranslator implements Translator {

    final HashMap<String, String> map;

    public EnglishTranslator() {
        map = new HashMap<>();

        map.put("loading.footer",
                "So listen to my story and tell me whether the world has ever seen a person more truthful than Baron Munchausen."
                        + "\nYou can see that there used to be two rows of eleven buttons each on my waistcoat, and that now only three are left."
                        + "\nNothing is more detestable than a traveller who does not adhere strictly to the truth in his stories."
                        + "\nI left the Assembly without another word, unable to endure any longer the company of men who could not distinguish between lying brag and the simple truth."
                        + "\nLet us raise our cups, dear gentlemen, friends and comrades, for this married couple, for our health and together for the comforts of society and for the unconditional veracity of each narrator!"
                        + "\nI have plenty of equally striking proofs at the service of any who are insolent enough to doubt the truth of any of my statements."
                        + "\nSome travellers are apt to advance more than is perhaps strictly true; if any of the company entertain a doubt of my veracity"
                        + "\nThat’s right, I lifted both myself and my horse up into the air, and if you think it's easy, try doing it yourself."
                        + "\nPeople can doubt the truth of the stories about my real heroic deeds, which is highly insulting for a noble gentleman, who values his honor."
                        + "\nAnd if there be any one who doubts the truth of what I say, he is an infidel, and I will fight him at any time and place, and with any weapon he pleases."
                        + "\nAll that I have related before is truth and truth, and if there  be any one so hardy as to deny  it, I am ready to fight him with  any weapon he pleases!"
                        + "\nMy dear friends and companions, have confidence in what I say, and pay  honour to the tales of Munchausen!"
                        + "\nA traveller  has a right to relate and embellish his adventures  as he pleases, and it is very impolite to refuse  that deference and applause they deserve."
                        + "\nIf any gentleman will say he doubts the truth of this story, I will find him a gallon of lemon juice and make him drink it at one draught."
                        + "\nAll my adventures are truth and verity, and who dare to doubt their authenticity, let him tell me it face to face."
                        + "\nYou see for yourselves that this strange tale must be true, however improbable it sounds, or else how could it possibly have happened?"
                        + "\nI also would doubt it, if I hadn't seen the green iron worms and their destructive activity with my own eyes. "
                        + "\nIf the shadow of a doubt can remain on any person’s mind, I say, let him take a voyage to Moon himself, and then he will know I am a traveller of veracity."
                        + "\nThis is because I love traveling and I am always looking for adventures, and you sit at home and see nothing, except the four walls of your room."
        );
        map.put("loading.retry_btn", "Retry");
        map.put("loading.download_btn", "Download");
        map.put("loading.title", "Downloading resources");
        map.put("loading.message", "The game will now start downloading the resources. Please, do not interrupt the Wi-Fi connection and wait for the download.");
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
        map.put("expansion_download.downloading_part", "Downloading part __NUM__/__TOTAL__ ...");
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
        map.put("date_inter.fail_banner_title", "Chose another date?");
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
        map.put("servants_inter.discard_btn", "Discard");
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
                + "\nOur team had put strength and soul into this audiobook! We hope that our it will bring a lot of wonderful and positive emotions to you, and let a smile lit up your face!"
                + "\nListen and enjoy!");
        map.put("greetings_banner.btn", "Start");
        map.put("rate_banner.btn", "Rate");
        map.put("rate_banner.title", "Please, rate out application and leave a positive review");
        map.put("pro_banner.btn", "Rate");
        map.put("pro_banner.title", "Thank you for purchasing the full version!"
                + "\nOur bow!");
        map.put("thank_you_banner.btn", "Feedback");
        map.put("thank_you_banner.title", "Thank you for purchasing the full version!"
                + "\nOur bow!"
                + "\nPlease, rate out application and leave a positive review");
        map.put("logo.title", "creative studio\n\"Fingertips and Company\"\npresents");
        map.put("authors.share_title", "Share the game with your friends!");
        map.put("authors.rate_title", "Please, rate the app!");
        map.put("authors.title", "Creators");
        map.put("authors.content", "");
        map.put("fails.title", "Goofs");
        map.put("gallery.title", "Gallery");
        map.put("gallery.bonus_title", "Exhibit is not completed :(");
        map.put("ending.part1", "The");
        map.put("ending.part2", "End");
        map.put("ending.menu_btn", "menu");
        map.put("share_banner.title", "Tell your friends about audio-book");
        map.put("share_banner.fb", "Facebook");
        map.put("share_banner.tw", "Twitter");
        map.put("share_banner.vk", "Vkontakte");
        map.put("share_banner.in", "Instagram");
        map.put("saves.title", "Saves");
        map.put("menu.authors_btn", "Creators");
        map.put("menu.continue_btn", "Continue");
        map.put("menu.gallery_btn", "Gallery");
        map.put("menu.goofs_btn", "Goofs");
        map.put("menu.saves_btn", "Saves");
        map.put("menu.start_btn", "New story");
        map.put("painting.back_btn", "Back");
        map.put("chapter_inter.part", "Part");
        map.put("chapter_inter.chapter", "Chapter");
        map.put("continue_inter.btn", "Continue");
        map.put("horn_inter.btn", "Continue");

    }

    @Override
    public String t(String key) {

        if (map.containsKey(key)) return map.get(key);

        throw new GdxRuntimeException("Translation was not found by key: " + key);
    }
}
