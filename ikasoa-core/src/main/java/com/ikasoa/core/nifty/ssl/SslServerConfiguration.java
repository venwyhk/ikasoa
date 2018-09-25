package com.ikasoa.core.nifty.ssl;

import com.google.common.collect.ImmutableList;
import org.jboss.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.io.File;

public abstract class SslServerConfiguration {

    public abstract static class BuilderBase<T> {

        // Note: when adding new fields, make sure to update the initFromConfiguration() method below.
        public File keyFile;
        public String keyPassword = "";
        public File certFile;
        public Iterable<String> ciphers;
        boolean allowPlaintext;

        public T ciphers(Iterable<String> ciphers) {
            this.ciphers = ImmutableList.copyOf(ciphers);
            return (T) this;
        }

        public T keyFile(File keyFile) {
            this.keyFile = keyFile;
            return (T) this;
        }

        public T keyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
            return (T) this;
        }

        public T certFile(File certFile) {
            this.certFile = certFile;
            return (T) this;
        }

        public T allowPlaintext(boolean allowPlaintext) {
            this.allowPlaintext = allowPlaintext;
            return (T) this;
        }

        /**
         * Copies the state of an existing SSL configration into this builder.
         * @param config the SSL configuration.
         * @return this builder.
         */
        public T initFromConfiguration(SslServerConfiguration config) {
            keyFile(config.keyFile);
            keyPassword(config.keyPassword);
            certFile(config.certFile);
            ciphers(config.ciphers);
            allowPlaintext(config.allowPlaintext);
            return (T) this;
        }

        protected abstract SslServerConfiguration createServerConfiguration();

//        public SslServerConfiguration build() {
//            Preconditions.checkNotNull(keyFile);
//            Preconditions.checkNotNull(certFile);
//            return createServerConfiguration();
//        }
    }

    public final Iterable<String> ciphers;
    public final File keyFile;
    public final String keyPassword;
    public final File certFile;
    public final boolean allowPlaintext;

    private SslHandlerFactory serverContext;

    protected SslServerConfiguration(BuilderBase builder) {
        this.ciphers = builder.ciphers;
        this.keyFile = builder.keyFile;
        this.keyPassword = builder.keyPassword;
        this.certFile = builder.certFile;
        this.allowPlaintext = builder.allowPlaintext;
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
