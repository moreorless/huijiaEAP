/**
 
 * author: liunan
 * created: 2012-8-9
 */
package com.huijia.eap.commons.mvc.view;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Streams;
import org.nutz.mvc.View;

public class ImageView implements View {
	
	public static final class ImageByteArrayInputStream extends ByteArrayInputStream {
		
		private String contentType;

		public ImageByteArrayInputStream(byte[] buf) {
			super(buf);
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		
	}

	public ImageView() {
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Throwable {
		if (obj instanceof ImageByteArrayInputStream) {
			ImageByteArrayInputStream ibais = (ImageByteArrayInputStream) obj;
			response.setContentType(ibais.getContentType());
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[8192];
			int len;
			try {
				while (-1 != (len = ibais.read(buf))) {
					out.write(buf, 0, len);
				}
			}
			finally {
				Streams.safeClose(out);
				Streams.safeClose(ibais);
			}
		} else {
			response.setContentType("image/png");
			Streams.safeClose(response.getOutputStream());
		}
	}

}
