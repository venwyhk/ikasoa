package com.ikasoa.core.nifty.ssl;

import com.ikasoa.core.nifty.server.impl.NettyServerImpl;

/**
 * Listen to changes in the transport.
 */
public interface TransportAttachObserver {
    void attachTransport(NettyServerImpl transport);

    void detachTransport();
}
