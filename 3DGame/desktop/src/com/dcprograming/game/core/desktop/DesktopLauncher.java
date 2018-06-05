package com.dcprograming.game.core.desktop;

import java.awt.Toolkit;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dcprograming.game.core.Core;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		int pixels = (int) Math.min(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 9 / 2, Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 16 / 2);
		config.width = pixels * 16;
		config.height = pixels * 9;
		System.out.println("Initial Screen Resolution: " + config.width + "x" + config.height);
		config.resizable = false;
		config.addIcon("Icon.png", FileType.Internal);
		config.addIcon("Icon32.png", FileType.Internal);
		config.title = "Quittage";
		Core game = new Core(pixels);
		new LwjglApplication(game, config);
	}
}
