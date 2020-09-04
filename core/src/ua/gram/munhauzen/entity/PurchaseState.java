package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.PlatformParams;

public class PurchaseState implements JsonEntry {

    @JsonProperty
    public boolean isPro;
    @JsonProperty
    public boolean isVersionSelected;
    @JsonProperty
    public int maxChapter;
    @JsonProperty
    public String currentExpansionVersion;
    @JsonProperty
    public ArrayList<Purchase> purchases;
    @JsonProperty
    public ArrayList<Product> products;
    @JsonProperty
    public ArrayList<String> promocodes;
    @JsonProperty
    public int referralCount;
    @JsonProperty
    public ArrayList<String> referrals;

    public PurchaseState() {
        if (purchases == null)
            purchases = new ArrayList<>();
        if (products == null)
            products = new ArrayList<>();
        if (promocodes == null)
            promocodes = new ArrayList<>();
        if (referrals == null)
            referrals = new ArrayList<>();
    }

    public void setPro(PlatformParams params) {
        isPro = false;

        if (purchases != null) {

            boolean hasFull = false, hasPart1 = false, hasPart2 = false;

            for (Purchase purchase : purchases) {
                if (purchase.productId.equals(params.appStoreSkuFullThanks)) {
                    hasFull = true;
                }

                if (purchase.productId.equals(params.appStoreSkuFull)) {
                    hasFull = true;
                }

                if (purchase.productId.equals(params.appStoreSkuPart1)) {
                    hasPart1 = true;
                }

                if (purchase.productId.equals(params.appStoreSkuPart2)) {
                    hasPart2 = true;
                }
            }

            isPro = hasFull || (hasPart1 && hasPart2);

        }

        isPro = isPro || MunhauzenGame.developmentIsPro;

        if (MunhauzenGame.developmentSimulatePurchase) {
            isPro = false;
        }
    }
}
