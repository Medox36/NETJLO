package ch.giuntini.netjlo_fos.connections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.socket.Send;

import java.io.File;
import java.io.IOException;

public class FileServerConnection<T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        implements Acceptable, Disconnectable, Send {

    private FileConnection<S, I> connection;
    private final T serverSocket;
    private final Class<I> interpreterC;
    private final String rootPathForFiles;

    public FileServerConnection(T serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.rootPathForFiles = rootPathForFiles;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        S socket = serverSocket.accept();
        connection = new FileConnection<>(socket, rootPathForFiles, interpreterC);
    }

    @Override
    public void disconnect() throws IOException {
        connection.disconnect();
        serverSocket.close();
    }

    @Override
    public void send(File file) {
        connection.send(file);
    }
}
