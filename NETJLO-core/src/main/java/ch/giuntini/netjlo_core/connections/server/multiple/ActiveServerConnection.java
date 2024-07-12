package ch.giuntini.netjlo_core.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;

import java.io.IOException;

public class ActiveServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends Connection<S, P, I> {

    private final MultipleServerConnection<T, S, P, I> parent;

    protected ActiveServerConnection(
            S socket,
            Class<I> interpreterC,
            Class<P> packC,
            MultipleServerConnection<T, S, P, I> parent
    ) {
        super(socket, interpreterC, packC);
        if (socket.isClosed() || !socket.isConnected()) {
            throw new IllegalStateException("The given Socket for a ActiveServerConnection must be open and connected");
        }
        this.parent = parent;
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        parent.removeClosedActiveConnection(this);
    }
}
