package ch.giuntini.netjlo_default.commections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.single.ServerConnectionBuilder;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultServerConnectionBuilder<I extends Interpretable<DefaultPackage>>
        extends ServerConnectionBuilder<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    public DefaultServerConnectionBuilder() {
        super.serverSocket(DefaultServerSocket.class);
        super.socket(BaseSocket.class);
        super.pack(DefaultPackage.class);
    }
}
