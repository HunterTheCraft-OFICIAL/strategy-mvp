package io.hunterthecraft.strategy.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.List;

public class CountryLoader {
    public static List<Country> loadCountries() {
        try {
            FileHandle file = Gdx.files.internal("data/countries.geo.json");
            if (!file.exists()) {
                Gdx.app.log("ERRO", "Arquivo data/countries.geo.json NAO ENCONTRADO!");
                return new ArrayList<>();
            }

            String jsonText = file.readString();
            JsonValue root = new JsonReader().parse(jsonText);
            JsonValue features = root.get("features");
            List<Country> countries = new ArrayList<>();

            for (JsonValue feature : features) {
                String name = feature.get("properties").getString("name", "Unknown");
                JsonValue geometry = feature.get("geometry");
                if (geometry == null) continue;

                String type = geometry.getString("type", "");
                if (!"Polygon".equals(type)) continue;

                JsonValue coords = geometry.get("coordinates");
                if (coords == null || coords.size() == 0) continue;

                JsonValue ring = coords.get(0);
                if (ring == null || ring.size() < 3) continue;

                List<Vector2> points = new ArrayList<>();
                for (JsonValue coord : ring) {
                    if (coord.size() < 2) continue;
                    float lon = coord.get(0).asFloat();
                    float lat = coord.get(1).asFloat();
                    float x = (lon + 180) * 2.0f;
                    float y = (lat + 90) * 2.0f;
                    points.add(new Vector2(x, y));
                }

                if (points.size() >= 3) {
                    List<List<Vector2>> polygons = new ArrayList<>();
                    polygons.add(points);
                    countries.add(new Country(name, polygons));
                }
            }

            Gdx.app.log("MAPA", "Carregados " + countries.size() + " paises com sucesso.");
            return countries;

        } catch (Exception e) {
            Gdx.app.log("ERRO", "Falha critica ao carregar mapa: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
