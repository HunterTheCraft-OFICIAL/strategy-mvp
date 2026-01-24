package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import io.hunterthecraft.strategy.Main;
import io.hunterthecraft.strategy.data.WorldData;

public class MapScreen implements Screen {

    private Main game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    public MapScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Renderiza todos os países
        for (WorldData.Country country : WorldData.COUNTRIES) {
            Color color = getColorForCountry(country.name);
            shapeRenderer.setColor(color);

            // Cada país pode ter múltiplos polígonos (ex: ilhas)
            for (Vector2[] polygon : country.polygons) {
                if (polygon.length < 3) continue; // precisa de pelo menos 3 pontos

                float[] points = new float[polygon.length * 2];
                for (int i = 0; i < polygon.length; i++) {
                    points[i * 2] = polygon[i].x;
                    points[i * 2 + 1] = polygon[i].y;
                }
                shapeRenderer.polygon(points);
            }
        }

        shapeRenderer.end();
    }

    // Gera uma cor consistente baseada no nome do país
    private Color getColorForCountry(String name) {
        int hash = name.hashCode();
        float r = ((hash >> 16) & 0xFF) / 255f;
        float g = ((hash >> 8) & 0xFF) / 255f;
        float b = (hash & 0xFF) / 255f;
        return new Color(r, g, b, 0.6f); // transparência suave
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