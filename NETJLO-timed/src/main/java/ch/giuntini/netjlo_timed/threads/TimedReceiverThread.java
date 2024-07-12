package ch.giuntini.netjlo_timed.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_timed.connections.client.TimedConnection;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;
import ch.giuntini.netjlo_timed.streams.TimedPackageObjectInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TimedReceiverThread<S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        extends Thread implements AutoCloseable {

    private final TimedReceiverInterpretThread<P, I> timedReceiverInterpretThread;

    private final ConcurrentLinkedQueue<P> packages = new ConcurrentLinkedQueue<>();
    private final TimedPackageObjectInputStream<P> objectInputStream;
    private final TimedConnection<S, P, I> connection;
    private final S socket;
    private final Timer timer;
    private volatile boolean stop;

    public TimedReceiverThread(TimedConnection<S, P, I> connection, S socket, Class<I> interpreterC, Class<P> packC) {
        super("Timed-Receiving-Thread");
        this.connection = connection;
        this.socket = socket;
        I interpreter;
        try {
            objectInputStream = new TimedPackageObjectInputStream<>(new BufferedInputStream(socket.getInputStream()), packC);
            interpreter = interpreterC.getConstructor().newInstance();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        timer = new Timer("Daemon TimedPackage Receiver-Thread Timer", true);
        timedReceiverInterpretThread = new TimedReceiverInterpretThread<>(packages, interpreter);
    }

    @Override
    public void run() {
        timedReceiverInterpretThread.start();
        while (!stop) {
            try {
                @SuppressWarnings("unchecked")
                P p = (P) objectInputStream.readObject();
                packageValuation(p);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitIn(socket, objectInputStream, connection, stop);
        timedReceiverInterpretThread.close();
    }

    private synchronized void packageValuation(P p) {
        Date now = Date.from(Instant.now());
        if (p.timeStamp == null || p.timeStamp.before(now)) {
            addForInterpretation(p);
        }
        if (p.timeStamp.after(now)) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    addForInterpretation(p);
                }
            }, p.timeStamp);
        }
    }

    private void addForInterpretation(P p) {
        packages.add(p);
    }

    @Override
    public void close() {
        stop = true;
    }
}
