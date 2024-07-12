package ch.giuntini.netjlo_timed.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.socket.Connectable;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_timed.interpreter.Interpretable;
import ch.giuntini.netjlo_timed.packages.TimedPackage;
import ch.giuntini.netjlo_timed.sockets.TimedSend;
import ch.giuntini.netjlo_timed.threads.TimedReceiverThread;
import ch.giuntini.netjlo_timed.threads.TimedSenderThread;

import java.io.IOException;
import java.util.Date;

public class TimedConnection<S extends BaseSocket, P extends TimedPackage<?>, I extends Interpretable<P>>
        implements Connectable, Disconnectable, TimedSend<P> {

    private S socket;
    private TimedSenderThread<S, P, I> senderThread;
    private TimedReceiverThread<S, P, I> receiverThread;

    private TimedConnection() {
    }

    public TimedConnection(S socket, Class<I> interpreterC, Class<P> packC) {
        this.socket = socket;
        senderThread = new TimedSenderThread<>(this, socket);
        receiverThread = new TimedReceiverThread<>(this, socket, interpreterC, packC);
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
        senderThread.start();
        receiverThread.start();
    }

    @Override
    public void disconnect() throws IOException {
        if (!socket.isClosed()) {
            senderThread.close();
            receiverThread.close();
            socket.disconnect();
        }
    }


    @Override
    public void send(P pack) {
        senderThread.addPackageToSendStack(pack);
    }

    @Override
    public void send(P pack, int delay) {
        senderThread.addPackageToSendStack(pack, delay);
    }

    @Override
    public void send(P pack, Date timestamp) {
        senderThread.addPackageToSendStack(pack, timestamp);
    }
}
