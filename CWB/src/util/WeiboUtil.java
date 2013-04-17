package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import bean.UserBean;
import bean.StatusBean;

public class WeiboUtil {
	/**
	 * 解析微博的发布时间 :
	 * 1分钟内-->刚刚
	 * 1小时内-->xx分钟前
	 * @param time
	 * @return
	 */
	public static String parseTime(String time) {
		Calendar calendar = CommonUtil.parseTime(time);
		String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : "" + calendar.get(Calendar.MINUTE);
		String hour = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : "" + calendar.get(Calendar.HOUR_OF_DAY);
		String second = calendar.get(Calendar.SECOND) < 10 ? "0" + calendar.get(Calendar.SECOND) : "" + calendar.get(Calendar.SECOND);
		String result = hour + ":" + minute + ":" + second;
		long millis = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
		if (millis <= 60 * 1000) {
			result = "刚刚";
		} else if (millis < 60 * 60 * 1000) {
			result = millis / 1000 / 60 + "分钟前";
		} else {
			result = "今天" + hour + ":" + minute;
		}
		return result;
	}
	/**
	 * 将时间转换为Calendar对象
	 * @param time
	 * @return
	 */
	public static Calendar timeToCalendar(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(Date.parse(time)));
		return calendar;
	}
	/**
	 * 解析文本中超链接
	 * @param str
	 * @return
	 */
	public static SpannableStringBuilder parseLink(String str) {
		String regex="http://t.cn/[a-zA-Z0-9]+";
		
		Pattern p=Pattern.compile(regex);
		
		Matcher m=p.matcher(str);
		
		while(m.find()){
			String s=m.group();
			str = str.replace(s, "<font color='#6acaf6'><a href='" + s + "'>" + s + "</a></font>");
		}
		SpannableStringBuilder spannable;
		try {
//			String tmp = new String(str);
//			int index = tmp.indexOf("http://");
//			ArrayList<String> links = new ArrayList<String>();
//			while ((index = tmp.indexOf("http://")) != -1) {
//				tmp = tmp.substring(index);
//				if (tmp.indexOf(" ") != -1) {
//					links.add(tmp.substring(0, tmp.indexOf(" ")));
//					tmp = tmp.substring(tmp.indexOf(" "));
//				} else {
//					links.add(tmp);
//					break;
//				}
//			}
//			for (String s : links) {
//				str = str.replace(s, "<font color='#6acaf6'><a href='" + s + "'>" + s + "</a></font>");
//			}
			Spanned spanned = Html.fromHtml(str);
			spannable = new SpannableStringBuilder(spanned);
			return spannable;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 封装单个微博内容
	 * @param status
	 * @return
	 */
	public static StatusBean getWeiboStatus(JSONObject status) {
		try {
			StatusBean weibo = new StatusBean();
			weibo.created_at = status.optString("created_at");
			weibo.id = status.optString("id");
			weibo.mid = status.optString("mid");
			weibo.idstr = status.optString("idstr");
			weibo.text = status.optString("text");
			weibo.source = status.optString("source");
			weibo.favorited = status.optString("favorited");
			weibo.truncated = status.optString("truncated");
			weibo.in_reply_to_status_id = status.optString("in_reply_to_status_id");
			weibo.in_reply_to_user_id = status.optString("in_reply_to_user_id");
			weibo.in_reply_to_screen_name = status.optString("in_reply_to_screen_name");
			weibo.thumbnail_pic = status.optString("thumbnail_pic");
			weibo.bmiddle_pic = status.optString("bmiddle_pic");
			weibo.original_pic = status.optString("original_pic");
			weibo.geo = status.optString("geo");
			weibo.reposts_count = status.optString("reposts_count");
			weibo.comments_count = status.optString("comments_count");
			weibo.attitudes_count = status.optString("attitudes_count");
			weibo.mlevel = status.optString("mlevel");
			if (!status.isNull("thumbnail_pic")) {
				weibo.thumbnail_pic = status.optString("thumbnail_pic");
			}
			if (!status.isNull("bmiddle_pic")) {
				weibo.bmiddle_pic = status.optString("bmiddle_pic");
			}
			if (!status.isNull("original_pic")) {
				weibo.original_pic = status.optString("original_pic");
			}
			if (!status.isNull("retweeted_status")) {
				JSONObject retweeted_status = status.getJSONObject("retweeted_status");
				weibo.retweeted_status = getWeiboStatus(retweeted_status);
			}
			JSONObject user = status.getJSONObject("user");
			weibo.userBean=getWeiboStatus_user(user);
			return weibo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 封装单个微博内容中的 user部分
	 * @param user
	 * @return
	 */
	public static UserBean getWeiboStatus_user(JSONObject user) {
		try {

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
			return userbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
