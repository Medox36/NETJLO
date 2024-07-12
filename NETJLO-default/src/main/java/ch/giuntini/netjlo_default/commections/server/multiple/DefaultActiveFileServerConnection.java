package ch.giuntini.netjlo_default.commections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.multiple.ActiveFileServerConnection;
import ch.giuntini.netjlo_fos.connections.server.multiple.MultipleFileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultActiveFileServerConnection<I extends Interpretable>
        extends ActiveFileServerConnection<DefaultServerSocket, BaseSocket, I> {

    protected DefaultActiveFileServerConnection(
            BaseSocket socket,
            String rootPathForFiles,
            Class<I> interpreterC,
            MultipleFileServerConnection<DefaultServerSocket, BaseSocket, I> parent
    ) {
        super(socket, rootPathForFiles, interpreterC, parent);
    }
}
