package activity;

import java.util.ArrayList;
import java.util.HashMap;

import net.AsyncImageLoader;
import net.AsyncImageLoader.ImageLoadedCallback;
import net.HttpProxy;
import net.HttpUtil;
import net.ImageLoadAsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import util.AsyncBitmapLoader;
import util.CommonUtil;
import util.DensityUtil;
import util.ImageLoadUtil;
import util.WeiboUtil;
import widget.RefreshableListView;
import widget.RefreshableListView.OnRefreshListener;
import Common.CommonConstants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.StatusBean;
import bean.UserBean;

import com.cuixbo.cweibo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @allparams(since_id,max_id,count,page,base_app,feature,trim_user)
 * @author xiaobocui
 * @1-每页最多显示20条
 * @2-下拉加载-->最新的20条微薄,清空原有的list
 * @3-底部加载-->小于max_id的20条微薄,加到list后边
 * @4-所有曾经加载过的不做缓存处理
 * @5-当前的20条微薄保存,下次启动时若无网络则显示 上次保存的20条短信
 */
public class WeiboHomeActivity_Pull extends Activity {
	// 存放界面显示给用户的所有微薄
	ArrayList<StatusBean> weibos_all = new ArrayList<StatusBean>();
	// 存放加载的新微薄
	ArrayList<StatusBean> weibos_temp = new ArrayList<StatusBean>();
	 
	LruCache<String, Bitmap> memoryCaches;
	
	TextView tv1, tvUserName;
	Button btnLoad, btnCreate;
	final static int WEIBO_LOADED_OK = 100;
	Handler handler;
	WeiboListViewAdapter adapter;
	HashMap<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

	private ArrayList<String> mItems;
	private RefreshableListView mListView;

