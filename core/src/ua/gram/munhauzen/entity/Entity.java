package ua.gram.munhauzen.entity;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Entity {

    public String id;

    public Entity() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "#" + id;
    }
}
