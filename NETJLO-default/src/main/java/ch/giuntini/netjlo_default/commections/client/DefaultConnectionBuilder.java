package ch.giuntini.netjlo_default.commections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.client.ConnectionBuilder;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultConnectionBuilder<I extends Interpretable<DefaultPackage>>
        extends ConnectionBuilder<BaseSocket, DefaultPackage, I> {

    public DefaultConnectionBuilder() {
        super.socket(BaseSocket.class);
        super.pack(DefaultPackage.class);
    }
}
