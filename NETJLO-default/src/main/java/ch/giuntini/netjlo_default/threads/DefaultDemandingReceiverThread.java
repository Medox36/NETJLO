package ch.giuntini.netjlo_default.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_default.commections.client.DefaultConnection;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DefaultDemandingReceiverThread<I extends Interpretable<DefaultPackage>> extends DefaultReceiverThread<I> {

    private final ConcurrentLinkedQueue<DefaultPackage> packages;
    private final DefaultDemandingInterpreterThread<I> interpreterThread;

    public DefaultDemandingReceiverThread(DefaultConnection<I> connection, BaseSocket socket, Class<I> interpreterC) {
        super(connection, socket, interpreterC);
        packages = new ConcurrentLinkedQueue<>();
        interpreterThread = new DefaultDemandingInterpreterThread<>(packages, interpreter);
    }

    @Override
    public void run() {
        interpreterThread.start();
        while (!stop) {
            try {
                DefaultPackage p = (DefaultPackage) objectInputStream.readObject();
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
