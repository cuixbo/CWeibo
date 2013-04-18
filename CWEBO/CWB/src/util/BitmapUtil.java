package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtil {
	public static Bitmap getBitmapFromURL(String url, int width, int height) {
		Bitmap bm = null;
		try {
			URL u = new URL(url);
			bm = getBitmap(u.openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap getBitmap(String src, int width, int height) {
		Bitmap bm = null;
		try {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			bm = BitmapFactory.decodeFile(src, opts);
			int xScale = opts.outWidth / width;
			int yScale = opts.outHeight / height;
			opts.inSampleSize = xScale > yScale ? xScale : yScale;
			opts.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(src, opts);
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap getBitmap(String src, int SampleSize) {
		Bitmap bm = null;
		Options opts = new Options();
		opts.inSampleSize = SampleSize;
		bm = BitmapFactory.decodeFile(src, opts);
		return bm;
	}

	public static Bitmap getBitmap(File file, int width, int height) {
		Bitmap bm = null;
		bm = getBitmap(file.getAbsolutePath(), width, height);
		return bm;
	}

	public static Bitmap getBitmap(InputStream in) {
		return BitmapFactory.decodeStream(in);
	}

	public static Bitmap getBitmap(File file, int SampleSize) {
		Bitmap bm = null;
		bm = getBitmap(file.getAbsolutePath(), SampleSize);
		return bm;
	}

	public static int computeSampleSize(Options options, int minSideLength,
			int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
