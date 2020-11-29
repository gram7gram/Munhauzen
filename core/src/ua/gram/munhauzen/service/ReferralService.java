package ua.gram.munhauzen.service;

import java.util.ArrayList;
import java.util.Stack;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.utils.Log;

public class ReferralService {

    public final String tag = getClass().getSimpleName();
    public static final String REFERRAL_TYPE_1 = "invited_3";
    public static final String REFERRAL_TYPE_2 = "invited_7";

    public static final int REFERRAL_REWARD_1 = 3;
    public static final int REFERRAL_REWARD_2 = 7;
    public static final int MAX_REFERRAL_COUNT = 7;

    final MunhauzenGame game;

    public ReferralService(MunhauzenGame game) {
        this.game = game;
    }

    public void copyReferralLink() {
        game.stopCurrentSfx();
        MunhauzenGame.referralInterface.sendReferralLink();
    }

    /**
     * Set value once on app start
     */
    public void setReferralCount(int count) {

        Log.e(tag, "setReferralCount=" + count);

        count = Math.max(0, count);

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
        if (menuState.referralsToDisplay == null) {
            menuState.referralsToDisplay = new Stack<>();
        }

        state.referralCount = count;

        if (!game.params.isStandaloneProVersion) {
            rewardInAppPurchase();
        }
    }

    private void rewardInAppPurchase() {

        MenuState menuState = game.gameState.menuState;
        PurchaseState state = game.gameState.purchaseState;

        if (state.referralCount >= REFERRAL_REWARD_2) {
            if (!state.referrals.contains(game.params.appStoreSku10Chapter)) {

                // add 20 chapters
                state.referrals.add(game.params.appStoreSku10Chapter);
                state.referrals.add(game.params.appStoreSku10Chapter);

                menuState.referralsToDisplay.push(REFERRAL_TYPE_2);
            }
        }

        if (state.referralCount >= REFERRAL_REWARD_1) {
            if (!state.referrals.contains(game.params.appStoreSku5Chapter)) {

                // add 5 chapters
                state.referrals.add(game.params.appStoreSku5Chapter);

                menuState.referralsToDisplay.push(REFERRAL_TYPE_1);
            }
        }
    }
}
