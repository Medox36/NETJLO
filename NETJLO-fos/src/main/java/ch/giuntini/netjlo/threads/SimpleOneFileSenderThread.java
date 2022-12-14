package ch.giuntini.netjlo.threads;

import ch.giuntini.netjlo.connections.client.sockets.DefaultSocket;
import ch.giuntini.netjlo.packages.FilePartPackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SimpleOneFileSenderThread<S extends DefaultSocket> extends Thread {

    private ObjectOutputStream objectOutputStream;
    private final S socket;
    private final File file;

    public SimpleOneFileSenderThread(S socket, File file) {
        super("Sender-Thread");
        this.socket = socket;
        if (file == null || !file.exists() || !file.canRead() || file.isDirectory())
            throw new IllegalArgumentException("The file to send can't be null, can't not exist can't be unreadable and can't be a directory");
        this.file = file;

        try {
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        cycle();
        try {
            socket.shutdownOutput();
            //TODO check if stream isn't already closed through the shutdownOutput() method
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cycle() {
        if (file != null) {
            try (FileReader fr = new FileReader(file)) {
                objectOutputStream.writeObject(new FilePartPackage(file.getName()));
                objectOutputStream.flush();
                char[] buff = new char[8192];
                while (fr.read(buff) > 0) {
                    objectOutputStream.writeObject(new FilePartPackage(String.valueOf(buff)));
                    objectOutputStream.flush();
                }
                objectOutputStream.writeObject(new FilePartPackage(true));
                objectOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
