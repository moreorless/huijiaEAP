package com.huijia.eap.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 本地地址工具
 * @author gaoxl
 *
 */
public class LocalAddressUtil {
	
//	private static Object lock  = new Object();
	
//	private static String ip;
	
	/**
	 * 获取本机地址
	 * @return
	 */
	public static final String getIp() {
		try {
			return fetchIp();
		} catch (SocketException e) {
			return "127.0.0.1";
		}
	}
	
	/**
	 * 获取本机所有有效的外网地址，不包括内网地址
	 * @return
	 * @throws SocketException
	 */
	public static final List<String> getAvailableIps() throws SocketException {
		return getAvailableIps(true);
	}
	
	/**
	 * 获取本机所有有效的IP地址
	 * @param ignoreInternals 是否过滤掉内网地址
	 * @return
	 * @throws SocketException
	 */
	public static final List<String> getAvailableIps(boolean ignoreInternals) throws SocketException {
		List<String> ips = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					ips.add(ip.getHostAddress());
				} else if (!ignoreInternals && ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					ips.add(ip.getHostAddress());
				}
			}
		}
		
		return ips;
	}
	
	/**
	 * 获取本机机器名
	 * <b>如果机器名为中文，则返回IP地址</b>
	 * @return
	 */
	public static final String getHostName() {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) { //处理中文机器名问题，使用IP代替
			hostname = getIp();
		}
		return hostname;
	}

	private static String fetchIp() throws SocketException {
		String localip = "127.0.0.1";// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// 是否找到外网IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
					localip = ip.getHostAddress();
				}
			}
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

}