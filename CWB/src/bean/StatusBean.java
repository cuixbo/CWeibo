package bean;

import java.io.Serializable;

/**
 * ΢����status��
 * 
 * ����ֵ�ֶ� �ֶ����� �ֶ�˵��<br>
 * created_at string ΢������ʱ��<br>
 * id int64 ΢��ID<br>
 * mid int64 ΢��MID<br>
 * idstr string �ַ����͵�΢��ID<br>
 * text string ΢����Ϣ����<br>
 * source string ΢����Դ<br>
 * favorited boolean �Ƿ����ղأ�true���ǣ�false����<br>
 * truncated boolean �Ƿ񱻽ضϣ�true���ǣ�false����<br>
 * in_reply_to_status_id string ����δ֧�֣��ظ�ID<br>
 * in_reply_to_user_id string ����δ֧�֣��ظ���UID<br>
 * in_reply_to_screen_name string ����δ֧�֣��ظ����ǳ�<br>
 * thumbnail_pic string ����ͼƬ��ַ��û��ʱ�����ش��ֶ�<br>
 * bmiddle_pic string �еȳߴ�ͼƬ��ַ��û��ʱ�����ش��ֶ�<br>
 * original_pic string ԭʼͼƬ��ַ��û��ʱ�����ش��ֶ�<br>
 * geo object ������Ϣ�ֶ� ��ϸ<br>
 * user object ΢�����ߵ��û���Ϣ�ֶ� ��ϸ<br>
 * retweeted_status object ��ת����ԭ΢����Ϣ�ֶΣ�����΢��Ϊת��΢��ʱ���� ��ϸ<br>
 * reposts_count int ת����<br>
 * comments_count int ������<br>
 * attitudes_count int ��̬��<br>
 * mlevel int ��δ֧��<br>
 * visible object ΢���Ŀɼ��Լ�ָ���ɼ�������Ϣ����object��typeȡֵ��0����ͨ΢����1��˽��΢����3��ָ������΢����4������΢����list_idΪ��������<br>
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
