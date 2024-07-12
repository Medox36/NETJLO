package ch.giuntini.netjlo_default.commections.server.sockets;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;

import java.io.IOException;
import java.net.InetAddress;

public class DefaultServerSocket extends BaseServerSocket<BaseSocket> {

    public DefaultServerSocket(int port) throws IOException {
        super(port, BaseSocket.class);
    }

    public DefaultServerSocket(int port, int backlog) throws IOException {
        super(port, backlog, BaseSocket.class);
    }

    public DefaultServerSocket(int port, int backlog, int soTimeout) throws IOException {
        super(port, backlog, BaseSocket.class);
        setSoTimeout(soTimeout);
    }

    public DefaultServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        super(port, backlog, bindAddr, BaseSocket.class);
    }
}
