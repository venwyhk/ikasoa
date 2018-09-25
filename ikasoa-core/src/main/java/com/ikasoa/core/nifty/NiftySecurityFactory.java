package com.ikasoa.core.nifty;

import com.ikasoa.core.nifty.server.NettyServerConfiguration;
import com.ikasoa.core.nifty.server.NiftyServerConfiguration;

public interface NiftySecurityFactory {
	NiftySecurityHandlers getSecurityHandlers(NiftyServerConfiguration def, NettyServerConfiguration serverConfig);
}
