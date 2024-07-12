package ch.giuntini.netjlo_core.connections.client.sockets;

import java.net.SocketException;
import java.net.SocketImpl;
import java.net.URL;

public class URLSocket extends BaseSocket {

    public URLSocket(SocketImpl impl) throws SocketException {
        super(impl);
    }

    public URLSocket(URL address) {
        super(address.toExternalForm(), address.getPort());
    }

    public URLSocket(URL address, int port) {
        super(address.toExternalForm(), port);
    }
}
