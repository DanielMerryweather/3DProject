package com.dcprograming.game.core.desktop;

import java.awt.Toolkit;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dcprograming.game.core.Core;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Toolkit.getDefaultToolkit().getScreenResolution() * 16;
		config.height = Toolkit.getDefaultToolkit().getScreenResolution() * 9;
		config.resizable = false;
		config.addIcon("Icon.png", FileType.Internal);
		config.addIcon("Icon32.png", FileType.Internal);
		config.title = "Quiddich";
		config.useGL30 = true;
		Core game = new Core();
		new LwjglApplication(game, config);
	}
}