package ua.gram.munhauzen.entity;

import java.util.HashSet;

public class ServantsState {

    public int hirePage;
    public final HashSet<String> viewedServants;

    public ServantsState() {
        viewedServants = new HashSet<>();
        hirePage = 1;
    }
}
