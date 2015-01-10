/**
 * Author: hu_shenghua
 * Created: Dec 2, 2011
 */
package com.huijia.eap.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.MessageFormat;

import com.huijia.eap.GlobalConfig;

/**
 * ping功能封装
 * 
 * @author: hu_shenghua
 */
public class Ping
{	
	/**
	 * ping指定地址
	 * @param IP ping地址
	 * @param retrys 重试次数
	 * @param timeOut 超时时间 毫秒
	 * @param datasize ping数据大小
	 * @return 字符串形式的回显值集合,每个元素为一行
	 * @throws PingException 如果ping不成功则抛出异常
	 */
	public static String pingReply(String IP, int retrys) throws PingException
	{
		String temp = doPingCmd(IP, retrys);
		return temp;
	}
	
	/**
	 * ping 指定地址
	 * @param IP 
	 * @param retrys 次数
	 * @param timeOut 超时,毫秒
	 * @throws PingException 如果ping不成功,则抛出异常
	 */
	public static void ping(String IP, int retrys) throws PingException
	{
		String temp = doPingCmd(IP, retrys);
		if(temp==null)
		{
			throw new PingOtherException();
		}
		boolean status = false;
		if(GlobalConfig.platform==GlobalConfig.OS.windows)
		{
//			if(temp.indexOf("Request timed out")!=-1||temp.indexOf("request could not find host")!=-1)
//			{
//				status = false;
//			}
//			status = true;
			if(temp.contains("Reply from")&&temp.contains("time=")&&temp.contains("TTL="))
			{
				status = true;
			}
			else
			{
				status = false;
			}
		}
		else if(GlobalConfig.platform==GlobalConfig.OS.linux)
		{
//			if(temp.indexOf("Destination Host Unreachable")!=-1||temp.indexOf("unknown host")!=-1)
//			{
//				status = false;
//			}
//			status = true;
			
			if(temp.contains("bytes from")&&temp.contains("icmp_seq=")&&temp.contains("ttl="))
			{
				status = true;
			}
			else
			{
				status = false;
			}
		}
		else
		{
			throw new PingOtherException("not support OS");
		}
		
		if(!status)
		{
			throw new PingOtherException();
		}
	}
	
	
	private static String doPingCmd(String destIp, int maxCount)
	{
		LineNumberReader input = null;
		Process process = null;
		try
		{
			String pingCmd = null;
			if(GlobalConfig.platform==GlobalConfig.OS.windows)
//			if (osName.startsWith("Windows"))
			{
				pingCmd = "cmd /c ping -n {0} {1}";
				pingCmd = MessageFormat.format(pingCmd, maxCount, destIp);
			}
//			else if (osName.startsWith("Linux"))
			else if(GlobalConfig.platform==GlobalConfig.OS.linux)
			{
				pingCmd = "ping -c {0} {1}";
				pingCmd = MessageFormat.format(pingCmd, maxCount, destIp);
			}
			else
			{
				throw new java.lang.IllegalArgumentException("not support OS");
			}
			process = Runtime.getRuntime().exec(pingCmd);
			InputStreamReader ir = new InputStreamReader(process
					.getInputStream());
			input = new LineNumberReader(ir);
			String line;
			
			StringBuilder sb = new StringBuilder();
			while ((line = input.readLine()) != null)
			{
				if (!"".equals(line))
				{
					sb.append(line).append("\r\n");
				}
			}
			return sb.toString().trim();			
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			if (null != input)
			{
				try
				{
					input.close();
				}
				catch (Exception ex)
				{
				}
			}
			if(process!=null)
			{
				try
				{
					process.destroy();
				}
				catch(Exception e)
				{}
			}
		}
	}
//	public static void main(String[] args)
//	{
//		try
//		{
////			String re = Ping.pingReply("www.qqs.coms", 4);
////			System.out.println(re);
//			Ping.ping("www.qqs.coms", 4);
//		}
//		catch (PingException e)
//		{
//			e.printStackTrace();
//		}
//	}
}
