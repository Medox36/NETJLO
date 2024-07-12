package ch.giuntini.netjlo_default.commections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.single.ServerConnection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultServerConnection<I extends Interpretable<DefaultPackage>>
        extends ServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    public DefaultServerConnection(DefaultServerSocket serverSocket, Class<I> interpreterC) {
        super(serverSocket, interpreterC, DefaultPackage.class);
    }
}
