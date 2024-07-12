package ch.giuntini.netjlo_demanding.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.threads.ReceiverThread;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DemandingReceiverThread<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends ReceiverThread<S, P, I> {

    private final ConcurrentLinkedQueue<P> packages;
    private final DemandingInterpreterThread<P, I> interpreterThread;

    public DemandingReceiverThread(Connection<S, P, I> connection, S socket, Class<I> interpreterC, Class<P> packC) {
        super(connection, socket, interpreterC, packC);
        packages = new ConcurrentLinkedQueue<>();
        interpreterThread = new DemandingInterpreterThread<>(packages, interpreter);
    }

    @Override
    public void run() {
        interpreterThread.start();
        while (!stop) {
            try {
                @SuppressWarnings("unchecked")
                P p = (P) objectInputStream.readObject();
                packages.add(p);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitIn(socket, objectInputStream, connection, stop);
        interpreterThread.close();
    }
}
