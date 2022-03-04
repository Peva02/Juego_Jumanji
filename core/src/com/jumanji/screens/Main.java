package com.jumanji.screens;

import com.badlogic.gdx.Game;
import com.jumanji.extra.AssetMan;

/**
 * @author Eduardo Pérez Vargas
 */
//Main es el encargado de adminstrar las pantallas del juego,
// y lo utilizaremos para ir mostrando las pantallas cuando corresponda
public class Main extends Game {
    public GetReadyScreen readyScreen;
    public GameOverScreen overScreen;

    //AssetMan sera la clase encargada de adminstrarnos los recuros necesarios
    public AssetMan assetManager;
    @Override
    public void create() {
        this.assetManager = new AssetMan();
        this.overScreen = new GameOverScreen(this);
        this.readyScreen = new GetReadyScreen(this);
        setScreen(readyScreen);
    }
}
