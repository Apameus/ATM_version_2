package gr.apameus.atm.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }


    public String receive(){
        try {
            return in.readUTF();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg){
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
