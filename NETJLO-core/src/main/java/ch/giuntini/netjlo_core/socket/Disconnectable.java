package ch.giuntini.netjlo_core.socket;

import java.io.IOException;

public interface Disconnectable {

    void disconnect() throws IOException;
}
