package ch.giuntini.netjlo_demanding.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.socket.Send;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DemandingMultipleServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        implements Acceptable, AutoCloseable, Disconnectable, Send<P> {

    private final Class<T> serverSocketC;
    private final Class<S> socketC;
    private final Class<P> packC;
    private final Class<I> interpreterC;

    private final T serverSocket;
    private final AtomicInteger activeConnectionCount = new AtomicInteger(0);
    private volatile int maxConnectionCount = 5;
    private volatile boolean stop;

    private final List<DemandingActiveServerConnection<T, S, P, I>> CONNECTIONS = Collections.synchronizedList(new LinkedList<>());

    public DemandingMultipleServerConnection(
            T serverSocket,
            Class<T> serverSocketC,
            Class<S> socketC,
            Class<P> packC,
            Class<I> interpreterC
    ) {
        this.serverSocket = serverSocket;
        this.serverSocketC = serverSocketC;
        this.socketC = socketC;
        this.packC = packC;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        while (!stop) {
            while (activeConnectionCount.intValue() < maxConnectionCount) {
                S socket = serverSocket.accept();
                CONNECTIONS.add(new DemandingActiveServerConnection<>(socket, interpreterC, packC, this));
                activeConnectionCount.incrementAndGet();
            }
            Thread.onSpinWait();
        }
        serverSocket.close();
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public synchronized void removeClosedActiveConnection(DemandingActiveServerConnection<T, S, P, I> connection) {
        CONNECTIONS.remove(connection);
        activeConnectionCount.decrementAndGet();
    }

    public DemandingActiveServerConnection<T, S, P, I> getConnection(int index) {
        return CONNECTIONS.get(index);
    }

    public Class<?>[] getTypes() {
        return new Class[]{serverSocketC, socketC, packC, interpreterC};
    }

    public void send(int index, P pack) {
        CONNECTIONS.get(index).send(pack);
    }

    @Override
    public void sendToAll(P pack) {
        synchronized (CONNECTIONS) {
            CONNECTIONS.forEach(spiActiveServerConnection -> spiActiveServerConnection.send(pack));
        }
    }

    @Override
    public void disconnect() throws IOException {
        close();
        serverSocket.close();
        synchronized (CONNECTIONS) {
            CONNECTIONS.forEach(spiActiveServerConnection -> {
                try {
                    spiActiveServerConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void close() {
        stop = true;
    }
}
