package bean;

/**
 * 消息未读数（remind）
 * 
 * 返回值字段 字段类型 字段说明 <br>
 * status int 新微博未读数<br>
 * follower int 新粉丝数<br>
 * cmt int 新评论数 <br>
 * dm int 新私信数 <br>
 * mention_status int 新提及我的微博数<br>
 * mention_cmt int 新提及我的评论数<br>
 * group int 微群消息未读数<br>
 * private_group int 私有微群消息未读数<br>
 * notice int 新通知未读数<br>
 * invite int 新邀请未读数<br>
 * badge int 新勋章数<br>
 * photo int 相册消息未读数
 * 
 * @author xiaobocui
 * 
 */
public class RemindBean {
	int status;// int 新微博未读数
	int follower;// int 新粉丝数
	int cmt;// int 新评论数
	int dm;// int 新私信数
	int mention_status;// int 新提及我的微博数
	int mention_cmt;// int 新提及我的评论数
	int group;// int 微群消息未读数
	int private_group;// int 私有微群消息未读数
	int notice;// int 新通知未读数
	int invite;// int 新邀请未读数
	int badge;// int 新勋章数
	int photo;// int 相册消息未读数
}
