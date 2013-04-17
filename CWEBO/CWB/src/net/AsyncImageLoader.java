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
 * �����ר����������ͼƬ�����ؼ�����֪ͨ���½���,��������Ҫһ���߳̾Ϳ�������ͼƬ
 * 
 * @author xiaobocui
 * 
 */
public class AsyncImageLoader {

	public HashMap<String, Bitmap> imageBitmaps;// ͼƬmap��
	public Thread loadThread;// ����ͼƬ���߳�
	public ArrayList<ImageLoadTask> tasks;// ͼƬ���ص��������񼯺�
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
	 * ����ͼƬ��������,��ͼƬ�������callback���ݹ���
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
	 * ��Ӧÿһ��ͼƬ��������
	 * 
	 * @author xiaobocui
	 * 
	 */
	public class ImageLoadTask {
		public ImageLoadTask(String urlPath, ImageLoadedCallback callback) {
			this.urlPath = urlPath;
			this.callback = callback;
		}

		public String urlPath;// ���ص�ַ
		public Bitmap bm;// �������ͼƬ;
		public ImageLoadedCallback callback;// ������ɺ�Ļص�

		@Override
		// Ϊ��ȷ��ͬһ����ַ��task������ڶ��,Ҫ��дequal����
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
		// // Ϊ��ȷ��ͬһ����ַ��task������ڶ��,Ҫ��дequal����
		// public boolean equals(Object obj) {
		// ImageLoadTask other = (ImageLoadTask) obj;
		// return urlPath.equals(other.urlPath);
		// }
	}

	/**
	 * ��Ӧÿ��ͼƬ������ɺ�Ļص�(���������д���)
	 * 
	 * @author xiaobocui
	 * 
	 */
	public interface ImageLoadedCallback {
		public void imageLoaded(String urlPath, Bitmap bm);
	}

}
