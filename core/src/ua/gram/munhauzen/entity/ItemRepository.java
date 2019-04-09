package ua.gram.munhauzen.entity;

public class ItemRepository {

    public static Item find(GameState gameState, String id) {
        for (Item o : gameState.itemRegistry) {
            if (o.id.equals(id)) {
                return o;
            }
        }

        return null;
    }
}
