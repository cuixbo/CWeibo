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
import util.ImageLoadUtil;
import util.WeiboUtil;
import Common.CommonConstants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class WeiboDetailActivity extends Activity {
	TextView tv1;
	Button btnReply, btnRepost;
	String count = "100";
	String uri = "https://api.weibo.com/2/comments/show.json?access_token=" + CApp.ACCESS_TOKEN + "&" + "count=" + count;
	String JSON;
	String previous_cursor;
	String next_cursor;
	String hasvisible;
	String total_number;
	final static int WEIBO_LOADED_OK = 100;
	Handler handler;
	ArrayList<StatusBean> comments = new ArrayList<StatusBean>();
	ListView lvComments;
	WeiboCommentListViewAdapter adapter;
	HashMap<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();
	String id, uid;
	ImageView image;
	TextView tvName, tvTime, tvRepost, tvComment, tvContent, tvSource;

	private void initView() {
		lvComments = (ListView) findViewById(R.id.lv1);
		View header = getLayoutInflater().inflate(R.layout.head_view_weibo_detail, null);
		lvComments.addHeaderView(header);
		tv1 = (TextView) findViewById(R.id.tv1);
		btnReply = (Button) findViewById(R.id.btnReply);
		btnRepost = (Button) findViewById(R.id.btnRepost);

		image = (ImageView) header.findViewById(R.id.iv_weibo_detail_image);
		tvName = (TextView) header.findViewById(R.id.tv_weibo_detail_user);
		tvContent = (TextView) header.findViewById(R.id.tv_weibo_detail_text);
		tvTime = (TextView) header.findViewById(R.id.tv_weibo_detail_created_at);
		tvSource = (TextView) header.findViewById(R.id.tv_weibo_detail_source);
		tvRepost = (TextView) header.findViewById(R.id.tv_weibo_detail_reposts_count);
		tvComment = (TextView) header.findViewById(R.id.tv_weibo_detail_comments_count);
	}

	private void initListener() {
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), WeiboUserInfoActivity.class);
				intent.putExtra("uid", uid);
				startActivity(intent);

			}
		});
		btnReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("type", CommonConstants.TYPE_COMMENT);
				startActivity(intent);
			}
		});
		btnRepost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
				intent.putExtra("type", CommonConstants.TYPE_REPOST);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		lvComments.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				Intent intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
				intent.putExtra("type", CommonConstants.TYPE_REPLY_COMMENT);
				intent.putExtra("id", id);
				StatusBean comment = comments.get(pos);
				intent.putExtra("cid", comment.idstr);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_detail);
		id = getIntent().getStringExtra("id");
		uid = getIntent().getStringExtra("uid");
		StatusBean weibo = (StatusBean) getIntent().getSerializableExtra("weibobean");
		initView();
		tvName.setText(weibo.userBean.name);
		tvContent.setText(weibo.text);

		image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(80, 80));
		image.setBackgroundDrawable(new BitmapDrawable(getResources(), ImageLoadUtil.downloadBitmap(weibo.userBean.profile_image_url)));
		
		tvTime.setText(WeiboUtil.parseTime(weibo.created_at));
		tvSource.setText("来自" + weibo.source.substring(weibo.source.indexOf(">") + 1, weibo.source.indexOf("</")));
		tvRepost.setText("转发(" + weibo.reposts_count + ")");
		tvComment.setText("评论(" + weibo.comments_count + ")");
		uri = uri + "&id=" + id;

		// btn1 = (Button) findViewById(R.id.btn1);
		adapter = new WeiboCommentListViewAdapter(comments);
		lvComments.setAdapter(adapter);
		// btn1.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View arg0) {

		// }
		// });
		initListener();
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case WEIBO_LOADED_OK:
					adapter.setListData(comments);
					adapter.notifyDataSetChanged();
					int len = adapter.list.size();
					for (int i = 0; i < len; i++) {
						// adapter.loader.loadImage(adapter.list.get(i).user.profile_image_url, adapter.callback);
					}
					break;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 10, 1, "评论");
		menu.add(1, 20, 2, "转发");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=null;
		switch (item.getItemId()) {
		case 10: 
			intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("type", CommonConstants.TYPE_COMMENT);
			startActivity(intent);
			break;
		case 20:
			intent = new Intent(getApplicationContext(), WeiboSubmitActivity.class);
			intent.putExtra("type", CommonConstants.TYPE_REPOST);
			intent.putExtra("id", id);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread.start()");

				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(uri);
				try {
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == 200) {
						comments = new ArrayList<StatusBean>();
						System.out.println("getStatusCode()==200");
						JSON = EntityUtils.toString(response.getEntity());
						System.out.println("getEntity()-->JSON");
						// System.out.println(JSON);
						// 获取JSON第一层数据
						JSONObject obj1 = new JSONObject(JSON);
						previous_cursor = obj1.optString("previous_cursor");
						next_cursor = obj1.optString("next_cursor");
						hasvisible = obj1.optString("hasvisible");
						total_number = obj1.optString("total_number");
						JSONArray arrs1 = obj1.getJSONArray("comments");
						// 获取JSON第二层数据(statuses)数组内容
						JSONObject obj2 = null;
						for (int i = 0; i < arrs1.length(); i++) {
							obj2 = arrs1.getJSONObject(i);
							StatusBean pwbean = new StatusBean();
							pwbean.created_at = obj2.optString("created_at");
							pwbean.id = obj2.optString("id");
							final String text = obj2.optString("text");
							pwbean.text = obj2.optString("text");
							pwbean.mid = obj2.optString("mid");
							pwbean.source = obj2.optString("source");
							pwbean.idstr = obj2.optString("idstr");

							// 获取JSON第三层数据(statuses->user中数据)
							JSONObject obj3 = obj2.getJSONObject("user");
							pwbean.userBean = new UserBean();
							pwbean.userBean.id = obj3.optString("id");
							pwbean.userBean.idstr = obj3.optString("idstr");
							pwbean.userBean.screen_name = obj3.optString("screen_name");
							pwbean.userBean.name = obj3.optString("name");
							pwbean.userBean.province = obj3.optString("province");
							pwbean.userBean.city = obj3.optString("city");
							pwbean.userBean.location = obj3.optString("location");
							pwbean.userBean.description = obj3.optString("description");
							pwbean.userBean.url = obj3.optString("url");
							pwbean.userBean.profile_image_url = obj3.optString("profile_image_url");
							pwbean.userBean.profile_url = obj3.optString("profile_url");
							pwbean.userBean.weihao = obj3.optString("weihao");
							pwbean.userBean.domain = obj3.optString("domain");
							pwbean.userBean.gender = obj3.optString("gender");
							pwbean.userBean.followers_count = obj3.optString("followers_count");
							pwbean.userBean.friends_count = obj3.optString("friends_count");
							pwbean.userBean.statuses_count = obj3.optString("statuses_count");
							pwbean.userBean.favourites_count = obj3.optString("favourites_count");
							pwbean.userBean.created_at = obj3.optString("created_at");
							pwbean.userBean.following = obj3.optString("following");
							pwbean.userBean.allow_all_act_msg = obj3.optString("allow_all_act_msg");
							pwbean.userBean.remark = obj3.optString("remark");
							pwbean.userBean.geo_enabled = obj3.optString("geo_enabled");
							pwbean.userBean.verified = obj3.optString("verified");
							pwbean.userBean.verified_type = obj3.optString("verified_type");
							pwbean.userBean.verified_reason = obj3.optString("verified_reason");
							pwbean.userBean.allow_all_comment = obj3.optString("allow_all_comment");
							pwbean.userBean.avatar_large = obj3.optString("avatar_large");
							pwbean.userBean.follow_me = obj3.optString("follow_me");
							pwbean.userBean.online_status = obj3.optString("online_status");
							pwbean.userBean.bi_followers_count = obj3.optString("bi_followers_count");
							pwbean.userBean.lang = obj3.optString("lang");
							pwbean.userBean.star = obj3.optString("star");
							pwbean.userBean.mbtype = obj3.optString("mbtype");
							pwbean.userBean.mbrank = obj3.optString("mbrank");
							pwbean.userBean.block_word = obj3.optString("block_word");
							comments.add(pwbean);
						}
						handler.sendEmptyMessage(WEIBO_LOADED_OK);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Thread.end()");
			}
		}).start();
		super.onResume();
	}

	public class WeiboCommentListViewAdapter extends BaseAdapter {
		ArrayList<StatusBean> list;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		AsyncImageLoader loader = new AsyncImageLoader();
		public ImageLoadedCallback callback = new ImageLoadedCallback() {

			@Override
			public void imageLoaded(String urlPath, Bitmap bm) {
				ImageView iv = (ImageView) lvComments.findViewWithTag(urlPath);
				if (iv != null)
					// iv.setImageBitmap(bm);
					iv.setBackgroundDrawable(new BitmapDrawable(getResources(), bm));

			}
		};

		public WeiboCommentListViewAdapter(ArrayList<StatusBean> list) {
			this.list = list;
		}

		public void setListData(ArrayList<StatusBean> list) {
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
				convertView = inflater.inflate(R.layout.list_item_weibo_comment, null);
				holder.text = (TextView) convertView.findViewById(R.id.tv_comment_content);
				holder.user_name = (TextView) convertView.findViewById(R.id.tv_comment_user);
				holder.created_at = (TextView) convertView.findViewById(R.id.tv_comment_time);
				holder.ivProfileImage = (ImageView) convertView.findViewById(R.id.tv_comment_image);
				holder.source = (TextView) convertView.findViewById(R.id.tv_public_weibo_source);
				holder.comments_count = (TextView) convertView.findViewById(R.id.tv_public_weibo_comments_count);
				holder.reposts_count = (TextView) convertView.findViewById(R.id.tv_public_weibo_reposts_count);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//holder.ivProfileImage.setImageBitmap(null);
			StatusBean statusBean = list.get(position);
			holder.user_name.setText(statusBean.userBean.name);
			holder.user_name.setTextColor(Color.BLACK);
			holder.user_name.setTextSize(14);
			holder.text.setText(statusBean.text);
			holder.created_at.setText(WeiboUtil.parseTime(statusBean.created_at));
			String source = statusBean.source.substring(statusBean.source.indexOf(">") + 1, statusBean.source.indexOf("</"));
			// holder.source.setText("来自" + source);
			// holder.reposts_count.setText("转发(" + weiboBean.reposts_count + ")");
			// holder.comments_count.setText("评论(" + weiboBean.comments_count + ")");

			holder.ivProfileImage.setTag(statusBean.userBean.profile_image_url);
			Bitmap bm = loader.loadImage(statusBean.userBean.profile_image_url, callback);
			if (bm != null)
				// holder.ivProfileImage.setImageBitmap(bm);
				holder.ivProfileImage.setBackgroundDrawable(new BitmapDrawable(getResources(), bm));
			// if (bitmaps.containsKey(weiboBean.user.id)) {
			// holder.ivProfileImage.setImageBitmap(bitmaps.get(weiboBean.user.id));
			// } else {
			// holder.task = new ImageLoadAsyncTask(holder.ivProfileImage, bitmaps, weiboBean);
			// holder.task.setList(lvPublicWeibo);
			// holder.task.execute(weiboBean.user.profile_image_url);
			// }
			return convertView;
		}

		public class ViewHolder {
			TextView user_name, text, created_at, source, reposts_count, comments_count;
			ImageView ivProfileImage;
			ImageLoadAsyncTask task;
		}
	}
}
