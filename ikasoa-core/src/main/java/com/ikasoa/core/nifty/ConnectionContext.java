package com.ikasoa.core.nifty;

import com.ikasoa.core.nifty.ssl.SslSession;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;

public interface ConnectionContext {
	/**
	 * Gets the remote address of the client that made the request
	 *
	 * @return The client's remote address as a {@link SocketAddress}
	 */
	public SocketAddress getRemoteAddress();

	/**
	 * Returns the SslSession of the connection. This could be null if the channel
	 * is not using SSL.
	 *
	 * @return a {@link SslSession} object for the current connection or
	 *         {@code null} if not using SSL.
	 */
	public SslSession getSslSession();

	/**
	 * Gets the value of an additional attribute specific to the connection
	 *
	 * @param attributeName
	 *            Name of attribute
	 * @return Attribute value, or {@code null} if not present
	 */
	public Object getAttribute(String attributeName);

	/**
	 * Sets the value of an additional attribute specific to the connection
	 *
	 * @param attributeName
	 *            Name of attribute
	 * @param value
	 *            New value of attribute. Must not be {@code null}
	 * @return Old attribute value, or {@code null} if not present
	 */
	public Object setAttribute(String attributeName, Object value);

	/**
	 * Removes an additional attribute specific to the connection
	 *
	 * @param attributeName
	 *            Name of attribute
	 * @return Old attribute value, or {@code null} if attribute was not present
	 */
	public Object removeAttribute(String attributeName);

	/**
	 * Returns a read-only iterator over the additional attributes
	 * 
	 * @return Iterator
	 */
	public Iterator<Map.Entry<String, Object>> attributeIterator();
}
