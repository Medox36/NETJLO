package ch.giuntini.netjlo_fos.connections.server.multiple;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.connections.server.sockets.BaseServerSocket;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;

import java.io.IOException;

public class ActiveFileServerConnection
        <T extends BaseServerSocket<S>, S extends BaseSocket, I extends Interpretable>
        extends FileConnection<S, I> {

    private final MultipleFileServerConnection<T, S, I> parent;

    protected ActiveFileServerConnection(
            S socket,
            String rootPathForFiles,
            Class<I> interpreterC,
            MultipleFileServerConnection<T,S,I> parent
    ) {
        super(socket, rootPathForFiles, interpreterC);
        this.parent = parent;
    }

    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        parent.removeClosedActiveConnection(this);
    }
}
