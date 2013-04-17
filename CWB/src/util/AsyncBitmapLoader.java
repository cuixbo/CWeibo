package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import widget.OnCompleteListener;
import net.AsyncImageLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;

public class AsyncBitmapLoader {
	public static LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(4 * 1024 * 1024);

	public void loadBitmap(ImageView view,String url){
		final Bitmap bm=cache.get(url);
		if(bm!=null){
			//���ڴ���ȡbm
			return ;
		}
		String filePath = url.replace("http://", "");
		File file = new File("/mnt/sdcard/downloadimage/" + filePath);
		if(file.exists()){
			//��disk��ȡbm
			return;
		}
		//�������첽����ͼƬ
		new AsyncTask<String, String, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				//��̨�߳�ִ����������
				try {
					URL u = new URL(params[0]);
					Options opt = new Options();
					//opt.inSampleSize = 1;
					Bitmap bm = BitmapFactory.decodeStream(u.openStream(), null, opt);
					return bm;
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Bitmap result) {
				//UI�̴߳����첽���
				if(result!=null){
					//view.setImageBitmap(result);
				}
				super.onPostExecute(result);
			}
			
		}.execute(url);
		
	}

}
