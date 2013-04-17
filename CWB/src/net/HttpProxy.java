package net;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import util.WeiboUtil;

import android.os.Handler;
import bean.UserBean;
import bean.StatusBean;

public class HttpProxy {
    /**
     * 加载个人主页微薄
     * @param request
     * @param weibos
     * @param handler
     * @return
     */
    public static ArrayList<StatusBean> getStatusesHome(HttpUriRequest request, Handler handler) {
        ArrayList<StatusBean> weibos=null;
        try {
            HttpResponse response = HttpUtil.getResponse(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                weibos=new ArrayList<StatusBean>();
                String JSON = EntityUtils.toString(response.getEntity());
                // System.out.println(JSON);
                // 获取JSON第一层数据
                JSONObject obj1 = new JSONObject(JSON);
                // previous_cursor = obj1.optString("previous_cursor");
                // next_cursor = obj1.optString("next_cursor");
                // hasvisible = obj1.optString("hasvisible");
                // total_number = obj1.optString("total_number");
                JSONArray arrs = obj1.getJSONArray("statuses");
                // JSONArray arr = new
                // JSONObject(JSON).getJSONArray("statuses");
                // 获取JSON第二层数据(statuses)数组内容
                JSONObject obj = null;
                for (int i = 0; i < arrs.length(); i++) {
                	weibos.add(WeiboUtil.getWeiboStatus(arrs.getJSONObject(i)));
//                    obj = arrs.getJSONObject(i);
//                    WeiboBean pwbean = new WeiboBean();
//                    pwbean.created_at = obj.optString("created_at");
//                    pwbean.id = obj.optString("id");
//                    pwbean.mid = obj.optString("mid");
//                    pwbean.idstr = obj.optString("idstr");
//                    pwbean.text = obj.optString("text");
//                    pwbean.source = obj.optString("source");
//                    pwbean.favorited = obj.optString("favorited");
//                    pwbean.truncated = obj.optString("truncated");
//                    pwbean.in_reply_to_status_id = obj.optString("in_reply_to_status_id");
//                    pwbean.in_reply_to_user_id = obj.optString("in_reply_to_user_id");
//                    pwbean.in_reply_to_screen_name = obj.optString("in_reply_to_screen_name");
//                    pwbean.thumbnail_pic = obj.optString("thumbnail_pic");
//                    pwbean.bmiddle_pic = obj.optString("bmiddle_pic");
//                    pwbean.original_pic = obj.optString("original_pic");
//                    pwbean.geo = obj.optString("geo");
//                    pwbean.reposts_count = obj.optString("reposts_count");
//                    pwbean.comments_count = obj.optString("comments_count");
//                    pwbean.attitudes_count = obj.optString("attitudes_count");
//                    pwbean.mlevel = obj.optString("mlevel");
//                    // 获取JSON第三层数据(statuses->user中数据)
//                    JSONObject obj2 = obj.getJSONObject("user");
//                    pwbean.user = new UserBean();
//                    pwbean.user.id = obj2.optString("id");
//                    pwbean.user.idstr = obj2.optString("idstr");
//                    pwbean.user.screen_name = obj2.optString("screen_name");
//                    pwbean.user.name = obj2.optString("name");
//                    pwbean.user.province = obj2.optString("province");
//                    pwbean.user.city = obj2.optString("city");
//                    pwbean.user.location = obj2.optString("location");
//                    pwbean.user.description = obj2.optString("description");
//                    pwbean.user.url = obj2.optString("url");
//                    pwbean.user.profile_image_url = obj2.optString("profile_image_url");
//                    pwbean.user.profile_url = obj2.optString("profile_url");
//                    pwbean.user.weihao = obj2.optString("weihao");
//                    pwbean.user.domain = obj2.optString("domain");
//                    pwbean.user.gender = obj2.optString("gender");
//                    pwbean.user.followers_count = obj2.optString("followers_count");
//                    pwbean.user.friends_count = obj2.optString("friends_count");
//                    pwbean.user.statuses_count = obj2.optString("statuses_count");
//                    pwbean.user.favourites_count = obj2.optString("favourites_count");
//                    pwbean.user.created_at = obj2.optString("created_at");
//                    pwbean.user.following = obj2.optString("following");
//                    pwbean.user.allow_all_act_msg = obj2.optString("allow_all_act_msg");
//                    pwbean.user.remark = obj2.optString("remark");
//                    pwbean.user.geo_enabled = obj2.optString("geo_enabled");
//                    pwbean.user.verified = obj2.optString("verified");
//                    pwbean.user.verified_type = obj2.optString("verified_type");
//                    pwbean.user.verified_reason = obj2.optString("verified_reason");
//                    pwbean.user.allow_all_comment = obj2.optString("allow_all_comment");
//                    pwbean.user.avatar_large = obj2.optString("avatar_large");
//                    pwbean.user.follow_me = obj2.optString("follow_me");
//                    pwbean.user.online_status = obj2.optString("online_status");
//                    pwbean.user.bi_followers_count = obj2.optString("bi_followers_count");
//                    pwbean.user.lang = obj2.optString("lang");
//                    pwbean.user.star = obj2.optString("star");
//                    pwbean.user.mbtype = obj2.optString("mbtype");
//                    pwbean.user.mbrank = obj2.optString("mbrank");
//                    pwbean.user.block_word = obj2.optString("block_word");
//                    weibos.add(pwbean);
                }
                handler.sendEmptyMessage(100);
                return weibos;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
