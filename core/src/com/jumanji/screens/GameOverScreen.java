package com.jumanji.screens;

import static com.jumanji.extra.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.jumanji.actor.Die;

public class GameOverScreen extends BaseScreen {
    private final Stage stage;
    private Die die;
    private final World WORLD;
    private final Music MUSICBG;
    private int statetime = 0;

    public GameOverScreen(Main mainGame) {
        super(mainGame);
        this.WORLD = new World(new Vector2(0, 0), false);
        FillViewport fillViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGTH);
        this.stage = new Stage(fillViewport);
        this.stage.addActor(mainGame.assetManager.addBackground());
        this.MUSICBG = mainGame.assetManager.getMusicGameOver();

    }

    //Añade la imagen de "GameOver"
    public void addGameOver() {
        Image gameOver = new Image(mainGame.assetManager.getGameOver());
        gameOver.setSize(3.5f, 2f);//Tienes que coger las medidas del mundo, no de la pantalla
        gameOver.setPosition((WORLD_WIDTH / 2) - (gameOver.getWidth() / 2), WORLD_HEIGTH - (WORLD_HEIGTH / 3f));
        this.stage.addActor(gameOver);//No se puede olvidar borrarlo
    }

    //Añade la animacion de muerte a la pantalla
    public void addDie() {
        this.die = new Die(this.WORLD, mainGame.assetManager.getDieAnimation(), new Vector2(WORLD_WIDTH, 1.38f));
        this.stage.addActor(die);
    }

    @Override
    //App primer plano
    public void show() {
        addGameOver();
        addDie();
        this.MUSICBG.setLooping(true);
        this.MUSICBG.setVolume(0.20f);
        this.MUSICBG.play();
    }

    @Override
    //App segundo plano
    public void hide() {
        this.die.detach();
        this.die.remove();
        this.MUSICBG.stop();
    }

    @Override
    //Se llama automaticamente cuando la app necesita renderizarse
    public void render(float delta) {
        super.render(delta);
        this.stage.draw();
        this.WORLD.step(delta, 6, 2);
        //Si el usuario toca la pantalla, o pulsa espacio en el pc,para la musica y se lanza la ventana de
        // getReady para comenzar a jugar de nuevo
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(62) || (int) this.die.getDeltaTime() == 12) {
            this.MUSICBG.stop();
            mainGame.setScreen(mainGame.readyScreen);
        }
    }

    @Override
    //Se llama para liberar los recursos de la pantalla
    public void dispose() {
        super.dispose();
        this.stage.dispose();
    }
}
