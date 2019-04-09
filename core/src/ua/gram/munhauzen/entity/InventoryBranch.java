package ua.gram.munhauzen.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InventoryBranch {

    public static final String DEFAULT = "DEFAULT";
    public static final String EMPTY = "EMPTY";
    public static final String DAY_PREFIX = "DAY_";

    public List<String> inventory;
    public List<Branch> options;

    public InventoryBranch() {
        inventory = new ArrayList<>(1);
        options = new ArrayList<>();
    }

}
