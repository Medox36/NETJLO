package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.server.multiple.DemandingActiveServerConnection;

public class DefaultDemandingActiveServerConnection<I extends Interpretable<DefaultPackage>>
        extends DemandingActiveServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    protected DefaultDemandingActiveServerConnection(
            BaseSocket socket,
            Class<I> interpreterC,
            DefaultDemandingMultipleServerConnection<I> parent
    ) {
        super(socket, interpreterC, DefaultPackage.class, parent);
    }
}
