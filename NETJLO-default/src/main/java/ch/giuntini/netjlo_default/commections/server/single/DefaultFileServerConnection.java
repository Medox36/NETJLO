package ch.giuntini.netjlo_default.commections.server.single;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_default.commections.server.sockets.DefaultServerSocket;
import ch.giuntini.netjlo_fos.connections.server.single.FileServerConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

public class DefaultFileServerConnection<I extends Interpretable>
        extends FileServerConnection<DefaultServerSocket, BaseSocket, I> {

    public DefaultFileServerConnection(DefaultServerSocket serverSocket, String rootPathForFiles, Class<I> interpreterC) {
        super(serverSocket, rootPathForFiles, interpreterC);
    }
}
