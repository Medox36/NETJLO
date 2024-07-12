package ch.giuntini.netjlo_fos.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;

public class FileConnectionBuilder<S extends BaseSocket, I extends Interpretable> {
    protected Class<S> socketC;
    protected Class<I> interpreterC;
    protected S socket;
    protected String address;
    protected String rootPathForFiles;
    protected int port;

    protected boolean addressIsSet, portIsSet, socketIsSet, interpreterIsSet, rootPathForFilesIsSet, socketInstantiated;

    public FileConnectionBuilder() {
    }

    public FileConnectionBuilder<S, I> address(String address) {
        this.address = address;
        addressIsSet = true;
        return this;
    }

    public FileConnectionBuilder<S, I> port(int port) {
        this.port = port;
        portIsSet = true;
        return this;
    }

    public FileConnectionBuilder<S, I> socket(Class<S> socketC) {
        this.socketC = socketC;
        socketIsSet = true;
        return this;
    }

    public FileConnectionBuilder<S, I> interpreter(Class<I> interpreterC) {
        this.interpreterC = interpreterC;
        interpreterIsSet = true;
        return this;
    }

    public FileConnectionBuilder<S, I> RootPathForFiles(String rootPathForFiles) {
        this.rootPathForFiles = rootPathForFiles;
        rootPathForFilesIsSet = true;
        return this;
    }

    public FileConnectionBuilder<S, I> soTimeout(int timeout) throws SocketException {
        checkState();
        socket.setSoTimeout(timeout);
        return this;
    }

    public FileConnectionBuilder<S, I> tcpNoDelay(boolean on) throws SocketException {
        checkState();
        socket.setTcpNoDelay(on);
        return this;
    }

    public FileConnectionBuilder<S, I> oobInline(boolean on) throws SocketException {
        checkState();
        socket.setOOBInline(on);
        return this;
    }

    public FileConnectionBuilder<S, I> keepAlive(boolean on) throws SocketException {
        checkState();
        socket.setKeepAlive(on);
        return this;
    }

    public FileConnection<S, I> build() {
        checkState();
        return new FileConnection<>(socket, rootPathForFiles, interpreterC);
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
        if (!rootPathForFilesIsSet) {
            throw new IllegalStateException("The root path for files has not been set");
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
