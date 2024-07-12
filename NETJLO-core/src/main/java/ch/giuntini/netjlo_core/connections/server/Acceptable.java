package ch.giuntini.netjlo_core.connections.server;

import java.io.IOException;

public interface Acceptable {

    void acceptAndWait() throws IOException;
}
