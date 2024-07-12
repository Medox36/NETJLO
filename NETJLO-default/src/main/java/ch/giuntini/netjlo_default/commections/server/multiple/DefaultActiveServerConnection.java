package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.multiple.ActiveServerConnection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultActiveServerConnection<I extends Interpretable<DefaultPackage>>
        extends ActiveServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    protected DefaultActiveServerConnection(
            BaseSocket socket,
            Class<I> interpreterC,
            DefaultMultipleServerConnection<I> parent
    ) {
        super(socket, interpreterC, DefaultPackage.class, parent);
    }
}
