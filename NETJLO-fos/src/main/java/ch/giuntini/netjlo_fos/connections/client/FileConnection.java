package ch.giuntini.netjlo_fos.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.socket.Connectable;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.socket.Send;
import ch.giuntini.netjlo_fos.threads.FileReceiverThread;
import ch.giuntini.netjlo_fos.threads.FileSenderThread;

import java.io.File;
import java.io.IOException;

public class FileConnection<S extends BaseSocket, I extends Interpretable>
        implements Connectable, Disconnectable, Send {

    protected S socket;
    protected FileSenderThread<S, I> senderThread;
    protected FileReceiverThread<S, I> receiverThread;

    protected FileConnection() {
    }

    public FileConnection(S socket, String rootPathForFiles, Class<I> interpreterC) {
        this.socket = socket;
        senderThread = new FileSenderThread<>(this, socket);
        receiverThread = new FileReceiverThread<>(this, socket, rootPathForFiles, interpreterC);
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
    public void send(File file) {
        senderThread.addFileToSendStack(file);
    }
}