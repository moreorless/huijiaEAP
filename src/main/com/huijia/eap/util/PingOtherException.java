/**
 * Author: hu_shenghua
 * Created: Dec 2, 2011
 */
package com.huijia.eap.util;

/**
 * 
 * @author: hu_shenghua
 */
public class PingOtherException extends PingException
{
	public PingOtherException(String msg)
	{
		super(msg);
	}
	public PingOtherException()
	{
		super("Other Ping Exception.");
	}
}
