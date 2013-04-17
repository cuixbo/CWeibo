package util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import Common.CommonConstants;
import activity.CApp;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 * 
 * @(多线程下载)每一个任务都会启动一个新的线程来服务
 * 
 * @author xiaobocui
 * 
 */
public class ImageLoadUtil {
	public static LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(4 * 1024 * 1024);
	public static Bitmap downloadBitmap(String url) {
		try {
			Bitmap bm = cache.get(url);
			if (bm != null) {
				return bm;
			}
			{// diskcache
				String filePath = url.replace("https://", "").replace("http://", "");
				File file = new File("/mnt/sdcard/downloadimage/" + filePath);
				if (file.exists()) {
					Options opt = new Options();
					//opt.inJustDecodeBounds = true;
					//BitmapFactory.decodeStream(new FileInputStream(file), null, opt);
					
					
					//opt.inSampleSize =1;
					//opt.inSampleSize = computeSampleSize(opt, -1, 256 * 256);
//					opt.inPreferredConfig = Config.ARGB_4444;
//					opt.inJustDecodeBounds = false;
//					opt.inPurgeable = true;
//					opt.inInputShareable = true;
					bm = BitmapFactory.decodeStream(new FileInputStream(file), null, opt);
					if (bm != null) {
						return bm;
					}
				}
			}
			URL u = new URL(url);
			Options opt = new Options();
			//opt.inSampleSize = 1;
			bm = BitmapFactory.decodeStream(u.openStream(), null, opt);
			if (bm != null) {
				// cache.put(url, bm);
				saveToDiskCache(url, u.openStream());
			}
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void updateImageView(ImageView view, String url, Drawable drawable) {
		final Drawable d = drawable;
		final String str = url;
		final ImageView imageview = view;
		new Thread(new Runnable() {

			@Override
			public void run() {
				final Bitmap bm = downloadBitmap(str);
				if (bm != null) {
					imageview.post(new Runnable() {

						@Override
						public void run() {
							if(CApp.USER_PIC_MODE==CommonConstants.USER_PIC_MODE_BG){
								imageview.setBackgroundDrawable(new BitmapDrawable(bm));
							}else {
								imageview.setImageBitmap(bm);
								
							}
							if (d != null) {
								BitmapDrawable bd = (BitmapDrawable) d;
								final Bitmap bm = bd.getBitmap();
								if (bm != null && !bm.isRecycled()) {
									bm.recycle();
								}
							}

						}
					});
				}
			}
		}).start();

	}

	public static void saveToDiskCache(String url, InputStream in) {
		String filePath = url.replace("https://", "").replace("http://", "");
		File file = new File("/mnt/sdcard/downloadimage/" + filePath);
		try {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
			bos.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

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

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

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
	public Bitmap getBitmap(String path,int width,int height){
		Bitmap bm = null;
		if(path!=null){
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			bm = BitmapFactory.decodeFile(path, opts);
			int xScale = opts.outWidth / width;
			int yScale = opts.outHeight / height;
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = xScale>yScale?xScale:yScale;
			bm = BitmapFactory.decodeFile(path, opts);
		}
		
		return bm;
	}
}
