package com.huijia.eap.quiz.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.huijia.eap.GlobalConfig;

/**
 * 报告模板
 * @author leon
 *
 */
public class ReportTemple {

	public static Map<String, String> getTemple(){
		Map<String, String> tplMap = new HashMap<String, String>();
		
		String tplIndex = GlobalConfig.getContextValueAs(String.class, "conf.dir") 
				+ File.separator + "report" + File.separator + "index.properties";
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new FileReader(tplIndex));
			String line = null;
			while( (line = reader.readLine()) != null ){
				if(!line.contains("=")) continue;
				String[] items = line.split("=");
				// eg: communication=沟通风格和冲突处理
				tplMap.put(items[0], items[1]);
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(reader != null) reader.close();
			}catch(Exception e){}
		}
		return tplMap;
	}
}
