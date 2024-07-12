package ch.giuntini.netjlo_default.commections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.server.single.DemandingServerConnectionBuilder;

public class DefaultDemandingServerConnectionBuilder<I extends Interpretable<DefaultPackage>>
        extends DemandingServerConnectionBuilder<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    public DefaultDemandingServerConnectionBuilder() {
        super.serverSocket(DefaultServerSocket.class);
        super.socket(BaseSocket.class);
        super.pack(DefaultPackage.class);
    }
}
