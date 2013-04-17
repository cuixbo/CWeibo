package net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * 这个类专门用来管理图片的下载及下载通知更新界面,这样仅需要一个线程就可以下载图片
 * 
 * @author xiaobocui
 * 
 */
public class AsyncImageLoader {

	public HashMap<String, Bitmap> imageBitmaps;// 图片map表
	public Thread loadThread;// 下载图片的线程
	public ArrayList<ImageLoadTask> tasks;// 图片下载的所有任务集合
	public Handler handler;

	public AsyncImageLoader() {
		this.imageBitmaps = new HashMap<String, Bitmap>();
		this.tasks = new ArrayList<ImageLoadTask>();
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case 100:
					ImageLoadTask task = (ImageLoadTask) msg.obj;
					task.callback.imageLoaded(task.urlPath, task.bm);
					break;
				}
				return false;
			}
		});
		loadThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					while (tasks.size() > 0) {
						ImageLoadTask task = tasks.remove(0);
						try {
							InputStream in = new URL(task.urlPath).openStream();
							BufferedInputStream bis = new BufferedInputStream(in);
							task.bm = BitmapFactory.decodeStream(bis);
							bis.close();
							in.close();
							imageBitmaps.put(task.urlPath, task.bm);
							Message msg = Message.obtain();
							msg.what = 100;
							msg.obj = task;
							handler.sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					synchronized (loadThread) {
						try {
							loadThread.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		loadThread.start();
	}

	/**
	 * 加载图片下载任务,将图片下载完的callback传递过来
	 */
	public Bitmap loadImage(String urlPath, ImageLoadedCallback callback) {
		Bitmap bm;
		if (imageBitmaps.containsKey(urlPath)) {
			bm = imageBitmaps.get(urlPath);
			if (bm != null)
				return bm;
			else
				imageBitmaps.remove(urlPath);
		}

		ImageLoadTask task = new ImageLoadTask(urlPath, callback);
		if (!tasks.contains(task)) {
			tasks.add(task);
			if (loadThread.getState() == Thread.State.WAITING) {
				synchronized (loadThread) {
					loadThread.notify();
				}
			}
		}
		return null;
	}

	/**
	 * 对应每一个图片下载任务
	 * 
	 * @author xiaobocui
	 * 
	 */
	public class ImageLoadTask {
		public ImageLoadTask(String urlPath, ImageLoadedCallback callback) {
			this.urlPath = urlPath;
			this.callback = callback;
		}

		public String urlPath;// 下载地址
		public Bitmap bm;// 下载完的图片;
		public ImageLoadedCallback callback;// 下载完成后的回调

		@Override
		// 为了确保同一个地址的task不会存在多个,要重写equal方法
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ImageLoadTask other = (ImageLoadTask) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (urlPath == null) {
				if (other.urlPath != null)
					return false;
			} else if (!urlPath.equals(other.urlPath))
				return false;
			return true;
		}
		private AsyncImageLoader getOuterType() {
			return AsyncImageLoader.this;
		}
		// @Override
		// // 为了确保同一个地址的task不会存在多个,要重写equal方法
		// public boolean equals(Object obj) {
		// ImageLoadTask other = (ImageLoadTask) obj;
		// return urlPath.equals(other.urlPath);
		// }
	}

	/**
	 * 对应每个图片下载完成后的回调(将变量进行传递)
	 * 
	 * @author xiaobocui
	 * 
	 */
	public interface ImageLoadedCallback {
		public void imageLoaded(String urlPath, Bitmap bm);
	}

}
