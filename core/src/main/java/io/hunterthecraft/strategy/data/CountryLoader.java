package io.hunterthecraft.strategy.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class Country {
    public final String name;
    public final List<List<Vector2>> polygons;

    public Country(String name, List<List<Vector2>> polygons) {
        this.name = name;
        this.polygons = polygons;
    }
}

public class CountryLoader {
    public static List<Country> loadCountries() {
        FileHandle file = Gdx.files.internal("data/countries.geo.json");
        String jsonText = file.readString();
        JsonValue root = new JsonReader().parse(jsonText);
        JsonValue features = root.get("features");

        List<Country> countries = new ArrayList<>();

        for (JsonValue feature : features) {
            String name = feature.get("properties").getString("name", "Unknown");
            JsonValue geometry = feature.get("geometry");
            String type = geometry.getString("type");

            if (!"Polygon".equals(type)) continue;

            JsonValue coords = geometry.get("coordinates");
            List<List<Vector2>> polygons = new ArrayList<>();

            // Primeiro anel (exterior)
            JsonValue ring = coords.get(0);
            List<Vector2> points = new ArrayList<>();
            for (JsonValue coord : ring) {
                float lon = coord.get(0).asFloat();
                float lat = coord.get(1).asFloat();
                float x = (lon + 180) * 2.0f; // 0 a 720
                float y = (lat + 90) * 2.0f;  // 0 a 360
                points.add(new Vector2(x, y));
            }
            polygons.add(points);

            countries.add(new Country(name, polygons));
        }

        return countries;
    }
}
