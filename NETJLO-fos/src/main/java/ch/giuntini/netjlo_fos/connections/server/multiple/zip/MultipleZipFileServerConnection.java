package ch.giuntini.netjlo_fos.connections.server.multiple.zip;

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

public class MultipleZipFileServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        implements Acceptable, AutoCloseable, Disconnectable, Send {

    private final Class<I> interpreterC;
    private final String rootPathForFiles;

    private final T serverSocket;
    private final AtomicInteger activeConnectionCount = new AtomicInteger(0);
    private volatile int maxConnectionCount = 5;
    private final long zipThreshold;
    private final boolean unzipDirs;
    private volatile boolean stop;

    private final List<ActiveZipFileServerConnection<T, S, I>> CONNECTIONS = Collections.synchronizedList(new LinkedList<>());

    public MultipleZipFileServerConnection(T serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.rootPathForFiles = rootPathForFiles;
        this.interpreterC = interpreterC;
        this.zipThreshold = -1;
        this.unzipDirs = false;
    }

    public MultipleZipFileServerConnection(
            T serverSocket,
            String rootPathForFiles,
            Class<I> interpreterC,
            long zipThreshold,
            boolean unzipDirs
    ) {
        this.interpreterC = interpreterC;
        this.rootPathForFiles = rootPathForFiles;
        this.serverSocket = serverSocket;
        this.zipThreshold = zipThreshold;
        this.unzipDirs = unzipDirs;
    }

    @Override
    public void acceptAndWait() throws IOException {
        while (!stop) {
            while (activeConnectionCount.intValue() < maxConnectionCount) {
                S socket = serverSocket.accept();
                if (zipThreshold == -1) {
                    CONNECTIONS.add(new ActiveZipFileServerConnection<>(socket, rootPathForFiles, interpreterC, this));
                } else {
                    CONNECTIONS.add(new ActiveZipFileServerConnection<>(socket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC, this));
                }
                activeConnectionCount.incrementAndGet();
            }
            Thread.onSpinWait();
        }
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public synchronized void removeClosedActiveConnection(ActiveZipFileServerConnection<T, S, I> connection) {
        CONNECTIONS.remove(connection);
        activeConnectionCount.decrementAndGet();
    }

    public ActiveZipFileServerConnection<T, S, I> getConnection(int index) {
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
