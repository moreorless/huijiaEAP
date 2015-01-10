var base64 = (function(obj) {

	var a64 = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/',
		a64_url_safe = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_',
		a256 = {
				indexOf : function(c) {
					return c.charCodeAt(0);
				},
				charAt : String.fromCharCode
			   };

	function code(s, discard, alpha, beta, w1, w2) {
		s = String(s);
		var b = 0, x = '', i, c, bs = 1, sb = 1, length = s.length, tmp;
		for (i = 0; i < length || (!discard && sb > 1); i += 1) {
			b *= w1;
			bs *= w1;
			if (i < length) {
				c = alpha.indexOf(s.charAt(i));
				if (c <= -1 || c >= w1) {
					throw new RangeError();
				}
				sb *= w1;
				b += c;
			}
			while (bs >= w2) {
				bs /= w2;
				if (sb > 1) {
					tmp = b;
					b %= bs;
					x += beta.charAt((tmp - b) / bs);
					sb /= w2;
				}
			}
		}
		return x;
	}

	obj.encode = function(s, url_safe) {
		if (typeof url_safe == "undefined")
			url_safe = false;
		s = code(s, false, a256, url_safe ? a64_url_safe : a64, 256, 64);
		return s + '===='.slice((s.length % 4) || 4);
	};

	obj.decode = function(s, url_safe) {
		if (typeof url_safe == "undefined")
			url_safe = false;
		var i;
		s = String(s).split('=');
		for (i = s.length - 1; i >= 0; i -= 1) {
			if (s[i].length % 4 === 1) {
				throw new RangeError();
			}
			s[i] = code(s[i], true, url_safe ? a64_url_safe : a64, a256, 64, 256);
		}
		return s.join('');
	};

	obj.utf16to8 = function(str) {
	    var out, i, len, c;
	 
	    out = "";
	    len = str.length;
	    for(i = 0; i < len; i++) {
	    c = str.charCodeAt(i);
	    if ((c >= 0x0001) && (c <= 0x007F)) {
	        out += str.charAt(i);
	    } else if (c > 0x07FF) {
	        out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
	        out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
	        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	    } else {
	        out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
	        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	    }
	    }
	    return out;
	};
	
	//utf-16转utf-8
	obj.utf8to16 = function(str) {
	    var out, i, len, c;
	    var char2, char3;
	 
	    out = "";
	    len = str.length;
	    i = 0;
	    while(i < len) {
	    c = str.charCodeAt(i++);
	    switch(c >> 4)
	    { 
	      case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
	        // 0xxxxxxx
	        out += str.charAt(i-1);
	        break;
	      case 12: case 13:
	        // 110x xxxx   10xx xxxx
	        char2 = str.charCodeAt(i++);
	        out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
	        break;
	      case 14:
	        // 1110 xxxx  10xx xxxx  10xx xxxx
	        char2 = str.charCodeAt(i++);
	        char3 = str.charCodeAt(i++);
	        out += String.fromCharCode(((c & 0x0F) << 12) |
	                       ((char2 & 0x3F) << 6) |
	                       ((char3 & 0x3F) << 0));
	        break;
	    }
	    }
	 
	    return out;
	};
	
	return obj;
}({}));
