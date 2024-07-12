package ch.giuntini.netjlo_default.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.threads.ReceiverThread;
import ch.giuntini.netjlo_default.commections.client.DefaultConnection;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultReceiverThread
        <I extends Interpretable<DefaultPackage>> extends ReceiverThread<BaseSocket, DefaultPackage, I> {

    public DefaultReceiverThread(DefaultConnection<I> connection, BaseSocket socket, Class<I> interpreterC) {
        super(connection, socket, interpreterC, DefaultPackage.class);
        setName("Default-Receiver-Thread");
    }
}
