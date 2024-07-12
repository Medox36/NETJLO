package ch.giuntini.netjlo_core.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.packages.BasePackage;
import ch.giuntini.netjlo_core.connections.client.Connection;
import ch.giuntini.netjlo_core.interpreter.Interpretable;
import ch.giuntini.netjlo_core.streams.PackageObjectInputStream;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ReceiverThread<S extends BaseSocket, P extends BasePackage<?>, I extends Interpretable<P>>
        extends Thread implements AutoCloseable {

    protected PackageObjectInputStream<P> objectInputStream;
    protected final Connection<S, P, I> connection;
    protected final S socket;
    protected final I interpreter;
    protected volatile boolean stop;

    public ReceiverThread(Connection<S, P, I> connection, S socket, Class<I> interpreterC, Class<P> packC) {
        super("Receiving-Thread");
        this.connection = connection;
        this.socket = socket;
        try {
            objectInputStream = new PackageObjectInputStream<>(new BufferedInputStream(socket.getInputStream()), packC);
            interpreter = interpreterC.getConstructor().newInstance();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                @SuppressWarnings("unchecked")
                P p = (P) objectInputStream.readObject();
                interpreter.interpret(p);
            } catch (EOFException e) {
                close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                close();
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitIn(socket, objectInputStream, connection, stop);
    }

    @Override
    public void close() {
        stop = true;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        close();
    }
}
