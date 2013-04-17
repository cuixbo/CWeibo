package bean;

import java.io.Serializable;

/**
 * 用户（user）
 * 
 * 返回值字段 字段类型 字段说明<br>
 * id int64 用户UID<br>
 * idstr string 字符串型的用户UID<br>
 * screen_name string 用户昵称<br>
 * name string 友好显示名称<br>
 * province int 用户所在省级ID<br>
 * city int 用户所在城市ID<br>
 * location string 用户所在地<br>
 * description string 用户个人描述<br>
 * url string 用户博客地址<br>
 * profile_image_url string 用户头像地址，50×50像素<br>
 * profile_url string 用户的微博统一URL地址<br>
 * domain string 用户的个性化域名<br>
 * weihao string 用户的微号<br>
 * gender string 性别，m：男、f：女、n：未知<br>
 * followers_count int 粉丝数<br>
 * friends_count int 关注数<br>
 * statuses_count int 微博数<br>
 * favourites_count int 收藏数<br>
 * created_at string 用户创建（注册）时间<br>
 * following boolean 暂未支持<br>
 * allow_all_act_msg boolean 是否允许所有人给我发私信，true：是，false：否<br>
 * geo_enabled boolean 是否允许标识用户的地理位置，true：是，false：否<br>
 * verified boolean 是否是微博认证用户，即加V用户，true：是，false：否<br>
 * verified_type int 暂未支持<br>
 * remark string 用户备注信息，只有在查询用户关系时才返回此字段<br>
 * status object 用户的最近一条微博信息字段 详细<br>
 * allow_all_comment boolean 是否允许所有人对我的微博进行评论，true：是，false：否<br>
 * avatar_large string 用户大头像地址<br>
 * verified_reason string 认证原因<br>
 * follow_me boolean 该用户是否关注当前登录用户，true：是，false：否<br>
 * online_status int 用户的在线状态，0：不在线、1：在线<br>
 * bi_followers_count int 用户的互粉数<br>
 * lang string 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语<br>
 * 
 * @author xiaobocui
 * 
 */
public class UserBean implements Serializable {
	private static final long serialVersionUID = -4593200564888010999L;
	public String id;
	public String idstr;
	public String screen_name;
	public String name;
	public String province;
	public String city;
	public String location;
	public String description;
	public String url;
	public String profile_image_url;
	public String profile_url;
	public String weihao;
	public String domain;
	public String gender;
	public String followers_count;
	public String friends_count;
	public String statuses_count;
	public String favourites_count;
	public String created_at;
	public String following;
	public String allow_all_act_msg;
	public String remark;
	public String geo_enabled;
	public String verified;
	public String verified_type;
	public String verified_reason;
	public String allow_all_comment;
	public String avatar_large;
	public String follow_me;
	public String online_status;
	public String bi_followers_count;
	public String lang;
	public String star;
	public String mbtype;
	public String mbrank;
	public String block_word;

	public UserBean() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}

	public String getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(String friends_count) {
		this.friends_count = friends_count;
	}

	public String getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}

	public String getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(String favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getFollowing() {
		return following;
	}

	public void setFollowing(String following) {
		this.following = following;
	}

	public String getAllow_all_act_msg() {
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(String allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(String geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(String verified_type) {
		this.verified_type = verified_type;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public String getAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(String allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getFollow_me() {
		return follow_me;
	}

	public void setFollow_me(String follow_me) {
		this.follow_me = follow_me;
	}

	public String getOnline_status() {
		return online_status;
	}

	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}

	public String getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(String bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getMbtype() {
		return mbtype;
	}

	public void setMbtype(String mbtype) {
		this.mbtype = mbtype;
	}

	public String getMbrank() {
		return mbrank;
	}

	public void setMbrank(String mbrank) {
		this.mbrank = mbrank;
	}

	public String getBlock_word() {
		return block_word;
	}

	public void setBlock_word(String block_word) {
		this.block_word = block_word;
	}

}
