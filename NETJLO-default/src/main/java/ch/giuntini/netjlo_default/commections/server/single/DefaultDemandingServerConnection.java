package ch.giuntini.netjlo_default.commections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.server.single.DemandingServerConnection;

public class DefaultDemandingServerConnection<I extends Interpretable<DefaultPackage>>
        extends DemandingServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    public DefaultDemandingServerConnection(DefaultServerSocket serverSocket, Class<I> interpreterC) {
        super(serverSocket, interpreterC, DefaultPackage.class);
    }
}
