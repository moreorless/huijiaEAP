/**
 * author: liunan
 * created: 2012-7-17
 */
package com.huijia.eap.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.lang.Mirror;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.annotation.MatchBy;
import com.huijia.eap.auth.matcher.RootPermissionMatcher;

@AuthBy(module="core", value={@MatchBy(RootPermissionMatcher.class)})
@At("/core/exception")
public class EncodeExceptionUtil {
	
	private static class CustomModule extends Module {
		
		private static final Version MODULE_VERSION = new Version(1, 0, 0, null);

		@Override
		public String getModuleName() {
			return "SQLExceptionModule";
		}

		@Override
		public void setupModule(SetupContext context) {
			context.addBeanSerializerModifier(new BeanSerializerModifier() {
				//确保message属性第一个被解析

				@Override
				public List<BeanPropertyWriter> orderProperties(
						SerializationConfig config, BeanDescription beanDesc,
						List<BeanPropertyWriter> beanProperties) {
					Collections.sort(beanProperties, new Comparator<BeanPropertyWriter>() {

						@Override
						public int compare(BeanPropertyWriter o1,
								BeanPropertyWriter o2) {
							return o1.getName().equals("message") ? -1 : (o2.getName().equals("message") ? 1 : 0);
						}
						
					});
					return beanProperties;
				}
				
			});
		}

		@Override
		public Version version() {
			return MODULE_VERSION;
		}
		
	}
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
		mapper.registerModule(new CustomModule());
		mapper.addHandler(new DeserializationProblemHandler() {

			@Override
			public boolean handleUnknownProperty(DeserializationContext ctxt,
					JsonParser jp, JsonDeserializer<?> deserializer,
					Object beanOrClass, String propertyName)
					throws IOException, JsonProcessingException {
				
				if (!Class.class.isAssignableFrom(beanOrClass.getClass()) && SQLException.class.isAssignableFrom(beanOrClass.getClass())) {
					Mirror<SQLException> mirror = Mirror.me(SQLException.class);
					switch (propertyName) {
						case "errorCode":
							mirror.setValue(beanOrClass, "vendorCode", jp.getIntValue());
							break;
						case "sqlstate":
							mirror.setValue(beanOrClass, "SQLState", jp.getText());
							break;
					}
					return true;
				}
				
				return true;
			}
			
		});
	}
	
	public static final String encodeException(Throwable t) throws IOException {
		byte[] exception = mapper.writeValueAsBytes(t);
		return Base64.encodeBytes(exception, Base64.URL_SAFE);
	}
	
	public static final Throwable decodeException(String encodedException) throws IOException {
		byte[] json = Base64.decode(encodedException, Base64.URL_SAFE);
		return mapper.readValue(json, Throwable.class);
	}
	
	public static void main(String[] args) throws IOException {
		System.out.print("请输入错误码:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = reader.readLine()) != null) {
			if ("exit".equalsIgnoreCase(line))
				break;
			Throwable t = decodeException(line);
			if (t != null)
				t.printStackTrace();
			else
				System.out.println("parse failed!");
			System.out.println("");
			System.out.println("-------------------错误码解析完毕----------------");
			System.out.print("请输入错误码:");
		}
	}
	
	@At
	@Ok("json")
	public String parse(HttpSession session, @Param("encode") String encode) throws IOException {
		Long lastAccessTime = (Long) session.getAttribute("EncodeExceptionUtil.http.parse.lastaccesstime");
		if (lastAccessTime != null && System.currentTimeMillis() - lastAccessTime < 10000) {
			return "对不起，10秒内只能进行一次解析";
		}
		session.setAttribute("EncodeExceptionUtil.http.parse.lastaccesstime", System.currentTimeMillis());
		
		Throwable t = decodeException(encode);
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		t.printStackTrace(pw);
		pw.flush();
		pw.close();
		writer.flush();
		writer.close();
		
		return writer.getBuffer().toString();
	}
	
	@At
	@Ok("raw:html")
	public StringBuilder page(HttpServletRequest request) {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
			.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
			.append("<head>")
			.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />")
			.append("<title>错误码解析工具</title>")
			.append("<link type=\"text/css\" rel=\"stylesheet\" href=\"").append(request.getContextPath()).append("/css/reset.css\" />")
			.append("<style type=\"text/css\">textarea { width:90%; border:0 none; margin:0 5%; } button { float:right; margin-right:5%; } pre { clear:both; width:90%; margin:0 auto; }</style>")
			.append("</head>")
			.append("<body>")
			.append("<div>请输入错误码:</div>")
			.append("<div><textarea id=\"encode\" rows=\"20\"></textarea></div>")
			.append("<div><button type=\"button\">解析</button></div>")
			.append("<div><pre id=\"content\"></pre></div>")
			.append("<script type=\"text/javascript\" src=\"").append(request.getContextPath()).append("/js/jquery.js\"></script>")
			.append("<script type=\"text/javascript\">")
			.append("$('button').click(function() {")
			.append("  $('#content').html('');")
			.append("  var encode = $('textarea').val();")
			.append("  if (encode && $.trim(encode).length > 0)")
			.append("    $.post('").append(request.getContextPath()).append("/core/exception/parse', {encode: encode}, function(stack) {")
			.append("      $('#content').html(stack);")
			.append("    }, 'json');")
			.append("});")
			.append("</script>")
			.append("</body>")
			.append("</html>");
		
		return html;
	}

}
