package ch.giuntini.netjlo_demanding.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.client.ConnectionBuilder;
import ch.giuntini.netjlo_core.interpreter.Interpretable;

public class DemandingConnectionBuilder<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends ConnectionBuilder<S, P, I> {

    @Override
    public DemandingConnection<S, P, I> build() {
        checkState();
        return new DemandingConnection<>(socket, interpreterC, packC);
    }
}
