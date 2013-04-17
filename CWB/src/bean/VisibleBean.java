package bean;

/**
 * 微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
 * 
 * @author xiaobocui
 * 
 */
public class VisibleBean {
	public String type;
	public String list_id;

}
