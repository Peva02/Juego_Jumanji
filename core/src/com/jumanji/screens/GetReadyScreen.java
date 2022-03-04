package com.jumanji.screens;

import static com.jumanji.extra.Util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class GetReadyScreen extends BaseScreen {
    private final Stage stage;
    private final Music musicLobby;

    public GetReadyScreen(Main mainGame) {
        super(mainGame);
        FillViewport fillViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGTH);
        this.stage = new Stage(fillViewport);
        this.stage.addActor(mainGame.assetManager.addBackground());
        this.musicLobby = mainGame.assetManager.getMusicLobby();
    }

    //Añade la imagen de "TaptoPlay"
    public void addTapToPlay() {
        Image tapToPlay = new Image(mainGame.assetManager.getTaptoPlay());
        tapToPlay.setPosition(WORLD_WIDTH / 5, WORLD_HEIGTH / 2);
        tapToPlay.setSize(3f, 1.5f);//Tienes que coger las medidas del mundo, no de la pantalla
        this.stage.addActor(tapToPlay);//No se puede olvidar borrarlo
    }

    @Override
    public void show() {
        addTapToPlay();
        this.musicLobby.setLooping(true);
        this.musicLobby.play();
    }

    @Override
    public void hide() {
        this.musicLobby.stop();
    }

    @Override
    public void render(float delta) {
        this.stage.draw();
        //Si el usuario toca la pantalla, o pulsa espacio en el pc, para la musica del looby
        // y lanza la ventana de GameScreen
        // y lanza la ventana de GameScreen
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(62)) {
            this.musicLobby.stop();
            mainGame.setScreen(new GameScreen(mainGame));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.stage.dispose();
    }
}
