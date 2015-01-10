package com.huijia.eap.util;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.apache.log4j.Logger;


/**
 * 加密和解密工具类
 * @author wenhua
 *
 */
public class CryptoUtil
{
	private static Logger logger = Logger.getLogger(CryptoUtil.class);	
	
	public static final String ALG_STRING = "AES";
	public static final String ALG_STRING_CIPHER = "AES/CBC/PKCS5Padding";

	private static final byte[] initvector = { (byte) 0xcb, (byte) 0x53, (byte) 0x03,
			(byte) 0x0f, (byte) 0xe0, (byte) 0x79, (byte) 0x9d, (byte) 0xdc,
			(byte) 0x80, (byte) 0xa9, (byte) 0x83, (byte) 0xf1, (byte) 0x03,
			(byte) 0xb6, (byte) 0x59, (byte) 0x83 };
    
	/**
	 * 只用于事件存储的加密。
	 * @param str
	 * @return
	 */
    public static String eventEncrypt(String str)
    {
    	char[] rechar=str.toCharArray();
    	for (int i=0;i<rechar.length;i++)
    	{
    		rechar[i]=Character.reverseBytes(rechar[i]);
    	}
    	return new String(rechar);
    }
 
	/**
	 * 只用于事件存储的解密。
	 * @param str
	 * @return
	 */   
    public static String eventDecrypt(String str)
    {
    	char[] rechar=str.toCharArray();
    	for (int i=0;i<rechar.length;i++)
    	{
    		rechar[i]=Character.reverseBytes(rechar[i]);
    	}
    	return new String(rechar);
    }
    
    /**
     * 使用密码加密字符串。
     * @param str
     * @param pass
     * @return
     */
	public static String encryptString(String str, String pass)
	{
		try
		{
			IvParameterSpec ipSpec = new IvParameterSpec(initvector);
			SecretKey key = new SecretKeySpec(genKeyFromPassword(pass),
					ALG_STRING);
			Cipher cipher = Cipher.getInstance(ALG_STRING_CIPHER);
			cipher.init(Cipher.ENCRYPT_MODE, key, ipSpec);
			byte[] cipherStr = cipher.doFinal(str.getBytes("UTF-8"));
			return base64Encode(cipherStr);
		}
		catch (Exception e)
		{
			return null;
		}
	}
    
	/**
	 * 使用密码解密字符串。
	 * @param str
	 * @param pass
	 * @return
	 */
	public static String decryptString(String str, String pass)
	{
		try
		{
			IvParameterSpec ipSpec = new IvParameterSpec(initvector);
			SecretKey key = new SecretKeySpec(genKeyFromPassword(pass),
					ALG_STRING);
			Cipher cipher = Cipher.getInstance(ALG_STRING_CIPHER);
			cipher.init(Cipher.DECRYPT_MODE, key, ipSpec);
			byte[] strB = cipher.doFinal(base64Decode(str));
			return new String(strB,"UTF-8");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private static String base64Encode(byte[] bytes)
	{
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes);
	}

	private static byte[] base64Decode(String str)
			throws java.io.IOException
	{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(str);
	}

	private static byte[] genKeyFromPassword(String str)
	{
		return md5sum(str.toString().getBytes());
	}

	private static byte[] md5sum(byte[] buffer)
	{
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(buffer);
			return md5.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
		}
		return null;
	}
	
	/**
	 * 加密文件。
	 * @param efile
	 * @param pass
	 * @return
	 */
	public static boolean encryptFile(File efile,String pass)
	{
		try
		{
			char[] buffer=new char[1024];
			InputStreamReader sfilereader=new InputStreamReader(new FileInputStream(efile),"UTF-8");
		    StringWriter swriter=new StringWriter();
		    while (true)
		    {
		      int re=sfilereader.read(buffer);
		      if (re==-1)
		    	  break;
		      swriter.write(buffer,0,re);		      
		    }
		    String str=swriter.toString();
		    swriter.close();
		    sfilereader.close();
		    if (CryptoUtil.decryptString(str,pass)!=null)
		       return true;
		    String dstring=CryptoUtil.encryptString(str,pass);
		    if (dstring==null)
		    	return false;		    
		    OutputStreamWriter dfilewriter=new OutputStreamWriter(new FileOutputStream(efile),"UTF-8");
		    dfilewriter.write(dstring);
		    dfilewriter.close();
		}catch(Exception e)
		{
			logger.info("Encrypt file: " +efile.getName() , e);
			return false;
		}
		return true;
	}
	
	/**
	 * 解密文件。
	 * @param dfile
	 * @param pass
	 * @return
	 */
	public static boolean decryptFile(File dfile,String pass)
	{
		try
		{
			char[] buffer=new char[1024];
			InputStreamReader sfilereader=new InputStreamReader(new FileInputStream(dfile),"UTF-8");
		    StringWriter swriter=new StringWriter();
		    while (true)
		    {
		      int re=sfilereader.read(buffer);
		      if (re==-1)
		    	  break;
		      swriter.write(buffer,0,re);		      
		    }
		    String dstr=CryptoUtil.decryptString(swriter.toString(),pass);
		    swriter.close();
		    sfilereader.close();
		    if (dstr==null)
		    	return false;
		    OutputStreamWriter dfilewriter=new OutputStreamWriter(new FileOutputStream(dfile),"UTF-8");
		    dfilewriter.write(dstr);
		    dfilewriter.close();
		}catch(Exception e)
		{
			logger.info("Decrypt file: " +dfile.getName(), e);
			return false;
		}
		return true;	
	}
    
	/**
	 * 解密文件到内存中的字节数组。
	 * @param dfile
	 * @param pass
	 * @return
	 */
	public static byte[] decryptFileToString(File dfile,String pass)
	{
		byte[] restr=null;
		try
		{
			char[] buffer=new char[1024];
			InputStreamReader sfilereader=new InputStreamReader(new FileInputStream(dfile),"UTF-8");
		    StringWriter swriter=new StringWriter();
		    while (true)
		    {
		      int re=sfilereader.read(buffer);
		      if (re==-1)
		    	  break;
		      swriter.write(buffer,0,re);		      
		    }
		    
		    String dstr=CryptoUtil.decryptString(swriter.toString(),pass);
		    if (dstr!=null)
		      restr=CryptoUtil.decryptString(swriter.toString(),pass).getBytes("UTF-8");
		    sfilereader.close();
		    swriter.close();		    
		}catch(Exception e)
		{
			logger.info("Decrypt file: "+dfile.getName() , e);
			return restr;
		}
		return restr;	
	}

	
}

