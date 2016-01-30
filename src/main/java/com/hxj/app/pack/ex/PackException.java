package com.hxj.app.pack.ex;

/**
 * 打包异常
 * 
 * @author Hxuejie hxuejie@126.com
 */
public class PackException extends Exception {
	private static final long	serialVersionUID	= 1L;

	public PackException() {
		super();
	}

	public PackException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PackException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackException(String message) {
		super(message);
	}

	public PackException(Throwable cause) {
		super(cause);
	}

}
