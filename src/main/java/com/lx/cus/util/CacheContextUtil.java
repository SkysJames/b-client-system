package com.lx.cus.util;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 信息缓存，可换成redis等实现，目前直接放内存中
 * @author LINCHUHAO
 *
 */
public final class CacheContextUtil {
	
	private static final Map<String, Object> value_map = new ConcurrentHashMap<>(1024);
	
	/**
	 * 默认保留一分钟
	 * @param value
	 * @return
	 */
	public static String setValue(Object value) {
		return setValue(value, 1000 * 60L);
	}
	
	public static String setValue(Object value, long timeout) {
		final String code = UUIDUtil.uuid32();
		value_map.put(code, value);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				value_map.remove(code);
			}
		}, timeout);
		return code;
	}
	
	public static Object getValue(String key) {
		return value_map.get(key);
	}

}
