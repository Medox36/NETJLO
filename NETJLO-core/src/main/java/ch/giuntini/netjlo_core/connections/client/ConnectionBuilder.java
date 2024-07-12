package ch.giuntini.netjlo_core.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.interpreter.Interpretable;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;

public class ConnectionBuilder<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>> {
    protected Class<S> socketC;
    protected Class<P> packC;
    protected Class<I> interpreterC;
    protected S socket;
    protected String address;
    protected int port;

    protected boolean addressIsSet, portIsSet, socketIsSet, interpreterIsSet, packagesIsSet, socketInstantiated;

    public ConnectionBuilder() {
    }

    public ConnectionBuilder<S, P, I> address(String address) {
        this.address = address;
        addressIsSet = true;
        return this;
    }

    public ConnectionBuilder<S, P, I> port(int port) {
        this.port = port;
        portIsSet = true;
        return this;
    }

    public ConnectionBuilder<S, P, I> socket(Class<S> socketC) {
        this.socketC = socketC;
        socketIsSet = true;
        return this;
    }

    public ConnectionBuilder<S, P, I> pack(Class<P> packC) {
        this.packC = packC;
        packagesIsSet = true;
        return this;
    }

    public ConnectionBuilder<S, P, I> interpreter(Class<I> interpreterC) {
        this.interpreterC = interpreterC;
        interpreterIsSet = true;
        return this;
    }

    public ConnectionBuilder<S, P, I> soTimeout(int timeout) throws SocketException {
        checkState();
        socket.setSoTimeout(timeout);
        return this;
    }

    public ConnectionBuilder<S, P, I> tcpNoDelay(boolean on) throws SocketException {
        checkState();
        socket.setTcpNoDelay(on);
        return this;
    }

    public ConnectionBuilder<S, P, I> oobInline(boolean on) throws SocketException {
        checkState();
        socket.setOOBInline(on);
        return this;
    }

    public ConnectionBuilder<S, P, I> keepAlive(boolean on) throws SocketException {
        checkState();
        socket.setKeepAlive(on);
        return this;
    }

    public Connection<S, P, I> build() {
        checkState();
        return new Connection<>(socket, interpreterC, packC);
    }

    protected void checkState() {
        if (!addressIsSet || !portIsSet) {
            throw new IllegalStateException("The IP-Address or port hasn't been defined! IP-Address:" + address + " port:" + port);
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
                this.socket = socketC.getConstructor(String.class, int.class).newInstance(address, port);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            socketInstantiated = true;
        }
    }
}
