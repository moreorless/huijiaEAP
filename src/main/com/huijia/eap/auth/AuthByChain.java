package com.huijia.eap.auth;

import java.util.ArrayList;

import com.huijia.eap.annotation.AuthBy;
import com.huijia.eap.annotation.MatchBy;


public class AuthByChain {

	private ArrayList<AuthBy> chain = new ArrayList<AuthBy>();
	
	public AuthByChain() {
		
	}
	
	public AuthByChain(AuthBy... bys) {
		if (bys != null) {
			for (AuthBy by : bys)
				if (by != null)
					chain.add(by);
		}
	}
	
	public AuthByChain add(AuthBy by) {
		if (by != null)
			chain.add(by);
		return this;
	}
	
	public String getModule() {
		for (AuthBy by : chain) {
			if (by.module() != null && by.module().trim().length() > 0)
				return by.module();
		}
		
		return null;
	}
	
	public MatchBy[] getMatchBys() {
		for (AuthBy by : chain) {
			if (by.value() != null && by.value().length > 0)
				return by.value();
		}
		
		return null;
	}
	
	public boolean isLogin() {
		return chain.iterator().next().login();
	}
	
	public boolean isCheck() {
		return chain.iterator().next().check();
	}
	
	public boolean isReturnable() {
		return chain.iterator().next().returnable();
	}
	
	public AuthBy.AuthType getType() {
		return chain.iterator().next().type();
	}
	
	public boolean isEmpty() {
		return chain.isEmpty();
	}
}
