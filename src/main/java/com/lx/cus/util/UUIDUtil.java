package com.lx.cus.util;

import java.util.UUID;

public final class UUIDUtil {
	private UUIDUtil() {}
	
	public static String uuid32() {
		return UUID.randomUUID().toString().replaceAll("\\-", "");
	}
}
