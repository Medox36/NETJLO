package ch.giuntini.netjlo_default.commections.server.multiple.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.multiple.zip.MultipleZipFileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultMultipleZipFileServerConnection<I extends Interpretable>
        extends MultipleZipFileServerConnection<DefaultServerSocket, BaseSocket, I> {

    protected DefaultMultipleZipFileServerConnection(
            DefaultServerSocket serverSocket,
            String rootPathForFiles,
            Class<I> interpreterC
    ) {
        super(serverSocket, rootPathForFiles, interpreterC);
    }

    public DefaultMultipleZipFileServerConnection(
            DefaultServerSocket serverSocket,
            String rootPathForFiles,
            Class<I> interpreterC,
            long zipThreshold,
            boolean unzipDirs
    ) {
        super(serverSocket, rootPathForFiles, interpreterC, zipThreshold, unzipDirs);
    }
}
