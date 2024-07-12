package ch.giuntini.netjlo_demanding.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.server.single.ServerConnection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_demanding.connections.client.DemandingConnection;

import java.io.IOException;

public class DemandingServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends ServerConnection<T, S, P, I> {

    private DemandingConnection<S, P, I> connection;

    public DemandingServerConnection(T serverSocket, Class<I> interpreterC, Class<P> packC) {
        super(serverSocket, interpreterC, packC);
    }

    @Override
    public void acceptAndWait() throws IOException {
        S socket = serverSocket.accept();
        connection = new DemandingConnection<>(socket, interpreterC, packC);
    }

    @Override
    public void disconnect() throws IOException {
        connection.disconnect();
        super.serverSocket.close();
    }

    @Override
    public void send(P pack) {
        connection.send(pack);
    }
}
