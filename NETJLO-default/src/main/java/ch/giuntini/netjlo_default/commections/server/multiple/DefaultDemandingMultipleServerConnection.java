package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.server.multiple.DemandingMultipleServerConnection;

public class DefaultDemandingMultipleServerConnection<I extends Interpretable<DefaultPackage>>
        extends DemandingMultipleServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    protected DefaultDemandingMultipleServerConnection(DefaultServerSocket serverSocket, Class<I> interpreterC) {
        super(serverSocket, DefaultServerSocket.class, BaseSocket.class, DefaultPackage.class, interpreterC);
    }
}
