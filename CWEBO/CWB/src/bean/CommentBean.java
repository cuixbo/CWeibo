package bean;

/**
 * ����ֵ�ֶ� �ֶ����� �ֶ�˵��<br>
 * created_at string ���۴���ʱ�� <br>
 * id int64 ���۵�ID<br>
 * text string ���۵����� <br>
 * source string ���۵���Դ <br>
 * user object �������ߵ��û���Ϣ�ֶ� ��ϸ<br>
 * mid string ���۵�MID <br>
 * idstr string �ַ����͵�����ID <br>
 * status object ���۵�΢����Ϣ�ֶ� ��ϸ <br>
 * reply_comment object ������Դ���ۣ������������ڶ���һ���۵Ļظ�ʱ���ش��ֶ�
 * 
 * @author xiaobocui
 * 
 */
public class CommentBean {
	public String created_at;// string ���۴���ʱ�� <br>
	public String id;// int64 ���۵�ID<br>
	public String text;// string ���۵����� <br>
	public String source;// string ���۵���Դ <br>
	public UserBean userBean;// object �������ߵ��û���Ϣ�ֶ� ��ϸ<br>
	public String mid;// string ���۵�MID <br>
	public String idstr;// string �ַ����͵�����ID <br>
	public StatusBean status;// object ���۵�΢����Ϣ�ֶ� ��ϸ <br>
	public CommentBean reply_comment;// object ������Դ���ۣ������������ڶ���һ���۵Ļظ�ʱ���ش��ֶ�
}
