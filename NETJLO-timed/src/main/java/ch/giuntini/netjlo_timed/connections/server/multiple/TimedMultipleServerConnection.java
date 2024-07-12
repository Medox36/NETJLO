package ch.giuntini.netjlo_timed.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimedMultipleServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        implements Acceptable, AutoCloseable {

    private final Class<T> serverSocketC;
    private final Class<S> socketC;
    private final Class<P> packC;
    private final Class<I> interpreterC;

    private final T serverSocket;
    private final AtomicInteger activeConnectionCount = new AtomicInteger(0);
    private volatile int maxConnectionCount = 5;
    private boolean stop;

    private final List<TimedActiveServerConnection<T, S, P, I>> CONNECTIONS = Collections.synchronizedList(new LinkedList<>());


    @SuppressWarnings("unchecked")
    public TimedMultipleServerConnection(T serverSocket, Class<S> socketC, Class<P> packC, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.serverSocketC = (Class<T>) serverSocket.getClass();
        this.socketC = socketC;
        this.packC = packC;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        while (!stop) {
            while (activeConnectionCount.intValue() < maxConnectionCount) {
                S socket = serverSocket.accept();
                CONNECTIONS.add(new TimedActiveServerConnection<>(socket, interpreterC, packC, this));
                activeConnectionCount.incrementAndGet();
            }
            Thread.onSpinWait();
        }
    }

    public void setMaxConnectionCount(int maxConnectionCount) {
        this.maxConnectionCount = maxConnectionCount;
    }

    public synchronized void removeClosedActiveConnection(TimedActiveServerConnection<T, S, P, I> connection) {
        CONNECTIONS.remove(connection);
        activeConnectionCount.decrementAndGet();
    }

    public Class<?>[] getTypes() {
        return new Class[]{serverSocketC, socketC, packC, interpreterC};
    }

    @Override
    public void close() {
        stop = true;
    }
}
