package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.multiple.MultipleServerConnection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultMultipleServerConnection<I extends Interpretable<DefaultPackage>>
        extends MultipleServerConnection<DefaultServerSocket, BaseSocket, DefaultPackage, I> {

    protected DefaultMultipleServerConnection(DefaultServerSocket serverSocket, Class<I> interpreterC) {
        super(serverSocket, DefaultPackage.class, interpreterC);
    }
}
