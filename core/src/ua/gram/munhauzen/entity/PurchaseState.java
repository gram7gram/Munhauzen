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
    public ArrayList<Purchase> purchases;
    @JsonProperty
    public ArrayList<Product> products;

    public PurchaseState() {
        if (purchases == null)
            purchases = new ArrayList<>();
        if (products == null)
            products = new ArrayList<>();
    }

    public void setPro(PlatformParams params) {
        isPro = false;

        if (purchases != null) {

            boolean hasFull = false, hasPart1 = false, hasPart2 = false;

            for (Purchase purchase : purchases) {
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
    }
}
