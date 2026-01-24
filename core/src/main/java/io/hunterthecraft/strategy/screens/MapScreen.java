package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import io.hunterthecraft.strategy.Main;
import io.hunterthecraft.strategy.data.Country;
import io.hunterthecraft.strategy.data.CountryLoader;

public class MapScreen implements Screen {

    private Main game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Array<Country> countries;

    public MapScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer();
        countries = new Array<>(CountryLoader.loadCountries());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Country country : countries) {
            Color color = getColorForCountry(country.name);
            shapeRenderer.setColor(color);

            for (List<Vector2> polygon : country.polygons) {
                if (polygon.size() < 3) continue;
                float[] points = new float[polygon.size() * 2];
                for (int i = 0; i < polygon.size(); i++) {
                    points[i * 2] = polygon.get(i).x;
                    points[i * 2 + 1] = polygon.get(i).y;
                }
                shapeRenderer.polygon(points);
            }
        }

        shapeRenderer.end();
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
