package com.huijia.eap.commons.ioc;

import java.util.ArrayList;
import java.util.Collections;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocException;

public class ComboIoc implements Ioc {
	
	private Ioc[] iocs;
	
	public ComboIoc(Ioc... iocs) {
		this.iocs = iocs;
	}

	@Override
	public <T> T get(Class<T> type, String name) throws IocException {
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				if (ioc.has(name)) {
					return ioc.get(type, name);
				}
			}
		}
		return null;
	}

	@Override
	public <T> T get(Class<T> type) throws IocException {
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				T value = null;
				try {
					value = ioc.get(type);
				} catch (IocException e) {
					continue;
				}
				if (value != null)
					return value;
			}
		}
		return null;
	}

	@Override
	public boolean has(String name) throws IocException {
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				boolean has = ioc.has(name);
				if (has)
					return true;
			}
		}
		return false;
	}

	@Override
	public String[] getNames() {
		ArrayList<String> names = new ArrayList<String>();
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				Collections.addAll(names, ioc.getNames());
			}
		}
		return names.toArray(new String[names.size()]);
	}

	@Override
	public void reset() {
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				ioc.reset();
			}
		}
	}

	@Override
	public void depose() {
		if (iocs != null) {
			for (Ioc ioc : iocs) {
				ioc.depose();
			}
		}
	}


}
