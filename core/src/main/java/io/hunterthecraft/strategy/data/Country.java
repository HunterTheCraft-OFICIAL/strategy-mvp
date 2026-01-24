package io.hunterthecraft.strategy.data;

import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class Country {
    public final String name;
    public final List<List<Vector2>> polygons;

    public Country(String name, List<List<Vector2>> polygons) {
        this.name = name;
        this.polygons = polygons;
    }
}
