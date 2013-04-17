package activity;

import net.HttpUtil;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import bean.UserBean;

import com.cuixbo.cweibo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeiboUserInfoActivity extends Activity {
	ImageView ivImage;
	Button btnBack, btnAddFriend, btnAddFavouriteFriend;
	TextView tvScreenname, tvDescription, tvLocation, tvGender, tvOnline_status, tvStatus, tvFriends, tvFollowers, tvFavourites;
	LinearLayout layoutStatus, layoutFriends, layoutFollowers, layoutFavourites;
	Handler handler;
	String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_user_info);
		uid=getIntent().getStringExtra("uid");
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case 100:
					Toast.makeText(getApplicationContext(), "request OK!", Toast.LENGTH_SHORT).show();
					tvScreenname.setText(userBean.screen_name);
					tvDescription.setText(userBean.description);
					tvLocation.setText(userBean.location);
					tvGender.setText(userBean.gender);
					tvOnline_status.setText(userBean.online_status);
					tvFriends.setText(userBean.friends_count);
					tvStatus.setText(userBean.statuses_count);
					tvFollowers.setText(userBean.followers_count);
					tvFavourites.setText(userBean.favourites_count);
					break;
				}
				return false;
			}
		});
		initView();
		initData();
		initListener();
	}

	private void initView() {
		/* ImageView */
		ivImage = (ImageView) findViewById(R.id.ivImage);

		/* Button */
		btnBack = (Button) findViewById(R.id.btnBack);
		btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
		btnAddFavouriteFriend = (Button) findViewById(R.id.btnAddFavouriteFriend);

		/* TextView */
		tvScreenname = (TextView) findViewById(R.id.tvScreenname);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvOnline_status = (TextView) findViewById(R.id.tvOnline_status);
		tvStatus = (TextView) findViewById(R.id.tvStatuses);
		tvFriends = (TextView) findViewById(R.id.tvFriends);
		tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		tvFavourites = (TextView) findViewById(R.id.tvFavourites);

		/* Layout */
		layoutStatus = (LinearLayout) findViewById(R.id.layoutStatuses);
		layoutFriends = (LinearLayout) findViewById(R.id.layoutFriends);
		layoutFollowers = (LinearLayout) findViewById(R.id.layoutFollowers);
		layoutFavourites = (LinearLayout) findViewById(R.id.layoutFavourites);
	}

	private void initData() {
		requestUserInfo();
		// TODO Auto-generated method stub

	}

	private void initListener() {
		layoutStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(getApplicationContext(), WeiboHomeActivity.class);
				intent.putExtra("uri", "https://api.weibo.com/2/statuses/user_timeline.json"+CApp.GET_URI+"&uid="+uid);
				startActivity(intent);
				
			}
		});

	}
	UserBean userBean;
	private void requestUserInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/users/show.json" + CApp.GET_URI+"&uid="+uid;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String json =EntityUtils.toString(response.getEntity());
						Gson gson = new Gson();
						userBean = gson.fromJson(json, new TypeToken<UserBean>() {
						}.getType());

						handler.sendEmptyMessage(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void requestFollowers(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/friendships/followers.json" + CApp.GET_URI+"&uid="+uid;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String json =EntityUtils.toString(response.getEntity());
						Gson gson = new Gson();
						userBean = gson.fromJson(json, new TypeToken<UserBean>() {
						}.getType());
						
						handler.sendEmptyMessage(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	private void requestFavourites() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/favorites.json" + CApp.GET_URI+"&uid="+uid;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String json =EntityUtils.toString(response.getEntity());
						Gson gson = new Gson();
						userBean = gson.fromJson(json, new TypeToken<UserBean>() {
						}.getType());
						
						handler.sendEmptyMessage(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
