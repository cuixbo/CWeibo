package bean;

/**
 * 返回值字段 字段类型 字段说明<br>
 * comment int 是否可以评论我的微博，0：所有人、1：关注的人、2：可信用户<br>
 * geo int 是否开启地理信息，0：不开启、1：开启<br>
 * message int 是否可以给我发私信，0：所有人、1：我关注的人、2：可信用户<br>
 * realname int 是否可以通过真名搜索到我，0：不可以、1：可以<br>
 * badge int 勋章是否可见，0：不可见、1：可见<br>
 * mobile int 是否可以通过手机号码搜索到我，0：不可以、1：可以<br>
 * webim int 是否开启webim， 0：不开启、1：开启<br>
 * 
 * @author xiaobocui
 * 
 */
public class PrivacyBean {
	public int comment;// int 是否可以评论我的微博，0：所有人、1：关注的人、2：可信用户
	public int geo;// int 是否开启地理信息，0：不开启、1：开启
	public int message;// int 是否可以给我发私信，0：所有人、1：我关注的人、2：可信用户
	public int realname;// int 是否可以通过真名搜索到我，0：不可以、1：可以
	public int badge;// int 勋章是否可见，0：不可见、1：可见
	public int mobile;// int 是否可以通过手机号码搜索到我，0：不可以、1：可以
	public int webim;// int 是否开启webim， 0：不开启、1：开启
}
