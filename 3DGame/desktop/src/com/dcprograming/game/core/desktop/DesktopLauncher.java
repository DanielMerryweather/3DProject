package com.dcprograming.game.core.desktop;

import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dcprograming.game.core.Core;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Toolkit.getDefaultToolkit().getScreenResolution() * 10;
		config.height = Toolkit.getDefaultToolkit().getScreenResolution() * 8;
		config.resizable = false;
		new LwjglApplication(new Core(), config);
	}
}
