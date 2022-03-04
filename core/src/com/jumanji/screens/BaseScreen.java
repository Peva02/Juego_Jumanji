package com.jumanji.screens;

import com.badlogic.gdx.Screen;

/**
 * @author Eduardo Pérez Vargas
 */

//Creamos la clase BaseScreen para no tener que implementar todos los metodos,
// en todas las pantallas de nuestro juego
public abstract class BaseScreen implements Screen {
    protected Main mainGame;

    public BaseScreen(Main mainGame) {
        this.mainGame = mainGame;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
