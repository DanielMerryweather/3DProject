package com.dcprograming.game.core.desktop;

import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.dcprograming.game.core.Core;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Toolkit.getDefaultToolkit().getScreenResolution() * 16;
		config.height = Toolkit.getDefaultToolkit().getScreenResolution() * 9;
		config.resizable = false;
		Core game = new Core();
		new LwjglFrame(game, config).addComponentListener(game);
	}
}
