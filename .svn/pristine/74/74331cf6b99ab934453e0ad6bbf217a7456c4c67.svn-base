package com.android.smartmobile.cz.map;


public class GeometryToCoordinate {

	/**
	 * 判断Geometry类型并处理
	 * @param geometry
	 * @param dataType 用户所需数据类型
	 * @return
	 */
	public static  Object findTypeAndProcess(String geometry, int dataType){
			byte[] data = geometry.getBytes();
			//读取data时的标记，表示从at开始，初始化跳过2字节，2字节表示order
			int at = 2;
			byte[] type = getInt(data, at);//获取到type
			at += 8;
			int shapeType = Integer.parseInt(new String(type), 16);
			switch(shapeType){
			case 1:{
				return processGeometry(geometry.substring(at, at + 32));
			}
			case 2:{
				
			}
			case 3:{
				
			}
			case 4:{
				
			}
			case 5:{
				byte[] part = getInt(data, at);//获取到有多少个type
				at += 8;
				int partInt = Integer.parseInt(new String(part), 16);
				StringBuilder sb = new StringBuilder("<MultiLineString>");
				for(int i = 0; i < partInt; i++){
					//此处继续跳过2字节
					at += 2;
					//byte[] lineType = 
					getInt(data, at);//获取到线类型
					
					at += 8;
					byte[] numPoints = getInt(data, at);//获取点得数量
					at += 8;
					int num = Integer.parseInt(new String(numPoints), 16);
					sb.append("<lineStringMember><LineString><coordinates>");
					for(int j = 0; j < num; j++){
						double[] xy = processGeometry(geometry.substring(at, at + 32));
						sb.append(xy[0] + "," + xy[1] + " ");
						at += 32;
					}
					sb.delete(sb.length() - 1, sb.length());
					sb.append("</coordinates></LineString></lineStringMember>");
				}
				sb.append("</MultiLineString>");
				return sb.toString();
			}
			case 6:{
				
			}
			default:return null;
			}
	}
	

	

	public static byte[] getInt(byte[] data,int at){
		byte[] a = reverse(data, at, 8);
		return a;
	}
	
	public static byte[] getDouble(byte[] data,int at){
		byte[] b = reverse(data, at, 16);
		return b;
	}
	public static byte[] reverse(byte[] data,int start,int length) {
		byte[] temp = new byte[length];
		for(int i = 0; i < length; i+=2){
			byte a = data[start + length - i - 1 - 1];
			byte b = data[start + length - i - 1];
			temp[i] = a;
			temp[i + 1] = b;
		}
		return temp;
	}
	/**
	 * 转换为坐标
	 * @param geometry
	 * @return 坐标
	 */
	public static double[] processGeometry(String geometry){
		double[] xy = new double[2];
		int l = geometry.length();
		String temp = geometry.substring(l-16);
		char[] chars = temp.toCharArray();
		byte b;
		int aa[] = new int[8];
		for(int i = 0; i < chars.length; i+=2){
			b = Integer.valueOf(chars[i]+""+chars[i+1], 16).byteValue();
			if(b<0)aa[i/2]=256+b;
			else aa[i/2]=b;
		}
		xy[1] = byteToDouble(aa);
		
		temp = geometry.substring(l-32,l-16);
		chars = temp.toCharArray();
		aa = new int[8];
		for(int i = 0; i < chars.length; i+=2){
			b = Integer.valueOf(chars[i]+""+chars[i+1], 16).byteValue();
			if(b<0)aa[i/2]=256+b;
			else aa[i/2]=b;
		}
		xy[0] = byteToDouble(aa);
		return xy;
	}
	public static double byteToDouble(int[] b){
		long l;
		l=b[0];
		l&=0xff;
		l|=((long)b[1]<<8);
		l&=0xffff;
		l|=((long)b[2]<<16);
		l&=0xffffff;
		l|=((long)b[3]<<24);
		l&=0xffffffffl;
		l|=((long)b[4]<<32);
		l&=0xffffffffffl;
		
		l|=((long)b[5]<<40);
		l&=0xffffffffffffl;
		
		l|=((long)b[6]<<48);
		l|=((long)b[7]<<56);
		return Double.longBitsToDouble(l);
	}
	
	// 2012-08-01  新处理坐标方式
	/**
	 * 转换为坐标点
	 * @param geometry
	 * @return 坐标点  [x,y]
	 */
	public double[] processGeometryPoint(String geometry){
		double[] xy = new double[2];
		int l = geometry.length();
		String temp = geometry.substring(l-16);
		char[] chars = temp.toCharArray();
		byte[] bytes = new byte[8];
		for(int i = 0; i < chars.length; i+=2){
			bytes[i/2] = Integer.valueOf(chars[i]+""+chars[i+1], 16).byteValue();
		}
		xy[1] = byteToDouble(bytes);
		
		temp = geometry.substring(l-32,l-16);
		chars = temp.toCharArray();
		bytes = new byte[8];
		for(int i = 0; i < chars.length; i+=2){
			bytes[i/2] = Integer.valueOf(chars[i]+""+chars[i+1], 16).byteValue();
		}
		xy[0] = byteToDouble(bytes);
		return xy;
	}
	
