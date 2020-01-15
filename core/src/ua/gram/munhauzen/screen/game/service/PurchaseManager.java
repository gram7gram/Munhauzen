package ua.gram.munhauzen.screen.game.service;

import ua.gram.munhauzen.screen.GameScreen;

public class PurchaseManager {

    public final GameScreen screen;

    public PurchaseManager(GameScreen screen) {
        this.screen = screen;
    }

    public boolean isExpansionPurchased(String expansion) {
        if (expansion == null) return true;


        return false;
    }
}
