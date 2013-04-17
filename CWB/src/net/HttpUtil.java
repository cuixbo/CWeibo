package net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtil {

	public static HttpGet getRequest_GET(String uri){
		return new HttpGet(uri);
	}
	public static HttpPost getRequest_POST(String uri,BasicNameValuePair... objs )throws Exception{
		HttpPost request=new HttpPost(uri);
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		for (BasicNameValuePair obj : objs) {
			parameters.add(obj);
		}
		request.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		return request;
	}
	
	public static HttpResponse getResponse(HttpUriRequest request)throws Exception{
		return new DefaultHttpClient().execute(request);
	}
	public static HttpResponse getResponseByGET(String uri)throws Exception{
		return new DefaultHttpClient().execute(getRequest_GET(uri));
	}
	public static HttpResponse getResponseByPOST(String uri,BasicNameValuePair...objs)throws Exception{
		return new DefaultHttpClient().execute(getRequest_POST(uri, objs));
	}
	

}
