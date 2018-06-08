package com.lx.cus.common;

/**
 * 标识当前系统应用抛出的异常
 * @author LINCHUHAO
 *
 */
public class ApplicationException extends RuntimeException {

	public ApplicationException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
