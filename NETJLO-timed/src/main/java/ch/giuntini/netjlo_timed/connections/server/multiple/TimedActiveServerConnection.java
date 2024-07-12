package ch.giuntini.netjlo_timed.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_timed.connections.client.TimedConnection;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.io.IOException;

public class TimedActiveServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        extends TimedConnection<S, P, I> {

    private final TimedMultipleServerConnection<T, S, P, I> parent;

    protected TimedActiveServerConnection(S socket, Class<I> interpreter, Class<P> pack, TimedMultipleServerConnection<T, S, P, I> parent) {
        super(socket, interpreter, pack);
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
