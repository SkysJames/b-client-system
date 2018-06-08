package com.lx.cus.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtil {
	
	public static <T> List<T> mapToBean(List<Map<String, Object>> rows, Class<T> clazz) {
		if (rows != null && !rows.isEmpty() && clazz != null) {
			List<T> values = new ArrayList<>(rows.size());
			rows.forEach(e -> {
				T t = null;
				try {
					t = clazz.newInstance();
					BeanUtils.populate(t, e);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
				values.add(t);
			});
			return values;
		}
		return null;
	}
	
	public static void copyProperties(Object dest, Object src) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void copyPropertiesIgnoreNull(Object dest, Object src) {
		try {
			if (dest == null) {
				throw new IllegalArgumentException("No destination bean specified");
			} else if (src == null) {
				throw new IllegalArgumentException("No src bean specified");
			} else {
				int i;
				String name;
				Object e;
				if (src instanceof DynaBean) {
					DynaProperty[] origDescriptors = ((DynaBean) src).getDynaClass().getDynaProperties();

					for (i = 0; i < origDescriptors.length; ++i) {
						name = origDescriptors[i].getName();
						if (PropertyUtils.isWriteable(dest, name)) {
							e = ((DynaBean) src).get(name);
							if (e != null) {
								BeanUtils.copyProperty(dest, name, e);
							}
						}
					}
				} else if (src instanceof Map) {
					@SuppressWarnings("rawtypes")
					Iterator keys = ((Map) src).keySet().iterator();

					while (keys.hasNext()) {
						String key = (String) keys.next();
						if (PropertyUtils.isWriteable(dest, key)) {
							@SuppressWarnings("rawtypes")
							Object val = ((Map) src).get(key);
							if (val != null) {
								BeanUtils.copyProperty(dest, key, val);
							}
						}
					}
				} else {
					PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(src);

					for (i = 0; i < propertyDescriptors.length; ++i) {
						name = propertyDescriptors[i].getName();
						if (!"class".equals(name) && PropertyUtils.isReadable(src, name)
								&& PropertyUtils.isWriteable(dest, name)) {
							try {
								e = PropertyUtils.getSimpleProperty(src, name);
								if (e != null) {
									BeanUtils.copyProperty(dest, name, e);
								}
							} catch (NoSuchMethodException arg5) {
								;
							}
						}
					}
				}

			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

}
