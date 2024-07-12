package ch.giuntini.netjlo_timed.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_timed.connections.client.TimedConnection;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;
import ch.giuntini.netjlo_timed.sockets.TimedSend;

import java.io.IOException;
import java.util.Date;

public class TimedServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        implements Acceptable, Disconnectable, TimedSend<P> {

    private TimedConnection<S, P, I> connection;
    private T serverSocket;
    private Class<P> packC;
    private Class<I> interpreterC;

    private TimedServerConnection() {
    }

    public TimedServerConnection(T serverSocket, Class<I> interpreterC, Class<P> packC) {
        this.serverSocket = serverSocket;
        this.interpreterC = interpreterC;
        this.packC = packC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        S socket = serverSocket.accept();
        connection = new TimedConnection<>(socket, interpreterC, packC);
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
    public void send(P pack, int delay) {
        connection.send(pack, delay);
    }

    @Override
    public void send(P pack, Date timestamp) {
        connection.send(pack, timestamp);
    }
}
