package ch.giuntini.netjlo_demanding.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.threads.SenderThread;
import ch.giuntini.netjlo_demanding.threads.DemandingReceiverThread;

import java.io.IOException;

public class DemandingConnection<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends Connection<S, P, I> {

    private SenderThread<S, P, I> senderThread;
    private DemandingReceiverThread<S, P, I> receiverThread;

    protected DemandingConnection() {
    }

    public DemandingConnection(S socket, Class<I> interpreterC, Class<P> packC) {
        super.socket = socket;
        senderThread = new SenderThread<>(this, socket);
        receiverThread = new DemandingReceiverThread<>(this, socket, interpreterC, packC);
    }

    @Override
    public void connect() throws IOException {
        super.socket.connect();
        receiverThread.start();
        senderThread.start();
    }

    @Override
    public void disconnect() throws IOException {
        if (!super.socket.isClosed()) {
            senderThread.close();
            receiverThread.close();
            super.socket.disconnect();
        }
    }

    @Override
    public void send(P pack) {
        senderThread.addPackageToSendStack(pack);
    }
}
