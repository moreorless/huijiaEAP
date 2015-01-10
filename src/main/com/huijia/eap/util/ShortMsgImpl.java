package com.huijia.eap.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vosms.service.InboundMessage;
import com.vosms.service.Message;
import com.vosms.service.OutboundMessage;
import com.vosms.service.Service;
import com.vosms.service.help.CommPortIdentifier;
import com.vosms.service.help.SerialPort;
import com.vosms.service.modem.SerialModemGateway;

/**
 * 短信发送工具类
 */
public class ShortMsgImpl{
	private static Logger logger = Logger.getLogger(ShortMsgImpl.class);
	private static ShortMsgImpl instance = null;
	private AlertActionSMModem modem = null;
	/**
	 * 是否已经初始化
	 */
	private boolean initialized = false;
	
	public boolean isInitialized() {
		return initialized;
	}

	private ShortMsgImpl(){}

	/**
	 * 初始化短信发送工具类并得到实例
	 * @param port 短信猫所有的串口号
	 * @return 失败返回 null,成功返回实例
	 */
	public static ShortMsgImpl getInstance(){
		try{
			if(instance == null)
				instance = new ShortMsgImpl();
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
		return instance;
	}
	
	/**
	 * 重置短信猫,当短信猫配置变化后,需要调用
	 * @param newPort
	 */
	public void reset(String newPort){
		this.destroy();
		this.init(newPort);
	}
	
	/**
	 * 初始化短信猫,系统启动及短信猫配置改变时需要调用
	 * @param port 短信猫所有的串口号
	 */
	public synchronized void init(String port){
		if(isInitialized()){
			logger.warn("modem has been initialized.");
			return;
		}
		
		try{
			if(modem == null)
				modem = new AlertActionSMModem(port);
			this.initialized = true;
		}catch(Exception ex){
			logger.error(ex.getMessage());
			this.initialized = false;
		}
	}
	
	/**
	 * 释放资源,系统关闭时需要调用
	 */
	public void destroy(){
		if(!isInitialized())
			return;
		
		try{
			/*			if(AlertActionSMModem.srv != null){
							AlertActionSMModem.srv.stopService();
							if(instance != null)
								instance = null;
						}
			*/			
			if(modem != null && modem.getSrv() != null){
				modem.getSrv().stopService();
				modem = null;
			}
		}catch(Exception ex){
			logger.warn("stop modem service is failed:" + ex.getMessage());
		}
		this.initialized = false;
	}
	
	/**
	 * 发送短消息
	 * @param phone 电话号码
	 * @param content 发送内容
	 * @return
	 */
	public boolean sendSM(String phone,String content){
		if(!isInitialized()){
			logger.warn("modem may not be intialized!");
			return false;
		}
		/*		if (AlertActionSMModem.srv == null || AlertActionSMModem.srv.getServiceStatus() != Service.ServiceStatus.STARTED){
		return false;
	}
*/		try{
			OutboundMessage msg = new OutboundMessage(phone, content);
			long start = System.currentTimeMillis();
			modem.sendMsg(msg);
			if(logger.isDebugEnabled())
				logger.debug("sendSM time:" + (System.currentTimeMillis() - start));
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 发送短信,只有测试短信猫时使用,每次测试完毕都会释放资源
	 * @param phoneNum 电话号码
	 * @param port 短信猫所在的端口
	 * @param content 发送的内容
	 * @return 成功-true,失败-false
	 */
	public static boolean testSendSM(String phoneNum,String port,String content){
		boolean result = false; 
		ShortMsgImpl testInstance = null;
		try{
			testInstance = new ShortMsgImpl();
			testInstance.init(port);
			result = testInstance.sendSM(phoneNum, content);
		}catch(Exception ex){
		}finally{
			testInstance.destroy();
			testInstance = null;
		}
		return result;
	}
	/**
	 * 测试短信猫是用到的代码
	 * 列出SIM卡上所有短信
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List listSms()
	{
		String msgClass = "all";
		List msgList = new ArrayList();
/*		if (AlertActionSMModem.srv == null || AlertActionSMModem.srv.getServiceStatus() != Service.ServiceStatus.STARTED) {
			return msgList;
		}
*/		InboundMessage.MessageClasses msgClassic = null;
		if (msgClass.equalsIgnoreCase("UNREAD"))
			msgClassic = InboundMessage.MessageClasses.UNREAD;
		else if (msgClass.equalsIgnoreCase("READ"))
		msgClassic = InboundMessage.MessageClasses.READ;
		else if (msgClass.equalsIgnoreCase("ALL"))
			msgClassic = InboundMessage.MessageClasses.ALL;
		else
			msgClassic = InboundMessage.MessageClasses.ALL;
		try {
			//AlertActionSMModem.srv.readMessages(msgList, msgClassic);
			modem.getSrv().readMessages(msgList, msgClassic);
		for (int i = 0; i < msgList.size(); ++i) {
			InboundMessage msg = (InboundMessage)msgList.get(i);
			if(logger.isDebugEnabled())
				logger.debug("Sending SMS, from[" + msg.getOriginator() + "], contents[" + msg.getText() + "]");
		}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return msgList;
	}
	
	
	/**
	 * 短信发送动作工具类
	 * @author zjf
	 *
	 */
	static class AlertActionSMUtil {
		  public static boolean checkModem(CommPortIdentifier portId, String comPort, int baudRate)
		  {
			  if(logger.isDebugEnabled())
				  logger.debug("checking modem on port:" + comPort);
			  
			  boolean iRet = false;

		    try
		    {
				if (portId == null) {
					portId = CommPortIdentifier.getPortIdentifier(comPort);
				}
				
		    	StringBuffer response = new StringBuffer();
		    	SerialPort serialPort = (SerialPort)portId.open("VOSMSCommTest", 2000);
		    	serialPort.setFlowControlMode(1);
		    	serialPort.setSerialPortParams(baudRate,8, 1, 0);
		    	InputStream inStream = serialPort.getInputStream();
		    	OutputStream outStream = serialPort.getOutputStream();
		    	serialPort.enableReceiveTimeout(1000);
		    	int c = inStream.read();
		    	while (c != -1) {
		    		c = inStream.read();
		    	}
				outStream.write(65);
				outStream.write(84);
				outStream.write(13);
				try {
					Thread.sleep(1000L);
				} catch (Exception localException1) {
				}
				c = inStream.read();
				while (c != -1) {
					response.append((char)c);
					c = inStream.read();
				}
				if (response.indexOf("OK") >= 0) {
			        try {
						outStream.write(65);
						outStream.write(84);
						outStream.write(43);
						outStream.write(67);
						outStream.write(71);
						outStream.write(77);
						outStream.write(77);
						outStream.write(13);
						response = new StringBuffer();
						c = inStream.read();
						while (c != -1) {
							response.append((char)c);
							c = inStream.read();
						}
						String iRetStr = response.toString().replaceAll("(\\s+OK\\s+)|[\n\r]", "");
						if (iRetStr.indexOf("MULTIBAND  900E  1800") >= 0) {
							iRet = true;
						}
					} catch (Exception e) {
							logger.error(e.getMessage(),e);
					}finally{
						if(inStream != null)
							inStream.close();
						if(outStream != null)
							outStream.close();
						if(serialPort != null)
							serialPort.close();

					}
				}
				if (!(iRet)) {
					logger.warn("connecting:" + comPort + "failure!");
					return iRet;
				}
		    }
		    catch (Exception e) {
		    	logger.warn(e.getMessage());
		    	return false;
		    }
			logger.info("modem device is ready for using.");
		    return true;
		}
	}
	
	/**
	 * 短信发送 Modem 类
	 * @author zjf
	 *
	 */
	class AlertActionSMModem {
		private Service srv;
		private String port = "COM3";
		//private AlertActionSMModem modem;
	     
		AlertActionSMModem(String port) throws Exception{
			this.port = port;
			if(!initService())
				throw new Exception("modem cann't be initialized.");
		}
	    
		public Service getSrv(){
			return this.srv;
		}
		
/*		public static AlertActionSMModem newInstance(int port) throws Exception{
		   if (modem == null) {
			   modem = new AlertActionSMModem(port);
		   }
	       return modem;
		}
*/	   
		public boolean sendMsg(OutboundMessage msg){
			msg.setEncoding(Message.MessageEncodings.ENCUCS2);
			try{
			   srv.sendMessage(msg);
			}catch(Exception ex){
				logger.error(ex.getMessage(),ex);
				return false;
			}
		   return true;
		}
	 
		private boolean initService(){
			boolean result = false;
			String gatewayId = "modem.com";
			String comPort = null;
//			if(Cupid.platform == OS.windows)
//				comPort = "COM" + this.port;
//			else
//				comPort = "/dev/ttyS" + this.port;
			
			comPort = port;
			logger.info("SendShortMsg : Use Com : "+port);
			int baudRate = 9600;
			String manufacturer = "WaveCom";
			String model = "";
			SerialModemGateway gateway = null;
			
			try{
				gateway = new SerialModemGateway(gatewayId, comPort, baudRate, manufacturer, model);
				if (AlertActionSMUtil.checkModem(null, comPort, baudRate)) {
					//String modelSN = VOSmsUtil.getModemModlel(comPort, baudRate).trim();
					//if (!VOSmsUtil.checkSN(modelSN)) {
						srv = Service.getInstance();
						gateway.setInbound(true);
						gateway.setOutbound(true);
						gateway.setSimPin("0000");
						srv.addGateway(gateway);
						srv.startService(); 
						result = true;
					//}
				}
			}catch(Throwable ex){
				result = false;
				try{
					if(gateway != null){
						gateway.stopGateway();
						gateway = null;
					}
				}catch(Exception e){}
				logger.error(ex.getMessage(),ex);
			}
			return result;
		}
	}
}
