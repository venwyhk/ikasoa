package com.ikasoa.core.nifty.ssl;

import org.jboss.netty.handler.ssl.SslHandler;

/**
 * A Factory that returns a handler.
 */
public interface SslHandlerFactory {
    SslHandler newHandler();
}
