package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import activity.CApp;
import android.R.integer;
import android.content.Context;
import android.text.method.DateTimeKeyListener;

public class CommonUtil {

	Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public static Calendar parseTime(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(Date.parse(time)));
		return calendar;
	}

	public static String groupUri_WithAccessTokenAndParams(String uri, String[] params, String[] values) {
		uri += "?access_token=" + CApp.ACCESS_TOKEN;
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				uri += "&" + params[i] + "=" + values[i];
			}
		}
		return uri;
	}

}