	public double byteToDouble(byte[] b){
		long l;
		l=b[0];
		l&=0xff;
		l|=((long)b[1]<<8);
		l&=0xffff;
		l|=((long)b[2]<<16);
		l&=0xffffff;
		l|=((long)b[3]<<24);
		l&=0xffffffffl;
		l|=((long)b[4]<<32);
		l&=0xffffffffffl;
		
		l|=((long)b[5]<<40);
		l&=0xffffffffffffl;
		l|=((long)b[6]<<48);
		
		l|=((long)b[7]<<56);
		return Double.longBitsToDouble(l);
	}
	public static String toPoint(String shape){
		return shape.replaceFirst("POINT", "").replaceFirst("\\(", "").replaceFirst("\\)", "").trim().replaceAll(" ", ",");
	}
	//MULTIPOLYGON(((4 1,3 1,2 1,4 1),(1 0,2 0,3 0,1 0)),(5 5,4 4,3 3,5 5))
	public static String toMultiPolygon(String shape){
		return shape.replaceFirst("MULTIPOLYGON", "").replaceFirst("\\(\\(\\(", "").replaceFirst("\\)\\)\\)", "").trim().replaceAll(" ", ",");
		//return shape.replaceFirst("MULTIPOLYGON", "").replaceFirst("\\(\\(\\(", "").replaceFirst("\\)\\)\\)", "").trim().replaceAll(" ", ",").replaceAll("),(", ";");
	}
	
	public static String toMultiPolygon1(String shape){

		StringBuilder sb = new StringBuilder("<MultiPolygonString>");
        String b = "";
		String polygon = shape;
		if(!"".equals(polygon)||polygon!=null){
			polygon = polygon.replaceFirst("MULTIPOLYGON", "").replaceFirst("\\(", "").trim().replaceAll(" ", ",");
			polygon = polygon.substring(0,polygon.length()-1);
			System.out.println(polygon);
			String[] a = polygon.split("\\)\\),\\(\\(");
			for(int j=0;j<a.length;j++){
				b=a[j];
				String[] d = b.split("\\),\\(");
				if(d.length>1){
					sb.append("<polygonMember>");
					for(int i=0;i<d.length;i++){
						sb.append("<linearRing>").append(d[i].replaceAll("\\(", "").replaceAll("\\)", "")+";").append("</linearRing>");
					}
					sb.append("</polygonMember>");
				}else{
					
					sb.append("<polygonMember>").append(b.replaceAll("\\(", "").replaceAll("\\)", "")+";").append("</polygonMember>");
				}
			}
			sb.append("</MultiPolygonString>");
			System.out.println(sb.toString());
		}
	     return sb.toString();
	}
	
	public static String toMultiLineString(String shape){
		//return shape.replaceFirst("MULTILINESTRING", "").replaceFirst("\\(\\(", "").replaceFirst("\\)\\)", "").trim().replaceAll(" ", ",").replaceAll("),(", ";");
		return shape.replaceFirst("MULTILINESTRING", "").replaceFirst("\\(\\(", "").replaceFirst("\\)\\)", "").trim().replaceAll(" ", ",");
	}
	public static String convertPolygon(String polygon) {
	
		String[] bb = polygon.split(",");
		String num="";
		if(bb.length >= 4){
			for(int i=0;i<bb.length;i++){
				if(i%2==0){
					num += bb[i]+" ";
				}else{
					num += bb[i]+",";
				}
				
			}
			num = num.substring(0,num.length()-1);
		}else{
			
		}
		return num;
	}
//测试	
	public static void main(String[] args) {
//		System.out.println(toMultiPolygon(
//"MULTIPOLYGON(((498726.201876834 309886.939474248,498783.695533107 309890.797802283,498785.553497817 309860.500964487,498787.330135686 309831.539155184,498786.933225345 309828.756922076,498783.754141472 309827.168739362,498741.367741404 309822.796428673,498736.731404307 309824.651219164,498734.215752974 309829.023529853,498731.830489783 309849.687148426,498729.31483845 309855.778956107,498728.386828481 309859.886553886,498726.201876834 309886.939474248)))"));
		//System.out.println(java.util.Arrays.toString(new GeometryToCoordinate().processGeometryPoint("0106000000010000000103000000010000000D000000FBCCB8CE98701E41858905C2FBE91241B3D439C87E711E41E514F3300BEA1241B921C83686711E419FD5FC0092E91241DE160F528D711E41EA4B18281EE91241C66C9FBB8B711E41AC94160713E9124175A93D047F711E41E602CAAC0CE91241D8339178D5701E4185FF8A2FFBE812412C40F5ECC2701E414F32D99A02E912410559EEDCB8701E41B035181814E91241E2E96B52AF701E4144D6A3BF66E91241BB026542A5701E4171ABA61D7FE91241F3C31C8CA1701E412CC8D48B8FE91241FBCCB8CE98701E41858905C2FBE91241")));
	//System.out.println(findTypeAndProcess("0101000000A91D153D93165D40D54F82D629F64340",0));
	//System.out.println("1000".split("\\|"));
//		try {
//			String a = convertPolygon("51,34,12,-66,37,22,9,54");
//			System.out.println(a);
//		} catch (SearcherException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String polygon = "MULTIPOLYGON(((1 1),(2 2)),((3 3)),((4 4)))";
		if("".equals(polygon)||polygon==null){
			polygon = polygon.replaceFirst("MULTIPOLYGON(", "");
			System.out.println(polygon);
		}
	}

	

}
