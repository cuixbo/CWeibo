package net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import bean.StatusBean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageLoadAsyncTask extends AsyncTask<String, String, String> {
    ImageView view;
    Bitmap bm;
    HashMap<String, Bitmap> bitmaps;
    StatusBean status;
    ListView list;
    String path;
    public ImageLoadAsyncTask(ImageView view,HashMap<String, Bitmap> bitmaps,StatusBean status) {
        this.view = view;
        this.bitmaps=bitmaps;
        this.status=status;
    }
    public void setList(ListView list){
        this.list=list;
    }
    

    @Override
    protected String doInBackground(String... params) {
        try {
            path=params[0];
            URL url = new URL(path);
            // URLConnection con=url.openStream();
            InputStream in = url.openStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            bm = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(status.userBean.name+"Õº∆¨œ¬‘ÿÕÍ±œ");
        if((ImageView)list.findViewWithTag(path)!=null){
            view.setImageBitmap(bm);
        }
        bitmaps.put(status.userBean.id, bm);
        super.onPostExecute(result);
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
