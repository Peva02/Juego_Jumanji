package com.jumanji.desktop;

import static com.jumanji.extra.Util.*;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jumanji.screens.Main;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;
        config.vSyncEnabled = true;
        config.foregroundFPS = 120;
        new LwjglApplication(new Main(), config);
    }
}
