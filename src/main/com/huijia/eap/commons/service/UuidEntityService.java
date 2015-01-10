package com.huijia.eap.commons.service;

import java.util.UUID;

import org.nutz.service.NameEntityService;

public class UuidEntityService<T> extends NameEntityService<T> {

	/**
	 * 生成UUID
	 * @return 生成的UUID
	 */
	protected String getUuid() {
		return UUID.randomUUID().toString();
	}
	
}
