package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.multiple.MultipleFileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultMultipleFileServerConnection<I extends Interpretable>
        extends MultipleFileServerConnection<DefaultServerSocket, BaseSocket, I> {

    protected DefaultMultipleFileServerConnection(
            DefaultServerSocket serverSocket,
            String rootPathForFiles,
            Class<I> interpreterC
    ) {
        super(serverSocket, rootPathForFiles, interpreterC);
    }
}
