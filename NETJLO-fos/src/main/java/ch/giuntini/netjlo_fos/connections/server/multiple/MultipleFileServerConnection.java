package ch.giuntini.netjlo_fos.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.socket.Send;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleFileServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        implements Acceptable, AutoCloseable, Disconnectable, Send {

    private final Class<I> interpreterC;
    private final String rootPathForFiles;

    private final T serverSocket;
    private final AtomicInteger activeConnectionCount = new AtomicInteger(0);
    private volatile int maxConnectionCount = 5;
    private volatile boolean stop;

    private final List<ActiveFileServerConnection<T, S, I>> CONNECTIONS = Collections.synchronizedList(new LinkedList<>());

    public MultipleFileServerConnection(T serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.rootPathForFiles = rootPathForFiles;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        while (!stop) {
            while (activeConnectionCount.intValue() < maxConnectionCount) {
                S socket = serverSocket.accept();
                CONNECTIONS.add(new ActiveFileServerConnection<>(socket, rootPathForFiles, interpreterC, this));
                activeConnectionCount.incrementAndGet();
            }
            Thread.onSpinWait();
        }
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public synchronized void removeClosedActiveConnection(ActiveFileServerConnection<T, S, I> connection) {
        CONNECTIONS.remove(connection);
        activeConnectionCount.decrementAndGet();
    }

    public ActiveFileServerConnection<T, S, I> getConnection(int index) {
        return CONNECTIONS.get(index);
    }

    public void send(int index, File file) {
        CONNECTIONS.get(index).send(file);
    }

    @Override
    public void sendToAll(File file) {
        CONNECTIONS.forEach(spiActiveServerConnection -> spiActiveServerConnection.send(file));
    }

    @Override
    public void disconnect() throws IOException {
        CONNECTIONS.forEach(spiActiveServerConnection -> {
            try {
                spiActiveServerConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverSocket.close();
    }

    @Override
    public void close() {
        stop = true;
    }
}
