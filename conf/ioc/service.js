var ioc = {
	// 默认的登录认证
	defaultAuthenticator : {
		type : "com.huijia.eap.auth.authentication.DefaultAuthenticator",
		fields : {
			service : {refer: 'userService' },
			servletContext : {app:'$servlet'}
		}
	},
	defaultPermissionMatcher : {
		type : "com.huijia.eap.auth.matcher.SimpleRWPermissionMatcher",
		fields : {
			methods : [],
		}
	}
};