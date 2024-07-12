package ch.giuntini.netjlo_core.connections.client.sockets;

import ch.giuntini.netjlo_core.socket.Connectable;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_core.socket.SocketUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;

public class BaseSocket extends Socket implements Connectable, Disconnectable {

    private final InetSocketAddress address;

    public BaseSocket(SocketImpl impl) throws SocketException {
        super(impl);
        address = null;
    }

    public BaseSocket(String address, int port) {
        super();
        this.address = new InetSocketAddress(SocketUtils.checkIPAddress(address), SocketUtils.checkPort(port));
    }

    public BaseSocket(InetSocketAddress address) {
        super();
        this.address = address;
    }

    @Override
    public void connect() throws IOException {
        if (!isConnected())
            connect(address);
    }

    @Override
    public void disconnect() throws IOException {
        if (!isClosed())
            close();
    }
}
