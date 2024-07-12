package ch.giuntini.netjlo_fos.connections.server.singefile;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_fos.connections.FileConnectionMode;
import ch.giuntini.netjlo_fos.connections.client.SingleFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.io.File;
import java.io.IOException;

public class SingleFileServerConnection<T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        implements Acceptable, Disconnectable {

    private SingleFileConnection<BaseSocket, I> connection;
    private T serverSocket;
    private FileConnectionMode mode;
    private String rootPathForFiles;
    private Class<I> interpreterC;
    private File file;

    private SingleFileServerConnection() {
    }

    public SingleFileServerConnection(T serverSocket, File file) {
        this.serverSocket = serverSocket;
        this.file = file;
        mode = FileConnectionMode.Send;
    }

    public SingleFileServerConnection(T serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this.serverSocket = serverSocket;
        this.rootPathForFiles = rootPathForFiles;
        this.interpreterC = interpreterC;
        mode = FileConnectionMode.Receive;
    }

    public FileConnectionMode getMode() {
        return mode;
    }

    @Override
    public void acceptAndWait() throws IOException {
        BaseSocket socket = serverSocket.accept();
        switch (mode) {
            case Send: {
                connection = new SingleFileConnection<>(socket, file);
                break;
            }
            case Receive: {
                connection = new SingleFileConnection<>(socket, rootPathForFiles, interpreterC);
                break;
            }
        }
    }

    @Override
    public void disconnect() throws IOException {
        connection.disconnect();
        serverSocket.close();
    }
}
