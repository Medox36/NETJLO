package ch.giuntini.netjlo_fos.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;

public class FileServerConnectionBuilder
        <T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable> {

    protected T serverSocket;
    protected Class<T> serverSocketC;
    protected Class<S> socketC;
    protected Class<I> interpreterC;
    protected String rootPathForFiles;

    /**
     * set to the JDK default, as used in the constructor below
     *
     * @see java.net.ServerSocket#ServerSocket(int)  ServerSocket
     */
    protected int backlog = 50;
    protected int port;

    protected boolean portIsSet, serverSocketIsSet, socketIsSet, interpreterIsSet, rootPathForFilesIsSet, socketInstantiated;

    public FileServerConnectionBuilder() {
    }

    public FileServerConnectionBuilder<T, S, I> port(int port) {
        this.port = port;
        portIsSet = true;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> backlog(int backlog) {
        if (socketInstantiated)
            throw new IllegalStateException("Backlog can't be set when the ServerSocket of the specified type is already created. Try setting the backlog earlier.");
        this.backlog = backlog;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> serverSocket(Class<T> serverSocketC) {
        this.serverSocketC = serverSocketC;
        serverSocketIsSet = true;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> socket(Class<S> socketC) {
        this.socketC = socketC;
        socketIsSet = true;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> interpreter(Class<I> interpreterC) {
        this.interpreterC = interpreterC;
        interpreterIsSet = true;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> RootPathForFiles(String rootPathForFiles) {
        this.rootPathForFiles = rootPathForFiles;
        rootPathForFilesIsSet = true;
        return this;
    }

    public FileServerConnectionBuilder<T, S, I> soTimeout(int timeout) throws SocketException {
        checkState();
        serverSocket.setSoTimeout(timeout);
        return this;
    }

    public FileServerConnection<T, S, I> build() {
        checkState();
        return new FileServerConnection<>(serverSocket, rootPathForFiles, interpreterC);
    }

    @SuppressWarnings("ClassGetClass")
    protected void checkState() {
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
        if (!rootPathForFilesIsSet) {
            throw new IllegalStateException("The root path for files has not been set");
        }
        if (!socketInstantiated) {
            try {
                serverSocket = serverSocketC.getConstructor(int.class, int.class, socketC.getClass()).newInstance(port, backlog, socketC);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
