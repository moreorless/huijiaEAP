package com.huijia.eap.util;

import java.util.Comparator;

public class IPComparatorHandler {
	
	private String IP;
	private Object asset;
	public IPComparatorHandler(String IP,Object asset){
		this.IP=IP;
		this.asset=asset;
	}
	public IPComparatorHandler(){}
	@SuppressWarnings("rawtypes")
	public static Comparator IPComparator=new Comparator(){
		@Override
		public int compare(Object arg0, Object arg1) {
			IPComparatorHandler ip1=(IPComparatorHandler)arg0;
			IPComparatorHandler ip2=(IPComparatorHandler)arg1;
			return compartTo(ip1.IP,ip2.IP);
		}		
	};
	private static long[] parseIp(String ip){
		long result[]=new long[4];
		if(ip!=null && !ip.equals("")){
			ip=ip.replace(".", "#");
			String[] ip1=ip.split("#");
			if(ip!=null){
				result[0]=Long.parseLong(ip1[0]);
				result[1]=Long.parseLong(ip1[1]);
				result[2]=Long.parseLong(ip1[2]);
				result[3]=Long.parseLong(ip1[3]);
			}
		}
		return result;
	}
	public static int compartTo(String ip1,String ip2){
        //以下方法不能判断的原因在于：
//		例如10.4.120.5与10.50.0.0按理应该前者小，但将它们转化为数字组合后，后者位数少，所以反而变成后面一个数字更小。
//		String ip11=ip1.replace(".","");
//		String ip22=ip2.replace(".", "");
//		return new Long(ip11).compareTo(new Long(ip22));
		//比较2个IP的顺序，按照数字顺序
		long[] ip11=parseIp(ip1);
		long[] ip22=parseIp(ip2);
		long ip1Result=0,ip2Result=0;
		for(int i=0;i<4;i++){
			ip1Result+=(ip11[i]<<(24-i*8));
		}
		for(int i=0;i<4;i++){
			ip2Result+=(ip22[i]<<(24-i*8));
		}
		if(ip1Result-ip2Result>0){
			return 1;
		}else if(ip1Result-ip2Result<0){
			return -1;
		}else{
			return 0;
		}
	}
	
	@Override
	public String toString(){
		return this.IP;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public Object getAsset() {
		return asset;
	}
	public void setAsset(Object asset) {
		this.asset = asset;
	}
	/**
	 * 比较startRangeIp和endRangeIp的大小
	 * @param startRangeIp
	 * @param endRangeIp
	 * @return
	 */
	public static boolean comparatorIP(String startRangeIp,String endRangeIp){
		long startIp=getIp10(startRangeIp);
		long endIp=getIp10(endRangeIp);
		if(startIp<=endIp){
			return true;
		}
		return false;
	}
	/**
	 *IPUtil 
	 *getIp10,将xxx.xxx.xx.x类型的IP转换成10进制的long型串
	*/
	public static long getIp10(String ip) {   
        long ip10 = 0;   
        String[] ss = ip.trim().split("\\.");   
        for (int i = 0; i < ss.length; i++) {   
            ip10 += Math.pow(256, 3 - i) * Integer.parseInt(ss[i]);   
        }   
  
        return ip10;   
    }
	
	/**
	 *IPUtil 
	 *getIp,将long型串转换成xxx.xxx.xx.x型的IP
	*/
	public static String getIp(long ip10) {   
	        String ip = "";   
	        long temp = 0;   
	        for (int i = 3; i >= 0; i--) {   
	            temp = ip10 / (long) Math.pow(256, i) % 256;   
	            if (i == 3) {   
	                ip = ip + temp;   
	            } else {   
	                ip = ip + "." + temp;   
	            }   
	        }   
	        return ip;   
	 }

}
