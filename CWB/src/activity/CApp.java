package activity;

import java.util.ArrayList;

import net.HttpUtil;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.inputmethod.InputMethodManager;
import bean.UserBean;
import bean.UserBean;

public class CApp extends Application {

	// public static String ACCESS_TOKEN =
	// "2.00W39JYDsp13hB0d478bf0b93_ucAD";//Ð¡»Æ¸Ç
	public static String ACCESS_TOKEN = "2.00QxLHwB3OyOGD4beabed2d90uyjpd";//
	public static String GET_URI = "?access_token=" + ACCESS_TOKEN;
	public static ArrayList<UserBean> FRIENDS = new ArrayList<UserBean>();
	public static String UID;
	public static InputMethodManager imm;
	public static SharedPreferences shp;
	public static Editor editor;
	public static int USER_PIC_MODE=20;
	public static UserBean local_user;
	@Override
	public void onCreate() {
		shp = getSharedPreferences("configuration", Context.MODE_PRIVATE);
		editor = shp.edit();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initData();
		super.onCreate();
	}

	private void initData() {
		//getMyUid();
	}

	public static void getMyUid() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/account/get_uid.json?access_token=" + CApp.ACCESS_TOKEN;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String JSON = EntityUtils.toString(response.getEntity());
						JSONObject obj = new JSONObject(JSON);
						UID = obj.optString("uid");
						getMyFriends();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void getMyFriends() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String myuri = "https://api.weibo.com/2/friendships/friends.json?access_token=" + CApp.ACCESS_TOKEN + "&count=200&uid=" + UID;
					HttpResponse response = HttpUtil.getResponseByGET(myuri);
					if (response.getStatusLine().getStatusCode() == 200) {
						String JSON = EntityUtils.toString(response.getEntity());
						JSONObject obj1 = new JSONObject(JSON);
						JSONArray users = obj1.getJSONArray("users");
						JSONObject user = null;
						//SQLiteDatabase db = DBUtil.getDBInstance("/mnt/sdcard/CWEIBO/weibo.db");
						for (int i = 0; i < users.length(); i++) {
							user = users.getJSONObject(i);
							UserBean userbean = new UserBean();
							userbean.id = user.optString("id");
							userbean.idstr = user.optString("idstr");
							userbean.screen_name = user.optString("screen_name");
							userbean.name = user.optString("name");
							userbean.province = user.optString("province");
							userbean.city = user.optString("city");
							userbean.location = user.optString("location");
							userbean.description = user.optString("description");
							userbean.url = user.optString("url");
							userbean.profile_image_url = user.optString("profile_image_url");
							userbean.profile_url = user.optString("profile_url");
							userbean.weihao = user.optString("weihao");
							userbean.domain = user.optString("domain");
							userbean.gender = user.optString("gender");
							userbean.followers_count = user.optString("followers_count");
							userbean.friends_count = user.optString("friends_count");
							userbean.statuses_count = user.optString("statuses_count");
							userbean.favourites_count = user.optString("favourites_count");
							userbean.created_at = user.optString("created_at");
							userbean.following = user.optString("following");
							userbean.allow_all_act_msg = user.optString("allow_all_act_msg");
							userbean.remark = user.optString("remark");
							userbean.geo_enabled = user.optString("geo_enabled");
							userbean.verified = user.optString("verified");
							userbean.verified_type = user.optString("verified_type");
							userbean.verified_reason = user.optString("verified_reason");
							userbean.allow_all_comment = user.optString("allow_all_comment");
							userbean.avatar_large = user.optString("avatar_large");
							userbean.follow_me = user.optString("follow_me");
							userbean.online_status = user.optString("online_status");
							userbean.bi_followers_count = user.optString("bi_followers_count");
							userbean.lang = user.optString("lang");
							userbean.star = user.optString("star");
							userbean.mbtype = user.optString("mbtype");
							userbean.mbrank = user.optString("mbrank");
							userbean.block_word = user.optString("block_word");

							// db.execSQL(
							// "insert into users (id,idstr,screen_name,name,province,city,location,description,url,profile_image_url,profile_url,weihao,domain,gender,followers_count,friends_count,statuses_count,favourites_count,created_at,following,allow_all_act_msg,remark,geo_enabled,verified,verified_type,verified_reason,allow_all_comment,avatar_large,follow_me,online_status,bi_followers_count,lang,star,mbtype,mbrank,block_word) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
							// new Object[] { userbean.id, userbean.idstr, userbean.screen_name, userbean.name, userbean.province, userbean.city, userbean.location, userbean.description,
							// userbean.url, userbean.profile_image_url, userbean.profile_url, userbean.weihao, userbean.domain, userbean.gender, userbean.followers_count,
							// userbean.friends_count, userbean.statuses_count, userbean.favourites_count, userbean.created_at, userbean.following, userbean.allow_all_act_msg,
							// userbean.remark, userbean.geo_enabled, userbean.verified, userbean.verified_type, userbean.verified_reason, userbean.allow_all_comment,
							// userbean.avatar_large, userbean.follow_me, userbean.online_status, userbean.bi_followers_count, userbean.lang, userbean.star, userbean.mbtype,
							// userbean.mbrank, userbean.block_word });

							FRIENDS.add(userbean);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
