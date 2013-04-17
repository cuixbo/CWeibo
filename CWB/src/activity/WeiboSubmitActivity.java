package activity;

import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.HttpUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import Common.CommonConstants;
import activity.WeiboSubmitActivity.WeiboFriendsListViewAdapter.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.UserBean;

import com.cuixbo.cweibo.R;

public class WeiboSubmitActivity extends Activity {
	String count = "100";
	String uri = "https://api.weibo.com/2/comments/show.json?access_token=" + CApp.ACCESS_TOKEN + "&" + "count=" + count;
	Button btnSubmit, btnAt,btnOk;
	ImageView image;
	TextView tvTitle, tvName, tvTime, tvRepost, tvComment, tvContent, tvSource;
	EditText etText;
	String id,cid;
	int type;
	ListView lvFriends;
	String content;
	Handler handler;
	WeiboFriendsListViewAdapter adapter;
	ArrayList<UserBean> selectedFriends = new ArrayList<UserBean>();
	final public static int WEIBO_SUBMIT_OK = 100;

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		etText = (EditText) findViewById(R.id.etText);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnAt = (Button) findViewById(R.id.btnAt);
		btnOk = (Button) findViewById(R.id.btnOk);
		lvFriends = (ListView) findViewById(R.id.lvFriends);
		btnOk.setText("OK");
		switch (type) {
		case CommonConstants.TYPE_CREATE:
			tvTitle.setText("发表微博");
			break;
		case CommonConstants.TYPE_COMMENT:
			tvTitle.setText("评论");
			break;
		case CommonConstants.TYPE_REPOST:
			tvTitle.setText("转发微博");
			break;
		case CommonConstants.TYPE_REPLY_COMMENT:
			tvTitle.setText("回复评论");
			break;

		default:
			tvTitle.setText("未定义");
			break;
		}
	}

	private void initListener() {
		adapter = new WeiboFriendsListViewAdapter(CApp.FRIENDS, R.layout.list_item_textview);
		lvFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
				UserBean userBean=CApp.FRIENDS.get(pos);
				ViewHolder holder=(ViewHolder)v.getTag();
				if(selectedFriends.contains(userBean)){
					selectedFriends.remove(userBean);
					holder.tvSelected.setVisibility(View.INVISIBLE);
				}else {
					selectedFriends.add(userBean);
					holder.tvSelected.setVisibility(View.VISIBLE);
				}
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text=etText.getText().toString();
				for (UserBean userBean : selectedFriends) {
					text+=" @"+userBean.name;
				}
				etText.setText(text);
				btnOk.setVisibility(View.INVISIBLE);
				selectedFriends=new ArrayList<UserBean>();
				lvFriends.setVisibility(View.INVISIBLE);
				CApp.imm.showSoftInput(getCurrentFocus(), 0);
			}
		});
		btnAt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lvFriends.setVisibility(View.VISIBLE);
				lvFriends.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				btnOk.setVisibility(View.VISIBLE);
				InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			
			
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				content = etText.getText().toString().trim();
				if (content == null || "".equals(content)) {
					return;
				}
				switch (type) {
				case CommonConstants.TYPE_CREATE:
					doCreate();
					break;
				case CommonConstants.TYPE_COMMENT:
					doComment();
					break;
				case CommonConstants.TYPE_REPOST:
					doRepost();
					break;
				case CommonConstants.TYPE_REPLY_COMMENT:
					doReplyComment();
					break;
				default:
					break;
				}

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_submit);
		id = getIntent().getStringExtra("id");
		cid = getIntent().getStringExtra("cid");
		type = getIntent().getIntExtra("type", 0);

		initView();
		initListener();
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case WEIBO_SUBMIT_OK:
					Toast.makeText(getApplicationContext(), "Submit OK!", Toast.LENGTH_SHORT).show();
					WeiboSubmitActivity.this.finish();
					break;
				}
				return false;
			}
		});
	}

	void doComment() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/comments/create.json?access_token=" + CApp.ACCESS_TOKEN + "&id=" + id + "&comment=" + URLEncoder.encode(content, "UTF-8");

					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(myuri);
					List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
					parameters.add(new BasicNameValuePair("access_token", CApp.ACCESS_TOKEN));
					parameters.add(new BasicNameValuePair("id", id));
					parameters.add(new BasicNameValuePair("comment", content));
					request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
					HttpResponse response = client.execute(request);
					if (response.getStatusLine().getStatusCode() == 200) {
						handler.sendEmptyMessage(100);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	void doCreate() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/statuses/update.json";
					HttpResponse response = HttpUtil.getResponseByPOST(myuri, new BasicNameValuePair("access_token", CApp.ACCESS_TOKEN), new BasicNameValuePair("status", content));
					if (response.getStatusLine().getStatusCode() == 200) {
						handler.sendEmptyMessage(100);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	void doRepost() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/statuses/repost.json";
					HttpResponse response = HttpUtil.getResponseByPOST(myuri, new BasicNameValuePair("access_token", CApp.ACCESS_TOKEN), new BasicNameValuePair("id", id),new BasicNameValuePair("status", content));
					if (response.getStatusLine().getStatusCode() == 200) {
						handler.sendEmptyMessage(100);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
	void doReplyComment() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/comments/reply.json";
					HttpResponse response = HttpUtil.getResponseByPOST(myuri, new BasicNameValuePair("access_token", CApp.ACCESS_TOKEN), new BasicNameValuePair("id", id),new BasicNameValuePair("cid", cid),new BasicNameValuePair("status", content));
					if (response.getStatusLine().getStatusCode() == 200) {
						handler.sendEmptyMessage(100);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public class WeiboFriendsListViewAdapter extends BaseAdapter {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ArrayList<UserBean> list;
		int resId;

		public WeiboFriendsListViewAdapter(ArrayList<UserBean> list, int resId) {
			this.list = list;
			this.resId = resId;
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
				convertView = inflater.inflate(resId, null);
				holder.tvText = (TextView) convertView.findViewById(R.id.tvText);
				holder.tvSelected = (TextView) convertView.findViewById(R.id.tvSelected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			UserBean userBean = list.get(position);
			holder.tvText.setText(userBean.name);
			return convertView;
		}
		public class ViewHolder {
			TextView tvText,tvSelected;
		}
	}
}
