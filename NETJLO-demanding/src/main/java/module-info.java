module NETJLO_demanding {
    requires NETJLO_core;

    exports ch.giuntini.netjlo_demanding.connections.client;
    exports ch.giuntini.netjlo_demanding.connections.server.multiple;
    exports ch.giuntini.netjlo_demanding.connections.server.single;
    exports ch.giuntini.netjlo_demanding.threads;
}