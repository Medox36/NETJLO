package ch.giuntini.netjlo_fos.threads;

import ch.giuntini.netjlo_core.connections.client.sockets.BaseSocket;
import ch.giuntini.netjlo_core.threads.ThreadCommons;
import ch.giuntini.netjlo_fos.connections.client.FileConnection;
import ch.giuntini.netjlo_fos.interpreter.Interpretable;
import ch.giuntini.netjlo_fos.packages.FilePartPackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileSenderThread<S extends BaseSocket, I extends Interpretable>
        extends Thread implements AutoCloseable {

    private final FileConnection<S, I> connection;
    protected ObjectOutputStream objectOutputStream;
    private final S socket;
    protected final ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<>();
    private volatile boolean stop;

    public FileSenderThread(FileConnection<S, I> connection, S socket) {
        super("Sender-Thread");
        this.connection = connection;
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        runLoop:
        while (!stop) {
            while (!files.isEmpty()) {
                try {
                    cycle(files.poll());
                } catch (IOException e) {
                    e.printStackTrace();
                    break runLoop;
                }
            }
            Thread.onSpinWait();
        }
        ThreadCommons.onExitOut(socket, objectOutputStream, connection, stop);
    }

    protected void cycle(File file) throws IOException {
        if (file != null) {
            FileReader fr = new FileReader(file);
            objectOutputStream.writeObject(new FilePartPackage(file.getName()));
            objectOutputStream.flush();
            char[] buff = new char[8192];
            while (fr.read(buff) > 0) {
                objectOutputStream.writeObject(new FilePartPackage(String.valueOf(buff)));
                objectOutputStream.flush();
            }
            fr.close();
            objectOutputStream.writeObject(new FilePartPackage(true));
            objectOutputStream.flush();
        }
    }

    public void addFileToSendStack(File file) {
        if (file == null || !file.exists() || !file.canRead() || file.isDirectory())
            throw new IllegalArgumentException("The file to send can't be null, can't not exist can't be unreadable and can't be a directory");
        files.add(file);
    }

    @Override
    public void close() {
        stop = true;
    }
}
