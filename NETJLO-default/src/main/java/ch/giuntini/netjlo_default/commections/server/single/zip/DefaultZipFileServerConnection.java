package ch.giuntini.netjlo_default.commections.server.single.zip;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.single.zip.ZipFileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultZipFileServerConnection<I extends Interpretable>
        extends ZipFileServerConnection<DefaultServerSocket, BaseSocket, I> {

    public DefaultZipFileServerConnection(DefaultServerSocket serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        this(serverSocket, rootPathForFiles, 536870912, true, interpreterC);
    }

    public DefaultZipFileServerConnection(
            DefaultServerSocket serverSocket,
            String rootPathForFiles,
            long zipThreshold,
            boolean unzipDirs,
            Class<I> interpreterC
    ) {
        super(serverSocket, rootPathForFiles, zipThreshold, unzipDirs, interpreterC);
    }
}