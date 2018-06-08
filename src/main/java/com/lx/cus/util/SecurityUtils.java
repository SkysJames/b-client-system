package com.lx.cus.util;

import java.util.Set;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lx.cus.common.ApplicationException;
import com.lx.cus.entity.User;

public class SecurityUtils {
	
	public static final String USER_KEY = "@user@";
	
	public static User getCurrentUser() {
		return (User) ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).getAttribute(USER_KEY);
	}
	
	public static void setCurrentUser(User user) {
		((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(true).setAttribute(USER_KEY, user);
	}
	
	public static void checkPermission(String permisson) {
		Set<String> permissions = getCurrentUser().getPermissions();
		boolean checked = false;
		if (permissions != null && !permissions.isEmpty()) {
			checked = permissions.contains(permisson);
		}
		if (!checked) {
			throw new ApplicationException("您没有该操作权限");
		}
	}
	
	public static boolean containsPermission(String permission) {
		User user = getCurrentUser();
		Set<String> permissions = user.getPermissions();
		return permissions != null && permissions.contains(permission);
	}

}