	String since_id = "0";
	String max_id = "0";
	String count = "40";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_home_pull);
		initData();
		initView();
		initListener();
		initHandler();
		String[] params = new String[] { "max_id", "count" };
		String[] values = new String[] { max_id, String.valueOf(Integer.parseInt(count) + 1) };
		String uri = CommonUtil.groupUri_WithAccessTokenAndParams(CommonConstants.URI_STATUSES_HOME, params, values);
		new PullToLoadNewDataTask().execute(uri);

	}

	/**
     */
	private void initData() {
		// CApp.getMyUid();
		CApp.getMyFriends();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/users/show.json" + CApp.GET_URI + "&uid=" + CApp.UID;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String json = EntityUtils.toString(response.getEntity());
						Gson gson = new Gson();
						CApp.local_user = gson.fromJson(json, new TypeToken<UserBean>() {
						}.getType());
						handler.sendEmptyMessage(300);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	TextView footView;

	private void initView() {
		tv1 = (TextView) findViewById(R.id.tv1);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		btnLoad = (Button) findViewById(R.id.btnLoad);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		mListView = (RefreshableListView) findViewById(R.id.lv1);
		// add top/footview
		footView = new TextView(this);
		footView.setText("更多");
		footView.setTextColor(Color.WHITE);
		footView.setGravity(Gravity.CENTER);
		footView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getApplication(), 40)));
		footView.setBackgroundColor(Color.parseColor("#ec4a71"));
		mListView.addFooterView(footView);
		adapter = new WeiboListViewAdapter(weibos_all);
		mListView.setAdapter(adapter);
	}

	private void initListener() {
		// listview底部栏-->"更多"-->加载更多微薄
		footView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] params = new String[] { "max_id", "count" };
				String[] values = new String[] { max_id, String.valueOf(Integer.parseInt(count) + 1) };
				String uri = CommonUtil.groupUri_WithAccessTokenAndParams(CommonConstants.URI_STATUSES_HOME, params, values);
				new LoadMoreDataTask().execute(uri);
			}
		});

		// Callback to refresh the list
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(RefreshableListView listView) {
				String[] params = new String[] { "since_id", "count" };
				String[] values = new String[] { since_id, count };
				String uri = CommonUtil.groupUri_WithAccessTokenAndParams(CommonConstants.URI_STATUSES_HOME, params, values);
				new PullToLoadNewDataTask().execute(uri);
			}

		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), WeiboDetailActivity.class);
				StatusBean weibo = weibos_all.get(position);
				intent.putExtra("id", weibo.idstr);
				intent.putExtra("uid", weibo.userBean.idstr);
				intent.putExtra("weibobean", weibo);
				startActivity(intent);
			}
		});
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
				intent.putExtra("type", CommonConstants.TYPE_CREATE);
				startActivity(intent);
			}
		});
		btnLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	private void initHandler() {
		
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case 12:
					adapter.setList(weibos_all);
					adapter.notifyDataSetChanged();
					int len = adapter.list.size();
					for (int i = 0; i < len; i++) {
						// adapter.loader.loadImage(adapter.list.get(i).user.profile_image_url, adapter.callback);
					}
					break;
				case 300:
					tvUserName.setText(CApp.local_user.name);
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 显示微薄的adapter
	 * 
	 * @author xiaobocui
	 * 
	 */
	public class WeiboListViewAdapter extends BaseAdapter {
		ArrayList<StatusBean> list;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		AsyncImageLoader loader = new AsyncImageLoader();
		public ImageLoadedCallback callback = new ImageLoadedCallback() {

			@Override
			public void imageLoaded(String urlPath, Bitmap bm) {
				ImageView iv = (ImageView) mListView.findViewWithTag(urlPath);
				if (iv != null) {
					if (CApp.USER_PIC_MODE == CommonConstants.USER_PIC_MODE_BG) {
						iv.setBackgroundDrawable(new BitmapDrawable(bm));
					} else {
						iv.setImageBitmap(bm);
					}

				}

			}
		};

		public WeiboListViewAdapter(ArrayList<StatusBean> list) {
			this.list = list;
		}

		public void setList(ArrayList<StatusBean> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			
			return list.get(arg0).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_item_weibo, null);
				holder.text = (TextView) convertView.findViewById(R.id.tvStatus);
				holder.user_name = (TextView) convertView.findViewById(R.id.tvName);
				holder.created_at = (TextView) convertView.findViewById(R.id.tvTime);
				holder.source = (TextView) convertView.findViewById(R.id.tvSource);
				holder.comments_count = (TextView) convertView.findViewById(R.id.tvCommentsCount);
				holder.reposts_count = (TextView) convertView.findViewById(R.id.tvRepostsCount);
				holder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivPic);
				holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
				holder.layout = (LinearLayout) convertView.findViewById(R.id.layoutRetweet);
				holder.ivProfileImage.setLayoutParams(new android.widget.LinearLayout.LayoutParams(50, 50));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.ivProfileImage.setImageResource(R.drawable.default_image);
			holder.ivProfileImage.setBackgroundResource(R.drawable.pic_default);
			StatusBean statusBean = list.get(position);
			holder.user_name.setText(statusBean.userBean.name);
			holder.user_name.setTextColor(Color.BLACK);
			holder.user_name.setTextSize(16);
			// holder.text.setText(weiboBean.text);

			// Spanned spanned = Html.fromHtml(weiboBean.text);
			// SpannableStringBuilder spannable = new SpannableStringBuilder(spanned);
			SpannableStringBuilder spannable = WeiboUtil.parseLink(statusBean.text);
			if (spannable != null) {
				holder.text.setText(spannable);
			}
			// holder.text.setText(weiboBean.text);
			// holder.text.setText("44"+Html.fromHtml("http://hkd.fom")+"dsf");
			holder.created_at.setText(WeiboUtil.parseTime(statusBean.created_at));
			String source = statusBean.source.substring(statusBean.source.indexOf(">") + 1, statusBean.source.indexOf("</"));
			holder.source.setText("来自" + source);
			holder.reposts_count.setText("转发(" + statusBean.reposts_count + ")");
			holder.comments_count.setText("评论(" + statusBean.comments_count + ")");

			holder.ivProfileImage.setTag(statusBean.userBean.profile_image_url);
			//ImageLoadUtil.updateImageView(holder.ivProfileImage, statusBean.userBean.profile_image_url, null);
			AsyncBitmapLoader.loadBitmap(holder.ivProfileImage, statusBean.userBean.profile_image_url);
			
			
			// Bitmap bm = loader.loadImage(weiboBean.user.profile_image_url, callback);
			// if (bm != null){
			// if(CApp.USER_PIC_MODE==CommonConstants.USER_PIC_MODE_BG){
			// holder.ivProfileImage.setBackgroundDrawable(new BitmapDrawable(bm));
			// }else {
			// holder.ivProfileImage.setImageBitmap(bm);
			// }
			// }
			if(statusBean.thumbnail_pic!=null){
				holder.ivImage.setVisibility(View.VISIBLE);
				//ImageLoadUtil.updateImageView(holder.ivImage, statusBean.thumbnail_pic, null);
			}else {
				holder.ivImage.setVisibility(View.GONE);
			}
			if(statusBean.original_pic!=null&&!"".equals(statusBean.original_pic)){
				holder.ivImage.setVisibility(View.VISIBLE);
				holder.ivImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
					}
				});
				//ImageLoadUtil.updateImageView(holder.ivImage, weiboBean.original_pic, null);
			}else {
				holder.ivImage.setVisibility(View.GONE);
			}
			
			if(statusBean.retweeted_status!=null){
				holder.layout.removeAllViewsInLayout();
				View retweetView = inflater.inflate(R.layout.list_item_weibo, null);
				TextView text = (TextView) retweetView.findViewById(R.id.tvStatus);
				TextView user_name = (TextView) retweetView.findViewById(R.id.tvName);
				TextView created_at = (TextView) retweetView.findViewById(R.id.tvTime);
				TextView sourceView = (TextView) retweetView.findViewById(R.id.tvSource);
				TextView comments_count = (TextView) retweetView.findViewById(R.id.tvCommentsCount);
				TextView reposts_count = (TextView) retweetView.findViewById(R.id.tvRepostsCount);
				ImageView ivProfileImage = (ImageView) retweetView.findViewById(R.id.ivPic);
				ImageView ivImage = (ImageView) retweetView.findViewById(R.id.ivImage);
				//holder.layout = (LinearLayout) retweetView.findViewById(R.id.layoutRetweet);
				//holder.ivProfileImage.setLayoutParams(new android.widget.LinearLayout.LayoutParams(50, 50));
				ivProfileImage.setVisibility(View.GONE);
				holder.layout.addView(retweetView);
				user_name.setText(statusBean.retweeted_status.userBean.name);
//				user_name.setTextColor(Color.BLACK);
//				user_name.setTextSize(16);
				SpannableStringBuilder spannable2 = WeiboUtil.parseLink(statusBean.retweeted_status.text);
				if (spannable2 != null) {
					text.setText(spannable2);
				}
				created_at.setText(WeiboUtil.parseTime(statusBean.retweeted_status.created_at));
				String source2 = statusBean.retweeted_status.source.substring(statusBean.retweeted_status.source.indexOf(">") + 1, statusBean.retweeted_status.source.indexOf("</"));
				sourceView.setText("来自" + source2);
				reposts_count.setText("转发(" + statusBean.retweeted_status.reposts_count + ")");
				comments_count.setText("评论(" + statusBean.retweeted_status.comments_count + ")");

				//holder.ivProfileImage.setTag(weiboBean.user.profile_image_url);
				holder.layout.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}

		public class ViewHolder {
			TextView user_name, text, created_at, source, reposts_count, comments_count;
			ImageView ivProfileImage,ivImage;
			LinearLayout layout;
			ImageLoadAsyncTask task;
		}
	}

	/**
	 * 下拉刷新异步任务类
	 * 
	 * @author xiaobocui
	 * 
	 */
	private class PullToLoadNewDataTask extends AsyncTask<String, Integer, Integer> {
		int TASK_POST_OK = 200;

		@Override
		protected Integer doInBackground(String... param) {

			HttpGet request = new HttpGet(param[0]);
			weibos_temp = HttpProxy.getStatusesHome(request, handler);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TASK_POST_OK;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == TASK_POST_OK && weibos_temp != null) {
				weibos_all = weibos_temp;
				weibos_temp = new ArrayList<StatusBean>();
				max_id = weibos_all.get(weibos_all.size() - 1).idstr;
				adapter.setList(weibos_all);
				adapter.notifyDataSetChanged();
			}
			// This should be called after refreshing finished
			mListView.completeRefreshing();
			super.onPostExecute(result);
		}
	}

	/**
	 * 主动加载更多的异步任务类
	 * 
	 * @author xiaobocui
	 * 
	 */
	private class LoadMoreDataTask extends AsyncTask<String, Integer, Integer> {
		int TASK_POST_OK = 200;

		@Override
		protected Integer doInBackground(String... param) {

			HttpGet request = new HttpGet(param[0]);
			weibos_temp = HttpProxy.getStatusesHome(request, handler);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TASK_POST_OK;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == TASK_POST_OK && weibos_temp != null) {
				weibos_temp.remove(0);
				weibos_all.addAll(weibos_temp);
				weibos_temp = new ArrayList<StatusBean>();
				max_id = weibos_all.get(weibos_all.size() - 1).idstr;
				adapter.setList(weibos_all);
				adapter.notifyDataSetChanged();
			}
			// This should be called after refreshing finished
			mListView.completeRefreshing();
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 100, 1, "注销");
		// menu.add(1, 3, 3, "item 3");
		// menu.add(1, 4, 4, "item 4");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			CApp.editor.putString("access_token", "");
			CApp.editor.commit();
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
