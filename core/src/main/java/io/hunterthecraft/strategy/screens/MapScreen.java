package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.hunterthecraft.strategy.Main;
import io.hunterthecraft.strategy.data.Country;
import io.hunterthecraft.strategy.data.CountryLoader;

import java.util.List;

public class MapScreen implements Screen {

    private Main game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private List<Country> countries;

    public MapScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer();
        countries = CountryLoader.loadCountries();

        if (countries.isEmpty()) {
            Gdx.app.postRunnable(() -> {
                try {
                    throw new RuntimeException("Mapa vazio: countries.geo.json não carregado ou inválido");
                } catch (Exception e) {
                    game.setScreen(new DebugScreen(game, e));
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (!shapeRenderer.isDrawing()) {
            try {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            } catch (Exception e) {
                Gdx.app.error("MapScreen", "Falha ao iniciar ShapeRenderer", e);
                game.setScreen(new DebugScreen(game, e));
                return;
            }
        }

        try {
            for (Country country : countries) {
                if (country == null || country.polygons == null) continue;
                Color color = getColorForCountry(country.name);
                shapeRenderer.setColor(color);

                for (List<Vector2> polygon : country.polygons) {
                    if (polygon == null || polygon.size() < 3) continue;

                    float[] points = new float[polygon.size() * 2];
                    boolean valid = true;
                    for (int i = 0; i < polygon.size(); i++) {
                        Vector2 v = polygon.get(i);
                        if (v == null || Float.isNaN(v.x) || Float.isNaN(v.y)) {
                            valid = false;
                            break;
                        }
                        points[i * 2] = v.x;
                        points[i * 2 + 1] = v.y;
                    }

                    // Validação final: deve ser > 0, par, e ter pelo menos 3 pontos
                    if (valid && points.length > 0 && points.length % 2 == 0) {
                        if (points.length < 6) {
                            Gdx.app.log("MapScreen", "Polígono ignorado: menos de 3 pontos (" + points.length/2 + ") para país " + country.name);
                        } else {
                            shapeRenderer.polygon(points);
                        }
                    } else {
                        Gdx.app.log("MapScreen", "Polígono ignorado: tamanho inválido (" + points.length + ") para país " + country.name);
                    }
                }
            }
        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Erro durante renderização", e);
            if (shapeRenderer.isDrawing()) {
                shapeRenderer.end();
            }
            game.setScreen(new DebugScreen(game, e));            return;
        }

        if (shapeRenderer.isDrawing()) {
            shapeRenderer.end();
        }
    }

    private Color getColorForCountry(String name) {
        int hash = name.hashCode();
        float r = ((hash >> 16) & 0xFF) / 255f;
        float g = ((hash >> 8) & 0xFF) / 255f;
        float b = (hash & 0xFF) / 255f;
        return new Color(r, g, b, 0.6f);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
