package activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import net.AsyncImageLoader;
import net.AsyncImageLoader.ImageLoadedCallback;
import net.ImageLoadAsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import util.CommonUtil;
import Common.CommonConstants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import bean.UserBean;
import bean.StatusBean;

import com.cuixbo.cweibo.R;

public class WeiboHomeActivity extends Activity {
    TextView tv1;
    Button btnLoad,btnCreate;
    String count = "20";
    String uri = "https://api.weibo.com/2/statuses/friends_timeline.json?access_token=" + CApp.ACCESS_TOKEN + "&" + "count=" + count;
    String JSON;
    String previous_cursor;
    String next_cursor;
    String hasvisible;
    String total_number;
    final static int WEIBO_LOADED_OK = 100;
    Handler handler;
    ArrayList<StatusBean> weibos = new ArrayList<StatusBean>();
    ListView lvPublicWeibo;
    PublicWeiboListViewAdapter adapter;
    HashMap<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_home);
        String str=getIntent().getStringExtra("uri");
        if(str!=null&&!"".equals(str)){
        	uri=str;
        }
        tv1 = (TextView) findViewById(R.id.tv1);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        lvPublicWeibo = (ListView) findViewById(R.id.lv1);
        adapter = new PublicWeiboListViewAdapter(weibos);
        lvPublicWeibo.setAdapter(adapter);
        lvPublicWeibo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Intent intent=new Intent(getApplicationContext(),WeiboDetailActivity.class);
				StatusBean weibo=weibos.get(position);
				intent.putExtra("id", weibo.idstr);
				intent.putExtra("uid", weibo.userBean.idstr);
				intent.putExtra("weibobean", weibo);
				startActivity(intent);
				
			}
		});
        btnCreate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(), WeiboSubmitActivity.class);
				intent.putExtra("type", CommonConstants.TYPE_CREATE);
				startActivity(intent);
			}
		});
        btnLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet(uri);
                        try {
                            HttpResponse response = client.execute(request);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                JSON = EntityUtils.toString(response.getEntity());
                                // System.out.println(JSON);
                                // 获取JSON第一层数据
                                JSONObject obj1 = new JSONObject(JSON);
                                previous_cursor = obj1.optString("previous_cursor");
                                next_cursor = obj1.optString("next_cursor");
                                hasvisible = obj1.optString("hasvisible");
                                total_number = obj1.optString("total_number");
                                JSONArray arrs = obj1.getJSONArray("statuses");
                                // JSONArray arr = new
                                // JSONObject(JSON).getJSONArray("statuses");
                                // 获取JSON第二层数据(statuses)数组内容
                                JSONObject obj = null;
                                for (int i = 0; i < arrs.length(); i++) {
                                    obj = arrs.getJSONObject(i);
                                    StatusBean pwbean = new StatusBean();
                                    pwbean.created_at = obj.optString("created_at");
                                    pwbean.id = obj.optString("id");
                                    pwbean.mid = obj.optString("mid");
                                    pwbean.idstr = obj.optString("idstr");
                                    final String text = obj.optString("text");
                                    pwbean.text = obj.optString("text");
                                    pwbean.source = obj.optString("source");
                                    pwbean.favorited = obj.optString("favorited");
                                    pwbean.truncated = obj.optString("truncated");
                                    pwbean.in_reply_to_status_id = obj.optString("in_reply_to_status_id");
                                    pwbean.in_reply_to_user_id = obj.optString("in_reply_to_user_id");
                                    pwbean.in_reply_to_screen_name = obj.optString("in_reply_to_screen_name");
                                    pwbean.thumbnail_pic = obj.optString("thumbnail_pic");
                                    pwbean.bmiddle_pic = obj.optString("bmiddle_pic");
                                    pwbean.original_pic = obj.optString("original_pic");
                                    pwbean.geo = obj.optString("geo");
                                    pwbean.reposts_count = obj.optString("reposts_count");
                                    pwbean.comments_count = obj.optString("comments_count");
                                    pwbean.attitudes_count = obj.optString("attitudes_count");
                                    pwbean.mlevel = obj.optString("mlevel");
                                    // 获取JSON第三层数据(statuses->user中数据)
                                    JSONObject obj2 = obj.getJSONObject("user");
                                    pwbean.userBean = new UserBean();
                                    pwbean.userBean.id = obj2.optString("id");
                                    pwbean.userBean.idstr = obj2.optString("idstr");
                                    pwbean.userBean.screen_name = obj2.optString("screen_name");
                                    pwbean.userBean.name = obj2.optString("name");
                                    pwbean.userBean.province = obj2.optString("province");
                                    pwbean.userBean.city = obj2.optString("city");
                                    pwbean.userBean.location = obj2.optString("location");
                                    pwbean.userBean.description = obj2.optString("description");
                                    pwbean.userBean.url = obj2.optString("url");
                                    pwbean.userBean.profile_image_url = obj2.optString("profile_image_url");
                                    pwbean.userBean.profile_url = obj2.optString("profile_url");
                                    pwbean.userBean.weihao = obj2.optString("weihao");
                                    pwbean.userBean.domain = obj2.optString("domain");
                                    pwbean.userBean.gender = obj2.optString("gender");
                                    pwbean.userBean.followers_count = obj2.optString("followers_count");
                                    pwbean.userBean.friends_count = obj2.optString("friends_count");
                                    pwbean.userBean.statuses_count = obj2.optString("statuses_count");
                                    pwbean.userBean.favourites_count = obj2.optString("favourites_count");
                                    pwbean.userBean.created_at = obj2.optString("created_at");
                                    pwbean.userBean.following = obj2.optString("following");
                                    pwbean.userBean.allow_all_act_msg = obj2.optString("allow_all_act_msg");
                                    pwbean.userBean.remark = obj2.optString("remark");
                                    pwbean.userBean.geo_enabled = obj2.optString("geo_enabled");
                                    pwbean.userBean.verified = obj2.optString("verified");
                                    pwbean.userBean.verified_type = obj2.optString("verified_type");
                                    pwbean.userBean.verified_reason = obj2.optString("verified_reason");
                                    pwbean.userBean.allow_all_comment = obj2.optString("allow_all_comment");
                                    pwbean.userBean.avatar_large = obj2.optString("avatar_large");
                                    pwbean.userBean.follow_me = obj2.optString("follow_me");
                                    pwbean.userBean.online_status = obj2.optString("online_status");
                                    pwbean.userBean.bi_followers_count = obj2.optString("bi_followers_count");
                                    pwbean.userBean.lang = obj2.optString("lang");
                                    pwbean.userBean.star = obj2.optString("star");
                                    pwbean.userBean.mbtype = obj2.optString("mbtype");
                                    pwbean.userBean.mbrank = obj2.optString("mbrank");
                                    pwbean.userBean.block_word = obj2.optString("block_word");
                                    weibos.add(pwbean);
                                }
                                handler.sendEmptyMessage(WEIBO_LOADED_OK);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case WEIBO_LOADED_OK:
                    adapter.notifyDataSetChanged();
                    int len=adapter.list.size();
                    for(int i=0;i<len;i++){
                        adapter.loader.loadImage(adapter.list.get(i).userBean.profile_image_url, adapter.callback);
                    }
                    break;
                }
                return false;
            }
        });
    }

    public class PublicWeiboListViewAdapter extends BaseAdapter {
        ArrayList<StatusBean> list;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        AsyncImageLoader loader=new AsyncImageLoader();
        public ImageLoadedCallback callback=new ImageLoadedCallback() {
            
            @Override
            public void imageLoaded(String urlPath, Bitmap bm) {
                ImageView iv=(ImageView)lvPublicWeibo.findViewWithTag(urlPath);
                if(iv!=null)
                    iv.setImageBitmap(bm);
                
            }
        };
        
        public PublicWeiboListViewAdapter(ArrayList<StatusBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            // View view=inflater.inflate(R.layout.list_item_public_weibo,
            // null);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_public_weibo, null);
                holder.text = (TextView) convertView.findViewById(R.id.tv_public_weibo_text);
                holder.user_name = (TextView) convertView.findViewById(R.id.tv_public_weibo_user);
                holder.created_at = (TextView) convertView.findViewById(R.id.tv_public_weibo_created_at);
                holder.source = (TextView) convertView.findViewById(R.id.tv_public_weibo_source);
                holder.comments_count = (TextView) convertView.findViewById(R.id.tv_public_weibo_comments_count);
                holder.reposts_count = (TextView) convertView.findViewById(R.id.tv_public_weibo_reposts_count);
                holder.ivProfileImage = (ImageView) convertView.findViewById(R.id.tv_public_weibo_profile_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ivProfileImage.setImageBitmap(null);
            StatusBean statusBean = list.get(position);
            holder.user_name.setText(statusBean.userBean.name);
            holder.user_name.setTextColor(Color.BLACK);
            holder.user_name.setTextSize(16);
            holder.text.setText(statusBean.text);
            Calendar calendar = CommonUtil.parseTime(statusBean.created_at);
            String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE);
            String hour = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY);
            String second = calendar.get(Calendar.SECOND) < 10 ? "0" + calendar.get(Calendar.SECOND) : "" + calendar.get(Calendar.SECOND);
            String time = hour + ":" + minute + ":" + second;
            holder.created_at.setText(time);
            String source = statusBean.source.substring(statusBean.source.indexOf(">") + 1, statusBean.source.indexOf("</"));
            holder.source.setText("From" + source);
            holder.reposts_count.setText("转发(" + statusBean.reposts_count + ")");
            holder.comments_count.setText("评论(" + statusBean.comments_count + ")");
            
            holder.ivProfileImage.setTag(statusBean.userBean.profile_image_url);
            Bitmap bm=loader.loadImage(statusBean.userBean.profile_image_url, callback);
            if(bm!=null)
                holder.ivProfileImage.setImageBitmap(bm);
//            if (bitmaps.containsKey(weiboBean.user.id)) {
//                holder.ivProfileImage.setImageBitmap(bitmaps.get(weiboBean.user.id));
//            } else {
//                holder.task = new ImageLoadAsyncTask(holder.ivProfileImage, bitmaps, weiboBean);
//                holder.task.setList(lvPublicWeibo);
//                holder.task.execute(weiboBean.user.profile_image_url);
//            }
            return convertView;
        }

        public class ViewHolder {
            TextView user_name, text, created_at, source, reposts_count, comments_count;
            ImageView ivProfileImage;
            ImageLoadAsyncTask task;
        }
    }
}
