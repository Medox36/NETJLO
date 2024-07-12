package ch.giuntini.netjlo_core.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.socket.Send;
import ch.giuntini.netjlo_core.socket.Terminable;

import java.io.IOException;
import java.util.List;

public class ServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        implements Acceptable, Disconnectable, Send<P>, Terminable {

    private Connection<S, P, I> connection;
    protected final T serverSocket;
    protected final Class<P> packC;
    protected final Class<I> interpreterC;

    public ServerConnection(T serverSocket, Class<I> interpreterC, Class<P> packC) {
        this.serverSocket = serverSocket;
        this.interpreterC = interpreterC;
        this.packC = packC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        S socket = serverSocket.accept();
        connection = new Connection<>(socket, interpreterC, packC);
    }

    @Override
    public void disconnect() throws IOException {
        connection.disconnect();
        serverSocket.close();
    }

    @Override
    public void send(P pack) {
        connection.send(pack);
    }

    @Override
    public void terminate() throws IOException {
        connection.terminate();
        serverSocket.close();
    }

    public boolean haveAllPackagesBeenSent() {
        return getRemainingPackages().isEmpty();
    }

    public List<P> getRemainingPackages() {
        if (connection == null) {
            throw new IllegalStateException("connection not yet connected");
        }
        return connection.getRemainingPackages();
    }
}
