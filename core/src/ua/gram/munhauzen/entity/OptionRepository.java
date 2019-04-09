package ua.gram.munhauzen.entity;


public class OptionRepository {

    public static Option find(GameState gameState, String id) {
        if (id == null) return null;

        for (Option o : gameState.getOptionRegistry()) {
            if (o.id.equals(id)) {
                return o;
            }
        }

        return null;
    }
}
