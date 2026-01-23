package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.hunterthecraft.strategy.Main;

public class LoadingScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private Texture logo;
    private float progress = 0f;

    public LoadingScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        logo = new Texture("libgdx.png");
    }

    @Override
    public void render(float delta) {
        // Simula carregamento (substitua por lógica real depois)
        if (progress < 1f) {
            progress += delta * 0.5f; // Ajuste a velocidade aqui
        } else {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        // Renderiza fundo
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        
        batch.begin();
        // Desenha logo centralizada
        float logoX = (Gdx.graphics.getWidth() - logo.getWidth()) / 2f;
        float logoY = (Gdx.graphics.getHeight() - logo.getHeight()) / 2f;
        batch.draw(logo, logoX, logoY);
        
        // Desenha barra de progresso
        float barWidth = 300f;
        float barHeight = 20f;
        float barX = (Gdx.graphics.getWidth() - barWidth) / 2f;
        float barY = logoY - 50f;
        
        // Fundo da barra
        batch.setColor(0.3f, 0.3f, 0.4f, 1f);
        batch.draw(logo, barX, barY, barWidth, barHeight);
        
        // Progresso
        batch.setColor(0.2f, 0.8f, 0.4f, 1f);
        batch.draw(logo, barX, barY, barWidth * progress, barHeight);
        
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
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
        if (logo != null) logo.dispose();
    }
}