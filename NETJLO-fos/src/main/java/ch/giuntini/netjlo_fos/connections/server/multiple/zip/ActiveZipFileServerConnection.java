package ch.giuntini.netjlo_fos.connections.server.multiple.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_fos.connections.client.zip.ZipFileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.io.IOException;

public class ActiveZipFileServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        extends ZipFileConnection<S, I> {

    private final MultipleZipFileServerConnection<T, S, I> parent;

    protected ActiveZipFileServerConnection(
            S socket,
            String rootPathForFiles,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<T,S,I> parent
    ) {
        super(socket, rootPathForFiles, interpreterC);
        this.parent = parent;
    }

    public ActiveZipFileServerConnection(
            S socket,
            String rootPathForFiles,
            long zipThreshold,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<T, S, I> parent
    ) {
        super(socket, rootPathForFiles, zipThreshold, interpreterC);
        this.parent = parent;
    }

    public ActiveZipFileServerConnection(
            S socket,
            String rootPathForFiles,
            boolean unzipDirs,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<T, S, I> parent
    ) {
        super(socket, rootPathForFiles, unzipDirs, interpreterC);
        this.parent = parent;
    }

    public ActiveZipFileServerConnection(
            S socket,
            String rootPathForFiles,
            long zipThreshold,
            boolean unzipDirs,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<T, S, I> parent
    ) {
        super(socket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC);
        this.parent = parent;
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        parent.removeClosedActiveConnection(this);
    }
}
