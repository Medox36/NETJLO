package ch.giuntini.netjlo_core.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SenderThread<S extends BaseSocket,P extends BasePackage<?>, I extends Interpretable<P>>
        extends Thread implements AutoCloseable {

    private final ConcurrentLinkedQueue<P> packages = new ConcurrentLinkedQueue<>();
    private ObjectOutputStream objectOutputStream;
    private final Connection<S, P, I> connection;
    private final S socket;
    private volatile boolean stop;

    public SenderThread(Connection<S, P, I> connection, S socket) {
        super("Sender-Thread");
        this.connection = connection;
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        runLoop:
        while (!stop) {
            while (!packages.isEmpty() && Thread.currentThread().isInterrupted()) {
                try {
                    P p = packages.poll();
                    if (p != null) {
                        objectOutputStream.writeObject(p);
                        objectOutputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break runLoop;
                }
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitOut(socket, objectOutputStream, connection, stop);
    }

    public void addPackageToSendStack(P p) {
        if (stop) {
            throw new IllegalStateException("Sender thread is stopped");
        }
        if (socket.isOutputShutdown()) {
            throw new IllegalStateException("Output of this socket is closed");
        }
        packages.add(p);
    }

    public List<P> getRemainingPackages() {
        if (!stop || !socket.isClosed()) {
            throw new IllegalStateException("Sender thread is still running");
        }
        return new ArrayList<>(packages);
    }

    @Override
    public void close() {
        stop = true;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        close();
    }
}
