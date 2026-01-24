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

import java.util.ArrayList;
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

        camera.update();        shapeRenderer.setProjectionMatrix(camera.combined);

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

                    // Reconstrói o array com validação rigorosa
                    List<Float> validPoints = new ArrayList<>();
                    boolean skipPolygon = false;

                    for (Vector2 v : polygon) {
                        if (v == null) {
                            skipPolygon = true;
                            break;
                        }
                        float x = v.x;
                        float y = v.y;
                        if (Float.isNaN(x) || Float.isInfinite(x) || Float.isNaN(y) || Float.isInfinite(y)) {
                            skipPolygon = true;
                            break;
                        }
                        validPoints.add(x);
                        validPoints.add(y);
                    }

                    if (skipPolygon || validPoints.size() < 6 || validPoints.size() % 2 != 0) {
                        Gdx.app.log("MapScreen", "Polígono ignorado: país=" + country.name + ", pontos=" + validPoints.size());
                        continue;
                    }

                    // Converte para array primitivo
                    float[] points = new float[validPoints.size()];
                    for (int i = 0; i < validPoints.size(); i++) {
                        points[i] = validPoints.get(i);                    }

                    // Renderiza
                    shapeRenderer.polygon(points);
                }
            }
        } catch (Exception e) {
            Gdx.app.error("MapScreen", "Erro durante renderização", e);
            if (shapeRenderer.isDrawing()) {
                shapeRenderer.end();
            }
            game.setScreen(new DebugScreen(game, e));
            return;
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
    }}
