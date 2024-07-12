module NETJLO_fos {
    requires NETJLO_core;

    exports ch.giuntini.netjlo_fos.connections;
    exports ch.giuntini.netjlo_fos.connections.client;
    exports ch.giuntini.netjlo_fos.connections.client.zip;
    exports ch.giuntini.netjlo_fos.connections.server.multiple;
    exports ch.giuntini.netjlo_fos.connections.server.multiple.zip;
    exports ch.giuntini.netjlo_fos.connections.server.singefile;
    exports ch.giuntini.netjlo_fos.connections.server.single;
    exports ch.giuntini.netjlo_fos.connections.server.single.zip;
    exports ch.giuntini.netjlo_fos.interpreter;
    exports ch.giuntini.netjlo_fos.packages;
    exports ch.giuntini.netjlo_fos.socket;
    exports ch.giuntini.netjlo_fos.streams;
    exports ch.giuntini.netjlo_fos.threads;
    exports ch.giuntini.netjlo_fos.threads.zip;
    exports ch.giuntini.netjlo_fos.zip;
}