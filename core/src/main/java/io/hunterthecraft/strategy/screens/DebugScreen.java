package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.hunterthecraft.strategy.Main;

public class DebugScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private FitViewport viewport;
    private String errorMessage;
    private boolean canCopy = false;

    public DebugScreen(Main game, Exception e) {
        this.game = game;
        this.errorMessage = "Erro no mapa:\n" + getFullStackTrace(e);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(0.8f);
        viewport = new FitViewport(800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // Título
        font.setColor(Color.YELLOW);
        font.draw(batch, "❌ FALHA AO CARREGAR MAPA", 20, viewport.getWorldHeight() - 30);

        // Mensagem de erro
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, errorMessage, Color.RED, viewport.getWorldWidth() - 40, 0, true);
        font.draw(batch, layout, 20, viewport.getWorldHeight() - 60 - layout.height);

        // Instrução
        font.setColor(Color.WHITE);
        font.draw(batch, "Toque na tela para copiar o erro", 20, 30);

        batch.end();

        // Detecta toque para copiar
        if (Gdx.input.isTouched()) {
            if (!canCopy) {
                canCopy = true;
            } else {
                Gdx.app.getClipboard().setContents(errorMessage);
                Gdx.app.log("DebugScreen", "Erro copiado para a área de transferência!");
                canCopy = false;
            }
        }
    }

    private String getFullStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @@Override
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