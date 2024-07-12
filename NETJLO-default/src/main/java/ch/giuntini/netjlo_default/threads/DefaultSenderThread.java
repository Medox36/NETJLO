package ch.giuntini.netjlo_default.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.threads.SenderThread;
import ch.giuntini.netjlo_default.commections.client.DefaultConnection;
import ch.giuntini.netjlo_default.packages.DefaultPackage;

public class DefaultSenderThread<I extends Interpretable<DefaultPackage>>
        extends SenderThread<BaseSocket, DefaultPackage, I> {

    public DefaultSenderThread(DefaultConnection<I> connection, BaseSocket socket) {
        super(connection, socket);
        setName("Default-Sender-Thread");
    }
}
