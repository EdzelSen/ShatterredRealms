package com.shatteredrealms;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class SplashScreen implements Screen {
    private final ShatteredRealmsGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private float elapsedTime = 0f;

    private static final float DISPLAY_TIME = 3f; // Total duration (seconds)
    private static final float VIRTUAL_WIDTH = 1280;
    private static final float VIRTUAL_HEIGHT = 720;
    private static final float FADE_DURATION = 1f; // 1 second fade

    public SplashScreen(ShatteredRealmsGame game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // Load custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ThaleahFat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 72;
        param.color = Color.WHITE;
        font = generator.generateFont(param);
        generator.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        // Clear screen
        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        String title = "Shattered Realms";
        GlyphLayout layout = new GlyphLayout(font, title);
        float x = (VIRTUAL_WIDTH - layout.width) / 2;
        float y = (VIRTUAL_HEIGHT + layout.height) / 2;

        // Compute fade alpha (opacity)
        float alpha;
        if (elapsedTime < FADE_DURATION) {
            // Fade in
            alpha = elapsedTime / FADE_DURATION;
        } else if (elapsedTime > DISPLAY_TIME - FADE_DURATION) {
            // Fade out
            alpha = 1f - (elapsedTime - (DISPLAY_TIME - FADE_DURATION)) / FADE_DURATION;
        } else {
            // Fully visible
            alpha = 1f;
        }

        // Apply fade alpha
        font.setColor(1, 1, 1, alpha);
        font.draw(batch, layout, x, y);

        batch.end();

        // Switch to main menu after full display time
        if (elapsedTime > DISPLAY_TIME) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
