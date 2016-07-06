package ir.sharif.ce.javaClass.paint.test;

import static java.lang.Math.random;

import java.awt.Color;
import java.awt.Font;
import java.io.File;


/**
 * @author Ruholla Jahande
 */
public class Utils {

	private Utils() {
	}

	public static Color getRandomColor() {
		return new Color((int) (random() * 255), (int) (random() * 255),
				(int) (random() * 255));
	}

	public static Font getResizedFont(Font f, String text, int width, int height) {
		int sizeW = (int) (width * 1.8 / text.length());
		int sizeH = (int) (height * 1.9);
		if (sizeH < sizeW) {
			sizeW = sizeH;
		}
		if (sizeW > 50) {
			sizeW = 50;
		}
		return new Font(f.getFontName(), f.getStyle(), sizeW);
	}

	public static String getFormat(File file) {
		String fFormat = file.getAbsolutePath();
		return fFormat.substring(fFormat.lastIndexOf('.') + 1);
	}

	public static String getFormat(String path) {

		return path.substring(path.lastIndexOf('.') + 1);
	}
}
