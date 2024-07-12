package ch.giuntini.netjlo_fos.connections.client;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.FileConnectionMode;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.threads.SimpleOneFileReceiverThread;
import ch.giuntini.netjlo_fos.threads.SimpleOneFileSenderThread;

import java.io.File;
import java.io.IOException;

public class SingleFileConnection<S extends BaseSocket, I extends Interpretable> extends FileConnection<S, I> {

    private S socket;
    private FileConnectionMode mode;

    private SingleFileConnection() {
    }

    public SingleFileConnection(S socket, File file) {
        this.socket = socket;
        senderThread = new SimpleOneFileSenderThread<>(this, socket, file);
        mode = FileConnectionMode.Send;
        try {
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SingleFileConnection(S socket, String rootPathForFiles, Class<I> interpreterC) {
        this.socket = socket;
        receiverThread = new SimpleOneFileReceiverThread<>(this, socket, rootPathForFiles, interpreterC);
        mode = FileConnectionMode.Receive;
        try {
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConnectionMode getMode() {
        return mode;
    }

    @Override
    public void connect() throws IOException {
        socket.connect();
        switch (mode) {
            case Send: {
                senderThread.start();
                break;
            }
            case Receive: {
                receiverThread.start();
                break;
            }
        }
    }

    @Override
    public void disconnect() throws IOException {
        socket.disconnect();
    }
}
