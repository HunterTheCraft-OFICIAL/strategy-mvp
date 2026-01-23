package io.hunterthecraft.strategy;

import com.badlogic.gdx.Game;
import io.hunterthecraft.strategy.screens.LoadingScreen;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}