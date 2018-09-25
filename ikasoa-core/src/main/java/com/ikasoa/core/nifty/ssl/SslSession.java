package com.ikasoa.core.nifty.ssl;

import javax.security.cert.X509Certificate;

public class SslSession {
    private final String alpn;
    private final String npn;
    private final String version;
    private final String cipher;
    // Time at which the connection was established since epoch in seconds.
    private final long establishedTime;
    private final X509Certificate peerCert;

    public SslSession(
            String alpn,
            String npn,
            String version,
            String cipher,
            long establishedTime,
            X509Certificate peerCert) {
        this.alpn = alpn;
        this.npn = npn;
        this.version = version;
        this.cipher = cipher;
        this.establishedTime = establishedTime;
        this.peerCert = peerCert;
    }


    public String getAlpn() {
        return alpn;
    }

    public String getNpn() {
        return npn;
    }

    public String getVersion() {
        return version;
    }

    public String getCipher() {
        return cipher;
    }

    public long getEstablishedTime() {
        return establishedTime;
    }

    public X509Certificate getPeerCert() {
        return peerCert;
    }

    public String toString() {
        return "SslSession(alpn=" + alpn +
            ", npn=" + npn +
            ", version=" + version +
            ", cipher=" + cipher +
            ", establishedTime=" + Long.toString(establishedTime) +
            ", peerCert=" + peerCert + ")";
    }
}
