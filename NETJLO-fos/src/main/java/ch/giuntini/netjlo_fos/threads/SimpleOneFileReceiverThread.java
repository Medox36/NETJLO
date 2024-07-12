package ch.giuntini.netjlo_fos.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.io.IOException;

public class SimpleOneFileReceiverThread<S extends BaseSocket, I extends Interpretable>
        extends FileReceiverThread<S, I> {

    public SimpleOneFileReceiverThread(
            FileConnection<S, I> connection,
            S socket,
            String rootPathForFiles,
            Class<I> interpreterC) {
        super(connection, socket, rootPathForFiles, interpreterC);
    }

    @Override
    public void run() {
        try {
            cycle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            if (!socket.isInputShutdown()) socket.shutdownInput();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
