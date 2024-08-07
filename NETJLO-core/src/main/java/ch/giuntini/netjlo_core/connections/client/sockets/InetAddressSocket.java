package ch.giuntini.netjlo_core.connections.client.sockets;

import ch.giuntini.netjlo_core.socket.SocketUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

public class InetAddressSocket extends BaseSocket {

    public InetAddressSocket(SocketImpl impl) throws SocketException {
        super(impl);
    }

    public InetAddressSocket(Inet4Address inet4Address, int port) {
        super(new InetSocketAddress(inet4Address, SocketUtils.checkPort(port)));
    }

    public InetAddressSocket(Inet6Address inet6Address, int port) {
        super(new InetSocketAddress(inet6Address, SocketUtils.checkPort(port)));
    }

    public InetAddressSocket(InetAddress inetAddress, int port) {
        super(new InetSocketAddress(inetAddress, SocketUtils.checkPort(port)));
    }

    public InetAddressSocket(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }
}
