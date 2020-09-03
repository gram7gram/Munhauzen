package ua.gram.munhauzen.service;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.utils.MD5;

public class ReferralService {

    public static final String REFERRAL_TYPE_1 = "invited_3";
    public static final String REFERRAL_TYPE_2 = "invited_7";

    public static final int REFERRAL_REWARD_1 = 3;
    public static final int REFERRAL_REWARD_2 = 7;
    public static final int MAX_REFERRAL_COUNT = 7;

    final MunhauzenGame game;

    public ReferralService(MunhauzenGame game) {
        this.game = game;
    }

    public String getPersonalReferralLink() {

        // TODO Generate link

        return "https://" + MD5.get() + "/" + MD5.get();
    }

    public void copyReferralLink() {

        String link = getPersonalReferralLink();

        // TODO Copy link to device clipboard

    }

    public void setReferralCount(int count) {

        if (game.gameState.menuState == null) {
            game.gameState.menuState = new MenuState();
        }
        if (game.gameState.purchaseState == null) {
            game.gameState.purchaseState = new PurchaseState();
        }

        MenuState menuState = game.gameState.menuState;
        PurchaseState state = game.gameState.purchaseState;


        if (state.referrals == null) {
            state.referrals = new ArrayList<>();
        }

        state.referralCount = count;

        if (state.referralCount >= REFERRAL_REWARD_2) {
            if (!state.referrals.contains(game.params.appStoreSku5Chapter)) {

                // add 5 chapters
                state.referrals.add(game.params.appStoreSku5Chapter);

                menuState.referralsToDisplay.push(REFERRAL_TYPE_1);
            }
        } else if (state.referralCount >= REFERRAL_REWARD_1) {
            if (!state.referrals.contains(game.params.appStoreSku10Chapter)) {

                // add 20 chapters
                state.referrals.add(game.params.appStoreSku10Chapter);
                state.referrals.add(game.params.appStoreSku10Chapter);

                menuState.referralsToDisplay.push(REFERRAL_TYPE_2);
            }
        }
    }
}
