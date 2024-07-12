package ch.giuntini.netjlo_core.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.socket.Send;
import ch.giuntini.netjlo_core.socket.Terminable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        implements Acceptable, AutoCloseable, Disconnectable, Send<P>, Terminable {

    private final Class<P> packC;
    private final Class<I> interpreterC;

    private final T serverSocket;
    private final AtomicInteger activeConnectionCount = new AtomicInteger(0);
    private volatile int maxConnectionCount = 5;
    private volatile boolean stop;

    private final List<ActiveServerConnection<T, S, P, I>> CONNECTIONS = Collections.synchronizedList(new LinkedList<>());

    public MultipleServerConnection(T serverSocket, Class<P> packC, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.packC = packC;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        while (!stop) {
            while (activeConnectionCount.intValue() < maxConnectionCount) {
                S socket = serverSocket.accept();
                CONNECTIONS.add(new ActiveServerConnection<>(socket, interpreterC, packC, this));
                activeConnectionCount.incrementAndGet();
            }
            Thread.onSpinWait();
        }
        serverSocket.close();
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public void removeClosedActiveConnection(ActiveServerConnection<T, S, P, I> connection) {
        CONNECTIONS.remove(connection);
        activeConnectionCount.decrementAndGet();
    }

    public ActiveServerConnection<T, S, P, I> getConnection(int index) {
        return CONNECTIONS.get(index);
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

    @Override
    public void terminate() throws IOException {
        close();
        serverSocket.close();
        synchronized (CONNECTIONS) {
            CONNECTIONS.forEach(spiActiveServerConnection -> {
                try {
                    spiActiveServerConnection.terminate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public boolean haveAllConnectionsBeenClosed() {
        synchronized (CONNECTIONS) {
            return getRemainingConnections().isEmpty();
        }
    }

    public List<ActiveServerConnection<T, S, P, I>> getRemainingConnections() {
        if (!stop || !serverSocket.isClosed()) {
            throw new IllegalStateException("Server not closed");
        }
        List<ActiveServerConnection<T, S, P, I>> remainingConnections = new ArrayList<>();
        synchronized (CONNECTIONS) {
            CONNECTIONS.forEach(activeServerConnection -> {
                if (activeServerConnection.isOpen()) {
                    remainingConnections.add(activeServerConnection);
                }
            });
        }
        return remainingConnections;
    }
}
