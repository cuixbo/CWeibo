package bean;

import java.io.Serializable;

/**
 * 微博（status）
 * 
 * 返回值字段 字段类型 字段说明<br>
 * created_at string 微博创建时间<br>
 * id int64 微博ID<br>
 * mid int64 微博MID<br>
 * idstr string 字符串型的微博ID<br>
 * text string 微博信息内容<br>
 * source string 微博来源<br>
 * favorited boolean 是否已收藏，true：是，false：否<br>
 * truncated boolean 是否被截断，true：是，false：否<br>
 * in_reply_to_status_id string （暂未支持）回复ID<br>
 * in_reply_to_user_id string （暂未支持）回复人UID<br>
 * in_reply_to_screen_name string （暂未支持）回复人昵称<br>
 * thumbnail_pic string 缩略图片地址，没有时不返回此字段<br>
 * bmiddle_pic string 中等尺寸图片地址，没有时不返回此字段<br>
 * original_pic string 原始图片地址，没有时不返回此字段<br>
 * geo object 地理信息字段 详细<br>
 * user object 微博作者的用户信息字段 详细<br>
 * retweeted_status object 被转发的原微博信息字段，当该微博为转发微博时返回 详细<br>
 * reposts_count int 转发数<br>
 * comments_count int 评论数<br>
 * attitudes_count int 表态数<br>
 * mlevel int 暂未支持<br>
 * visible object 微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号<br>
 * 
 * @author xiaobocui
 * 
 */
public class StatusBean implements Serializable {
	private static final long serialVersionUID = -2226866803064567133L;
	public String created_at;
	public String id;
	public String mid;
	public String idstr;
	public String text;
	public String source;
	public String favorited;
	public String truncated;
	public String in_reply_to_status_id;
	public String in_reply_to_user_id;
	public String in_reply_to_screen_name;
	public String thumbnail_pic;
	public String bmiddle_pic;
	public String original_pic;
	public String geo;
	public String reposts_count;
	public String comments_count;
	public String attitudes_count;
	public String mlevel;
	public VisibleBean visible;
	public StatusBean retweeted_status;
	public UserBean userBean;
}
