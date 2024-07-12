package ch.giuntini.netjlo_fos.connections.server.single.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.Acceptable;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_core.socket.Disconnectable;
import ch.giuntini.netjlo_fos.connections.client.zip.ZipFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.socket.Send;

import java.io.File;
import java.io.IOException;

public class ZipFileServerConnection<T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        implements Acceptable, Disconnectable, Send {

    private ZipFileConnection<S, I> connection;
    private final T serverSocket;
    private final Class<I> interpreterC;
    private final String rootPathForFiles;
    private final long zipThreshold;
    private final boolean unzipDirs;

    public ZipFileServerConnection(T serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this(serverSocket, rootPathForFiles, 536870912, true, interpreterC);
    }

    public ZipFileServerConnection(
            T serverSocket,
            String rootPathForFiles,
            long zipThreshold,
            boolean unzipDirs,
            Class<I> interpreterC
    ) {
        this.serverSocket = serverSocket;
        this.rootPathForFiles = rootPathForFiles;
        this.zipThreshold = zipThreshold;
        this.unzipDirs = unzipDirs;
        this.interpreterC = interpreterC;
    }

    @Override
    public void acceptAndWait() throws IOException {
        S socket = serverSocket.accept();
        connection = new ZipFileConnection<>(socket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC);
    }

    @Override
    public void disconnect() throws IOException {
        connection.disconnect();
    }

    @Override
    public void send(File file) {
        connection.send(file);
    }
}
