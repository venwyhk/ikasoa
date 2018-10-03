package com.ikasoa.core.nifty.ssl;

import org.jboss.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.io.File;

public abstract class SslServerConfiguration {

    public final Iterable<String> ciphers;
    public final File keyFile;
    public final String keyPassword;
    public final File certFile;
    public final boolean allowPlaintext;

    private SslHandlerFactory serverContext;

    protected SslServerConfiguration(SslServerConfiguration config) {
        this.ciphers = config.ciphers;
        this.keyFile = config.keyFile;
        this.keyPassword = config.keyPassword;
        this.certFile = config.certFile;
        this.allowPlaintext = config.allowPlaintext;
    }

    protected final void initializeServerContext() {
        serverContext = createSslHandlerFactory();
    }

    protected abstract SslHandlerFactory createSslHandlerFactory();

    public SslHandler createHandler() throws Exception {
        return serverContext.newHandler();
    }

    public abstract SslSession getSession(SSLEngine engine) throws SSLException;
}
