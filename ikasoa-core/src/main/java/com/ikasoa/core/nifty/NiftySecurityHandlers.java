package com.ikasoa.core.nifty;

import org.jboss.netty.channel.ChannelHandler;

public interface NiftySecurityHandlers {
	ChannelHandler getAuthenticationHandler();

	ChannelHandler getEncryptionHandler();
}
