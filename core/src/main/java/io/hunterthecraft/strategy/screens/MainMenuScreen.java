package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.hunterthecraft.strategy.Main;

public class MainMenuScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        font.draw(batch, "STRATEGY (ALPHA)", 100, Gdx.graphics.getHeight() - 100);
        font.draw(batch, "TOQUE PARA CONTINUAR", 100, Gdx.graphics.getHeight() - 150);
        batch.end();

        // Sai ao tocar (substitua por botões reais depois)
        if (Gdx.input.isTouched()) {
            // Aqui você colocará a lógica do jogo
            Gdx.app.exit(); // Por enquanto, só fecha
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
    }
}