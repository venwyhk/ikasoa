package com.ikasoa.core.nifty;

public class RequestContexts {
	private static ThreadLocal<RequestContext> threadLocalContext = new ThreadLocal<>();

	private RequestContexts() {
	}

	public static RequestContext getCurrentContext() {
		RequestContext currentContext = threadLocalContext.get();
		return currentContext;
	}

	public static void setCurrentContext(RequestContext requestContext) {
		threadLocalContext.set(requestContext);
	}

	/**
	 * Gets the thread-local context for the currently running request
	 *
	 * This is normally called only by the server, but it can also be useful to call
	 * when cleaning up a context
	 */
	public static void clearCurrentContext() {
		threadLocalContext.remove();
	}
}
