package io.hunterthecraft.strategy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import io.hunterthecraft.strategy.Main;

public class MainMenuScreen implements Screen {
    private Main game;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture logo;
    private float[] buttonX = new float[8];
    private float[] buttonY = new float[8];
    private float[] buttonWidth = new float[8];
    private float[] buttonHeight = new float[8];

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        logo = new Texture("libgdx.png");

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Logo no topo
        float logoWidth = 400f;
        float logoHeight = 200f;
        float logoX = (screenWidth - logoWidth) / 2f;
        float logoY = screenHeight - logoHeight - 50f;

        // Botões (4 por linha)
        float btnWidth = 200f;
        float btnHeight = 80f;
        float btnSpacing = 20f;
        float totalWidth = (4 * btnWidth) + (3 * btnSpacing);
        float startX = (screenWidth - totalWidth) / 2f;

        // Linha 1 (BTN1-BTN4)
        float startY1 = logoY - btnHeight - 50f;
        for (int i = 0; i < 4; i++) {
            buttonX[i] = startX + i * (btnWidth + btnSpacing);            buttonY[i] = startY1;
            buttonWidth[i] = btnWidth;
            buttonHeight[i] = btnHeight;
        }

        // Linha 2 (BTN5-BTN8)
        float startY2 = startY1 - btnHeight - 30f;
        for (int i = 0; i < 4; i++) {
            buttonX[i + 4] = startX + i * (btnWidth + btnSpacing);
            buttonY[i + 4] = startY2;
            buttonWidth[i + 4] = btnWidth;
            buttonHeight[i + 4] = btnHeight;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();

        // Desenha logo
        batch.draw(logo, 
                   (Gdx.graphics.getWidth() - logo.getWidth()) / 2f, 
                   Gdx.graphics.getHeight() - logo.getHeight() - 50f);

        // Desenha botões
        for (int i = 0; i < 8; i++) {
            // Fundo do botão
            batch.setColor(0.3f, 0.3f, 0.4f, 1f);
            batch.draw(logo, buttonX[i], buttonY[i], buttonWidth[i], buttonHeight[i]);

            // Texto do botão
            batch.setColor(1f, 1f, 1f, 1f);
            String text = getButtonText(i);
            float textX = buttonX[i] + buttonWidth[i] / 2f;
            float textY = buttonY[i] + buttonHeight[i] / 2f + 10f;
            float textWidth = font.getGlyphLayout().setText(text).width;
            font.draw(batch, text, textX - textWidth / 2f, textY);
        }

        batch.end();

        // Detecta toque
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int i = 0; i < 8; i++) {
                if (touchX >= buttonX[i] && touchX <= buttonX[i] + buttonWidth[i] &&                    touchY >= buttonY[i] && touchY <= buttonY[i] + buttonHeight[i]) {
                    handleButtonPress(i);
                    break;
                }
            }
        }
    }

    private String getButtonText(int index) {
        switch (index) {
            case 0: return "NOVO JOGO";
            case 1: return "CARREGAR";
            case 2: return "OPÇÕES";
            case 3: return "SAIR";
            case 4: return "CRÉDITOS";
            case 5: return "TUTORIAL";
            case 6: return "AJUDA";
            case 7: return "RECORDES";
            default: return "BOTÃO " + index;
        }
    }

    private void handleButtonPress(int index) {
        switch (index) {
            case 0:
                Gdx.app.log("Menu", "Iniciando novo jogo...");
                break;
            case 3:
                Gdx.app.exit();
                break;
            default:
                Gdx.app.log("Menu", "Botão " + index + " pressionado");
                break;
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
    public void dispose() {        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (logo != null) logo.dispose();
    }
}