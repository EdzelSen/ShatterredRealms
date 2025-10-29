package com.shatteredrealms;

import com.badlogic.gdx.Game;

public class ShatteredRealmsGame extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this)); // Launch the SplashScreen first then MainMenuScreen
    }
}
