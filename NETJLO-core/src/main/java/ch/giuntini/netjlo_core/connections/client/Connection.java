package ch.giuntini.netjlo_core.connections.client;

import ch.giuntini.netjlo_core.socket.Connectable;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.socket.Send;
import ch.giuntini.netjlo_core.socket.Terminable;
import ch.giuntini.netjlo_core.threads.ReceiverThread;
import ch.giuntini.netjlo_core.threads.SenderThread;

import java.io.IOException;
import java.util.List;

public class Connection<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        implements Connectable, Disconnectable, Send<P>, Terminable {

    protected S socket;
    private SenderThread<S, P, I> senderThread;
    private ReceiverThread<S, P, I> receiverThread;

    protected Connection() {
    }

    public Connection(S socket, Class<I> interpreterC, Class<P> packC) {
        this.socket = socket;
        senderThread = new SenderThread<>(this, socket);
        receiverThread = new ReceiverThread<>(this, socket, interpreterC, packC);
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
        receiverThread.start();
        senderThread.start();
    }

    public boolean isOpen() {
        return !socket.isClosed();
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
        if (socket.isClosed()) {
            throw new IllegalStateException("Cannot send pack to a closed connection");
        }
        senderThread.addPackageToSendStack(pack);
    }

    @Override
    public void terminate() throws IOException {
        if (!socket.isClosed()) {
            senderThread.interrupt();
            receiverThread.interrupt();
            socket.disconnect();
        }
    }

    public boolean haveAllPackagesBeenSent() {
        return getRemainingPackages().isEmpty();
    }

    public List<P> getRemainingPackages() {
        return senderThread.getRemainingPackages();
    }
}
