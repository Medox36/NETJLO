package ch.giuntini.netjlo_fos.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_fos.connections.client.SingleFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.io.File;

public class SimpleOneFileSenderThread<S extends BaseSocket, I extends Interpretable> extends FileSenderThread<S, I> {

    public SimpleOneFileSenderThread(SingleFileConnection<S, I> connection, S socket, File file) {
        super(connection, socket);
        if (file == null || !file.exists() || !file.canRead() || file.isDirectory())
            throw new IllegalArgumentException("The file to send can't be null, must exist, can't be unreadable and can't be a directory");

        connection.send(file);
        close();
    }
}
