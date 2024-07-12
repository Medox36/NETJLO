package ch.giuntini.netjlo_timed.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;

public class TimedServerConnectionBuilder
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>> {

    private T serverSocket;
    private Class<T> serverSocketC;
    private Class<S> socketC;
    private Class<P> packC;
    private Class<I> interpreterC;

    /**
     * set to the JDK default, as used in the constructor below
     *
     * @see java.net.ServerSocket#ServerSocket(int)  ServerSocket
     */
    private int backlog = 50;
    private int port;

    private boolean portIsSet, serverSocketIsSet, socketIsSet, interpreterIsSet, packagesIsSet, socketInstantiated;

    public TimedServerConnectionBuilder() {
    }

    public TimedServerConnectionBuilder<T, S, P, I> port(int port) {
        this.port = port;
        portIsSet = true;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> backlog(int backlog) {
        if (socketInstantiated)
            throw new IllegalStateException("Backlog can't be set when the ServerSocket of the specified type is already created. Try setting the backlog earlier.");
        this.backlog = backlog;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> serverSocket(Class<T> serverSocketC) {
        this.serverSocketC = serverSocketC;
        serverSocketIsSet = true;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> socket(Class<S> socketC) {
        this.socketC = socketC;
        socketIsSet = true;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> interpreter(Class<I> interpreterC) {
        this.interpreterC = interpreterC;
        interpreterIsSet = true;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> pack(Class<P> packC) {
        this.packC = packC;
        packagesIsSet = true;
        return this;
    }

    public TimedServerConnectionBuilder<T, S, P, I> soTimeout(int timeout) throws SocketException {
        checkState();
        serverSocket.setSoTimeout(timeout);
        return this;
    }

    public TimedServerConnection<T, S, P, I> build() {
        checkState();
        return new TimedServerConnection<>(serverSocket, interpreterC, packC);
    }

    @SuppressWarnings("ClassGetClass")
    private void checkState() {
        if (!portIsSet) {
            throw new IllegalStateException("The port hasn't been defined");
        }
        if (!serverSocketIsSet) {
            throw new IllegalStateException("The ServerSocket class has not been set");
        }
        if (!socketIsSet) {
            throw new IllegalStateException("The Socket class has not been set");
        }
        if (!interpreterIsSet) {
            throw new IllegalStateException("The Interpreter class has not been set");
        }
        if (!packagesIsSet) {
            throw new IllegalStateException("The Package class has not been set");
        }
        if (!socketInstantiated) {
            try {
                serverSocket = serverSocketC.getConstructor(int.class, int.class, socketC.getClass())
                        .newInstance(port, backlog, socketC);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
