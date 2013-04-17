package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.PatternMatcher;
import android.util.Patterns;

public class BitmapUtil {
	public static Bitmap getBitmapFromURL(String url, int width, int height) {
		Bitmap bm = null;
		try {
			URL u=new URL(url);
			bm = getBitmap(u.openStream(), width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}
	public static Bitmap getBitmap(String src, int width, int height) {
		Bitmap bm = null;
		try {
			File file=new File(src);
			InputStream in = new FileInputStream(file);
			bm = getBitmap(in, width, height);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap getBitmap(File file, int width, int height) {
		Bitmap bm = null;
		try {
			InputStream in = new FileInputStream(file);
			bm = getBitmap(in, width, height);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap getBitmap(InputStream in, int width, int height) {
		Bitmap bm = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeStream(in, null, opts);
		int xScale = opts.outWidth / width;
		int yScale = opts.outHeight / height;
		opts.inSampleSize = xScale > yScale ? xScale : yScale;
		opts.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeStream(in, null, opts);
		return bm;
	}
}
