package bean;

/**
 * 返回值字段 字段类型 字段说明<br>
 * created_at string 评论创建时间 <br>
 * id int64 评论的ID<br>
 * text string 评论的内容 <br>
 * source string 评论的来源 <br>
 * user object 评论作者的用户信息字段 详细<br>
 * mid string 评论的MID <br>
 * idstr string 字符串型的评论ID <br>
 * status object 评论的微博信息字段 详细 <br>
 * reply_comment object 评论来源评论，当本评论属于对另一评论的回复时返回此字段
 * 
 * @author xiaobocui
 * 
 */
public class CommentBean {
	public String created_at;// string 评论创建时间 <br>
	public String id;// int64 评论的ID<br>
	public String text;// string 评论的内容 <br>
	public String source;// string 评论的来源 <br>
	public UserBean userBean;// object 评论作者的用户信息字段 详细<br>
	public String mid;// string 评论的MID <br>
	public String idstr;// string 字符串型的评论ID <br>
	public StatusBean status;// object 评论的微博信息字段 详细 <br>
	public CommentBean reply_comment;// object 评论来源评论，当本评论属于对另一评论的回复时返回此字段
}
