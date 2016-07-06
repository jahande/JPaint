package ir.sharif.ce.javaClass.paint.test;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;



/**
 * @author Ruholla Jahande
 *
 */
public class JPaintFileFilter extends FileFilter {

	private final static String[] FORMATS = ImageIO.getWriterFormatNames();
	private final static String CONSTANT = "*.";
	private final static String IMAGE_FILES = "All image files";
	private final String format;
	private final boolean allImages;
	private static JPaintFileFilter[] res = null;

	public static JPaintFileFilter[] getImageInstances() {
		if (res == null) {
			res = new JPaintFileFilter[FORMATS.length + 1];
			for (int i = 0; i < res.length - 1; i++) {
				res[i] = new JPaintFileFilter(FORMATS[i], false);
			}
			res[res.length - 1] = new JPaintFileFilter(null, true);
		}
		return res;
	}

	public JPaintFileFilter(String s, boolean allImages) {
		super();
		this.format = s;
		this.allImages = allImages;
	}

	public static boolean acceptAll(File file) {
		String fFormat = Utils.getFormat(file);
		for (String i : FORMATS) {
			if (i.equals(fFormat)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		if (allImages) {
			return IMAGE_FILES;
		} else {
			return CONSTANT + format;
		}
	}

	@Override
	public boolean accept(File file) {
		String fFormat = Utils.getFormat(file);
		if (file.isDirectory()) {
			return true;
		}
		if (fFormat.equals(this.format)) {
			return true;
		} else if (this.allImages) {
			return acceptAll(file);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.format;
	}

}