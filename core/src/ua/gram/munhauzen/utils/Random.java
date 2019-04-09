package ua.gram.munhauzen.utils;

public class Random extends java.util.Random {

    public int between(int min, int max) {
        return nextInt((max - min) + 1) + min;
    }
}
