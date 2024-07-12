package ch.giuntini.netjlo_default.commections.server.multiple.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.multiple.zip.ActiveZipFileServerConnection;
import ch.giuntini.netjlo_fos.connections.server.multiple.zip.MultipleZipFileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultActiveZipFileServerConnection<I extends Interpretable>
        extends ActiveZipFileServerConnection<DefaultServerSocket, BaseSocket, I> {

    protected DefaultActiveZipFileServerConnection(
            BaseSocket socket,
            String rootPathForFiles,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<DefaultServerSocket, BaseSocket, I> parent
    ) {
        super(socket, rootPathForFiles, interpreterC, parent);
    }

    public DefaultActiveZipFileServerConnection(
            BaseSocket socket,
            String rootPathForFiles,
            long zipThreshold,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<DefaultServerSocket, BaseSocket, I> parent
    ) {
        super(socket, rootPathForFiles, zipThreshold, interpreterC, parent);
    }

    public DefaultActiveZipFileServerConnection(
            BaseSocket socket,
            String rootPathForFiles,
            boolean unzipDirs,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<DefaultServerSocket, BaseSocket, I> parent
    ) {
        super(socket, rootPathForFiles, unzipDirs, interpreterC, parent);
    }

    public DefaultActiveZipFileServerConnection(
            BaseSocket socket,
            String rootPathForFiles,
            long zipThreshold,
            boolean unzipDirs,
            Class<I> interpreterC,
            MultipleZipFileServerConnection<DefaultServerSocket, BaseSocket, I> parent
    ) {
        super(socket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC, parent);
    }
}
