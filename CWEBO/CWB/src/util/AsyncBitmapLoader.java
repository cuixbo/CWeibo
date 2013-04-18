package util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class AsyncBitmapLoader {
	public static LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(
			4 * 1024 * 1024);
	public static String imageCacheDir = "/mnt/sdcard/downloadimage/";

	public static void loadBitmap(ImageView view, String url) {
		// 1-从内存中取bm
		Bitmap bm = getFromMemoryCache(url);
		if (bm != null) {
			BitmapDrawable bd = (BitmapDrawable) view.getDrawable();
			view.setBackgroundDrawable(new BitmapDrawable(bm));
			//view.setImageBitmap(bm);
			recycleOldBitmap(bd);
			return;
		}
		// 2-从disk中取bm
		bm = getFromDiskCache(url);
		if (bm != null) {
			BitmapDrawable bd = (BitmapDrawable) view.getDrawable();
			view.setBackgroundDrawable(new BitmapDrawable(bm));
			//view.setImageBitmap(bm);
			recycleOldBitmap(bd);
			return;
		}
		// 3-从网络异步下载bm
		
		new AsyncBitmapLoadTask(view, url).execute(url);

	}

	/**
	 * 通过url访问网络下载图片并加载到imageview,且回收imageview之前的bitmap
	 * 
	 * @author xiaobocui
	 * 
	 */
	public static class AsyncBitmapLoadTask extends AsyncTask<String, String, Bitmap> {
		ImageView view;
		String url;

		public AsyncBitmapLoadTask(ImageView view, String url) {
			//System.out.println("AsyncBitmapLoadTask");
			this.view = view;
			this.url = url;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// 后台线程执行网络请求
			try {
				URL u = new URL(params[0]);
				Options opt = new Options();
				// opt.inSampleSize = 1;
				addToDiskCache(params[0], u.openStream());
				// Bitmap bm = BitmapFactory.decodeStream(u.openStream(), null,
				// opt);
				Bitmap bm = getFromDiskCache(url);
				return bm;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// UI线程处理异步结果
			if (result != null) {
				BitmapDrawable bd = (BitmapDrawable) view.getDrawable();
				//view.setImageBitmap(result);
				view.setBackgroundDrawable(new BitmapDrawable(result));
				recycleOldBitmap(bd);// 对之前的图片就行回收
			}
			super.onPostExecute(result);
		}
	}

	public static  void addToMemoryCache(String url, Bitmap bm) {
		memoryCache.put(url, bm);
	}

	public static void addToDiskCache(String url, InputStream in) {
		try {
			File file = new File(imageCacheDir + url.replace("http://", ""));
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
			bos.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getFromMemoryCache(String url) {
		return memoryCache.get(url);
	}

	public static Bitmap getFromDiskCache(String url) {
		Bitmap bm = null;
		try {
			File file = new File(imageCacheDir + url.replace("http://", ""));
			if (file.exists()) {
				bm = BitmapUtil.getBitmap(file, 256, 256);
				if (bm != null) {
					return bm;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static void recycleOldBitmap(BitmapDrawable bd) {
		if (bd != null) {
			Bitmap bm = bd.getBitmap();
			if (bm != null&&!bm.isRecycled()) {
				//bm.recycle();
				//System.gc();
			}
		}
	}

}
