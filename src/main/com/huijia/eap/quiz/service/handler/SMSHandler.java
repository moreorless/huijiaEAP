package com.huijia.eap.quiz.service.handler;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

public class SMSHandler {
	private Logger logger = Logger.getLogger(this.getClass());

	private static final int CODE_NUM = 4; // 验证码字符个数

	private static CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

	private String smsTemplate = "1";

	private String expireMinutes = "1";

	public SMSHandler(String smsTemplate, int expireMinutes) {
		this.smsTemplate = smsTemplate;
		this.expireMinutes = String.valueOf(expireMinutes);
	}

	public SMSHandler() {

	}

	public int initSMSService() {
		try {
			// ******************************注释*********************************************
			// *初始化服务器地址和端口 *
			// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
			// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
			// *******************************************************************************
			restAPI.init("sandboxapp.cloopen.com", "8883");
			// ******************************注释*********************************************
			// *初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN *
			// *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
			// *参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。 *
			// *******************************************************************************
			restAPI.setAccount("8a48b5514ae16942014af0c1ee800ae0",
					"1664c6a232e64b89845e3d717d9b2fdd");
			// ******************************注释*********************************************
			// *初始化应用ID *
			// *测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
			// *应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
			// *******************************************************************************
			restAPI.setAppId("aaf98f894b353559014b3a14dead0338");

			logger.error("短信服务器登录成功");
			return 0;
		} catch (Exception e) {
			logger.error("短信服务器登录失败");
			return -1;
		}
	}

	// 随机生成一个字符
	private static String getRandomChar() {
		Random random = new Random();
		int index = random.nextInt(9);
		return String.valueOf(index);
	}

	private String getRandomCode() {
		String s = "";
		for (int i = 0; i < CODE_NUM; i++) {
			s += getRandomChar();
		}
		return s;
	}

	public String sendSMS(String mobile) {
		String randomCode = this.getRandomCode();

		HashMap<String, Object> result = null;

		result = restAPI.sendTemplateSMS(mobile, smsTemplate, new String[] {
				randomCode, expireMinutes });

		logger.info("SDKTestGetSubAccounts result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				System.out.println(key + " = " + object);
			}
			return randomCode;
		} else {
			// 异常返回输出错误码和错误信息
			logger.error("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
			return null;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SMSHandler sms = new SMSHandler();
		if (sms.initSMSService() == -1) {
			System.out.println("初始化短信服务器失败");
			return;
		}
		String randomCode = sms.sendSMS("13717759088");
		if (randomCode == null) {
			System.out.println("发送短信失败");
			return;
		}
		System.out.println("短信校验码已发送：15201262843, " + randomCode);

	}

	
}
