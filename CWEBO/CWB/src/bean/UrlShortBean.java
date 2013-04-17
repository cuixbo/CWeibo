package bean;

/**
 * 短链（url_short）
 * 
 * 返回值字段 字段类型 字段说明<br>
 * url_short string 短链接<br>
 * url_long string 原始长链接<br>
 * type int 链接的类型，0：普通网页、1：视频、2：音乐、3：活动、5、投票<br>
 * result boolean 短链的可用状态，true：可用、false：不可用。<br>
 * 
 * @author xiaobocui
 * 
 */
public class UrlShortBean {
	public String url_short;// string 短链接
	public String url_long;// string 原始长链接
	public int type;// int 链接的类型，0：普通网页、1：视频、2：音乐、3：活动、5、投票
	public boolean result;// boolean 短链的可用状态，true：可用、false：不可用。
}
