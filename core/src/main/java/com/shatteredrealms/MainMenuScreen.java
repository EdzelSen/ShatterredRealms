package com.shatteredrealms;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MainMenuScreen implements Screen {
    private final ShatteredRealmsGame game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont titleFont;
    private BitmapFont buttonFont;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Music backgroundMusic;
    private Stage stage;
    private Skin skin;

    private float bgX = 0f;
    private float scrollSpeed = 25f;
    private String titleText = "Shattered Realms";

    // Virtual game resolution
    private static final float VIRTUAL_WIDTH = 1280;
    private static final float VIRTUAL_HEIGHT = 720;

    public MainMenuScreen(ShatteredRealmsGame game) {
        this.game = game;
        batch = new SpriteBatch();

        // Camera + viewport setup
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        // Stage for UI
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        // Load background
        background = new Texture("images/MountainBackgroundOrig.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        // === Load title font (ThaleahFat.ttf) ===
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ThaleahFat.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 64;
        titleParam.color = Color.WHITE;
        titleParam.shadowColor = new Color(0, 0, 0, 0.7f);
        titleParam.shadowOffsetX = 3;
        titleParam.shadowOffsetY = 3;
        titleFont = titleGen.generateFont(titleParam);
        titleGen.dispose();

        // === Load button font (KennyPixel.ttf) ===
        FreeTypeFontGenerator buttonGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/KenneyPixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter buttonParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        buttonParam.size = 32;
        buttonParam.color = Color.WHITE;
        buttonParam.shadowColor = new Color(0, 0, 0, 0.5f);
        buttonParam.shadowOffsetX = 2;
        buttonParam.shadowOffsetY = 2;
        buttonFont = buttonGen.generateFont(buttonParam);
        buttonGen.dispose();

        // Load skin
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create custom TextButton style using KennyPixel font
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = new Color(0.8f, 0.8f, 0.8f, 1);
        buttonStyle.overFontColor = new Color(1f, 1f, 0.8f, 1);

        // Create buttons
        float buttonWidth = 300;
        float buttonHeight = 70;
        float buttonSpacing = 20;
        float startY = VIRTUAL_HEIGHT / 2 + 50;

        TextButton startButton = new TextButton("NEW GAME", buttonStyle);
        startButton.setSize(buttonWidth, buttonHeight);
        startButton.setPosition((VIRTUAL_WIDTH - buttonWidth) / 2, startY);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        TextButton optionsButton = new TextButton("OPTIONS", buttonStyle);
        optionsButton.setSize(buttonWidth, buttonHeight);
        optionsButton.setPosition((VIRTUAL_WIDTH - buttonWidth) / 2, startY - buttonHeight - buttonSpacing);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openOptions();
            }
        });

        TextButton exitButton = new TextButton("EXIT", buttonStyle);
        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.setPosition((VIRTUAL_WIDTH - buttonWidth) / 2, startY - 2 * (buttonHeight + buttonSpacing));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitGame();
            }
        });

        // Add buttons to stage
        stage.addActor(startButton);
        stage.addActor(optionsButton);
        stage.addActor(exitButton);

        // Load and setup background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/BeforeTheLightFades_EvanCall(Frieren).mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
    }

    private void startGame() {
        System.out.println("Start game clicked!");
        // game.setScreen(new GameScreen(game));
    }

    private void openOptions() {
        System.out.println("Options clicked!");
        // game.setScreen(new OptionsScreen(game));
    }

    private void exitGame() {
        System.out.println("Exit clicked!");
        Gdx.app.exit();
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.1f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Calculate scaled background dimensions
        float scale = VIRTUAL_HEIGHT / background.getHeight();
        float bgWidth = background.getWidth() * scale;

        // Move background and wrap at scaled width
        bgX += scrollSpeed * delta;
        if (bgX >= bgWidth) {
            bgX -= bgWidth;
        }

        batch.begin();

        // Draw two copies side by side for seamless scrolling
        batch.draw(background, -bgX, 0, bgWidth, VIRTUAL_HEIGHT);
        batch.draw(background, -bgX + bgWidth, 0, bgWidth, VIRTUAL_HEIGHT);

        // Title
        GlyphLayout layout = new GlyphLayout(titleFont, titleText);
        float titleX = (VIRTUAL_WIDTH - layout.width) / 2;
        float titleY = VIRTUAL_HEIGHT - 60;
        titleFont.draw(batch, layout, titleX, titleY);

        batch.end();

        // Draw UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        backgroundMusic.pause();
    }

    @Override
    public void pause() {
        backgroundMusic.pause();
    }

    @Override
    public void resume() {
        backgroundMusic.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        titleFont.dispose();
        buttonFont.dispose();
        backgroundMusic.dispose();
        stage.dispose();
        skin.dispose();
    }
}
