package com.rezzo.libgdxjam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rezzo.libgdxjam.LifeIsSpace;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new LifeIsSpace(), config);
        config.title = "Planetoid";
        config.resizable = false;
        config.width = 800;
        config.height = 600;
    }
}
