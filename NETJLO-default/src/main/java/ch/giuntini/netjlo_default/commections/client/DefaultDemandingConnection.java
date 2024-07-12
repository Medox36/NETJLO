package ch.giuntini.netjlo_default.commections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.client.DemandingConnection;

public class DefaultDemandingConnection<I extends Interpretable<DefaultPackage>>
        extends DemandingConnection<BaseSocket, DefaultPackage, I> {

    private DefaultDemandingConnection() {
    }

    public DefaultDemandingConnection(BaseSocket socket, Class<I> interpreterC) {
        super(socket, interpreterC, DefaultPackage.class);
    }
}
