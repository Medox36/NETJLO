package ch.giuntini.netjlo_default.commections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultConnection <I extends Interpretable<DefaultPackage>> extends Connection<BaseSocket, DefaultPackage, I> {

    private DefaultConnection() {
    }

    public DefaultConnection(BaseSocket socket, Class<I> interpreterC) {
        super(socket, interpreterC, DefaultPackage.class);
    }
}
