package ch.giuntini.netjlo_timed.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_timed.connections.client.TimedConnection;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimedSenderThread<S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        extends Thread implements AutoCloseable {

    private final ConcurrentLinkedQueue<P> packages = new ConcurrentLinkedQueue<>();
    private ObjectOutputStream objectOutputStream;
    private final TimedConnection<S, P, I> connection;
    private final S socket;
    private final Timer timer;
    private volatile boolean stop;

    public TimedSenderThread(TimedConnection<S, P, I> connection, S socket) {
        super("Timed-Sender-Thread");
        this.connection = connection;
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Timer("Daemon TimedPackage Sender-Thread Timer", true);
    }

    @Override
    public void run() {
        while (!stop) {
            while (!packages.isEmpty()) {
                try {
                    P p = packages.poll();
                    if (p != null) {
                        objectOutputStream.writeObject(p);
                        objectOutputStream.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitOut(socket, objectOutputStream, connection, stop);
    }

    public void addPackageToSendStack(P p) {
        packages.add(p);
    }

    public synchronized void addPackageToSendStack(P p, Date timestamp) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addPackageToSendStack(p);
            }
        }, timestamp);
    }

    /**
     *
     * @param p package to send
     * @param delay in milliseconds
     */
    public synchronized void addPackageToSendStack(P p, int delay) {
        addPackageToSendStack(p, Date.from(Instant.now().plus(delay, ChronoUnit.MILLIS)));
    }

    @Override
    public void close() {
        stop = true;
    }
}
