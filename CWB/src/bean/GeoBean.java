package bean;

/**
 * 地理信息（geo）
 * 
 * 返回值字段 字段类型 字段说明<br>
 * longitude string 经度坐标<br>
 * latitude string 维度坐标<br>
 * city string 所在城市的城市代码<br>
 * province string 所在省份的省份代码<br>
 * city_name string 所在城市的城市名称<br>
 * province_name string 所在省份的省份名称<br>
 * address string 所在的实际地址，可以为空<br>
 * pinyin string 地址的汉语拼音，不是所有情况都会返回该字段<br>
 * more string 更多信息，不是所有情况都会返回该字段<br>
 * 
 * @author xiaobocui
 * 
 */
public class GeoBean {
	public String longitude;// string 经度坐标
	public String latitude;// string 维度坐标
	public String city;// string 所在城市的城市代码
	public String province;// string 所在省份的省份代码
	public String city_name;// string 所在城市的城市名称
	public String province_name;// string 所在省份的省份名称
	public String address;// string 所在的实际地址，可以为空
	public String pinyin;// string 地址的汉语拼音，不是所有情况都会返回该字段
	public String more;// string 更多信息，不是所有情况都会返回该字段
}
