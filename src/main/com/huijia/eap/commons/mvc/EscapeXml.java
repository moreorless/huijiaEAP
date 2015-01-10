package com.huijia.eap.commons.mvc;


import java.util.HashMap;
import java.util.Map;

/**
 * Handles escaping of characters that could be interpreted as XML markup.
 * <p>The specification for <code>&lt;c:out&gt;</code> defines the following
 * character conversions to be applied:
 * <table rules="all" frame="border">
 * <tr><th>Character</th><th>Character Entity Code</th></tr>
 * <tr><td>&lt;</td><td>&amp;lt;</td></tr>
 * <tr><td>&gt;</td><td>&amp;gt;</td></tr>
 * <tr><td>&amp;</td><td>&amp;amp;</td></tr>
 * <tr><td>&#039;</td><td>&amp;#039;</td></tr>
 * <tr><td>&#034;</td><td>&amp;#034;</td></tr>
 * </table>
 */
public class EscapeXml {
    
    private static final String[] ESCAPES;
    
    private static final Map<String, Character> UNESCAPES;

    static {
        int size = '>' + 1; // '>' is the largest escaped value
        ESCAPES = new String[size];
        ESCAPES['<'] = "&lt;";
        ESCAPES['>'] = "&gt;";
        ESCAPES['&'] = "&amp;";
        ESCAPES['\''] = "&#039;";
        ESCAPES['"'] = "&#034;";
        
        UNESCAPES = new HashMap<String, Character>(7);
        UNESCAPES.put("&lt;", '<');
        UNESCAPES.put("&gt;", '>');
        UNESCAPES.put("&amp;", '&');
        UNESCAPES.put("&#039;", '\'');
        UNESCAPES.put("&#034;", '"');
        UNESCAPES.put("&apos;", '\'');
        UNESCAPES.put("&quot;", '\'');
    }

    private static String getEscape(char c) {
        if (c < ESCAPES.length) {
            return ESCAPES[c];
        } else {
            return null;
        }
    }
    
    /**
     * Escape a string.
     * 
     * @param src
     *            the string to escape; must not be null
     * @return the escaped string
     */
    public static String escape(String src) {
        // first pass to determine the length of the buffer so we only allocate once
        int length = 0;
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            String escape = getEscape(c);
            if (escape != null) {
                length += escape.length();
            } else {
                length += 1;
            }
        }

        // skip copy if no escaping is needed
        if (length == src.length()) {
            return src;
        }

        // second pass to build the escaped string
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            String escape = getEscape(c);
            if (escape != null) {
                buf.append(escape);
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
    
    public static String unescape(String src) {
    	StringBuilder buf = new StringBuilder();
    	StringBuilder unescape = new StringBuilder();
    	for (int i = 0; i < src.length(); i++) {
    		char c = src.charAt(i);
    		if (c == '&') {
    			if (unescape.length() > 0) {
    				buf.append(unescape);
    				unescape.setLength(0);
    			}
				unescape.append(c);
    		} else if (c == ';') {
    			unescape.append(c);
    			if (UNESCAPES.containsKey(unescape.toString())) {
    				buf.append(UNESCAPES.get(unescape.toString()));
    			} else {
    				buf.append(unescape);
    			}
    			unescape.setLength(0);
    		} else if (unescape.length() > 0) {
    			unescape.append(c);
    		} else {
    			buf.append(c);
    		}
    	}
    	return buf.toString();
    }
}