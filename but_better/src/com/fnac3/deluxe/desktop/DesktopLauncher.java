package com.fnac3.deluxe.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fnac3.deluxe.core.FNaC3Deluxe;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(false);
		config.setWindowedMode(1024, 768);
		config.setResizable(false);
		config.setWindowIcon(Files.FileType.Local, "assets/dreamscape.png");
		config.setTitle("Five Nights at Candy's 3 Reimagined");
		try {
			new Lwjgl3Application(new FNaC3Deluxe(), config);
		} catch (Exception e) {
			String error = e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()).replace(", ", "\n");
			File file = new File("log_error.txt");
			file.createNewFile();
			var writer = new FileWriter(file);
			writer.write(error);
			writer.close();
			var optionPane = new JOptionPane(error, JOptionPane.ERROR_MESSAGE);
			var dialog = optionPane.createDialog("FNaC 3 Deluxe Error!");
			dialog.setVisible(true);
			dialog.requestFocusInWindow();
		}
	}
}
