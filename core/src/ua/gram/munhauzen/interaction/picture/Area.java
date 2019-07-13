package ua.gram.munhauzen.interaction.picture;

import com.badlogic.gdx.math.Polygon;

public class Area extends Polygon {

    public final short[] triangles;

    public Area(float[] vertices, short[] triangles) {
        super(vertices);

        this.triangles = triangles;
    }
}
