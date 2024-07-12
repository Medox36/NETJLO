package ch.giuntini.netjlo_default.commections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;
import ch.giuntini.netjlo_demanding.connections.client.DemandingConnectionBuilder;

public class DefaultDemandingConnectionBuilder<I extends Interpretable<DefaultPackage>>
        extends DemandingConnectionBuilder<BaseSocket, DefaultPackage, I> {

    public DefaultDemandingConnectionBuilder() {
        super.socket(BaseSocket.class);
        super.pack(DefaultPackage.class);
    }
}
