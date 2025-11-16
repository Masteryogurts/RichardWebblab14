import java.util.*;
import java.net.*;
import java.io.*;
import java.net.ServerSocket;
public class Client {
    private int ID;
    private String local;
    private Socket sock = null;
    private PrintWriter out;
    private BufferedReader in;
    public Client(String local, int ID) throws IOException{ 
        //we need something for output stream
        //something for input stream
        //and client needs tp create a socket with the host and ID;
        sock = new Socket(local, ID);
        out = new PrintWriter(sock.getOutputStream(), true); //getting most of this from the notes
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    public Socket getSocket(){
        return sock;
    }

    public void handshake(){
        out.println(12345);
        out.flush();;
    }

    public String request(String request) throws IOException{
        out.println(request);
        out.flush(); //out is gonna be what we use for prints and other statements
        return in.readLine(); //in is what we use to add/read
    }

    public void disconnect(){
        //the disconnect is just gonna be closing the socket
        try{
            sock.close();
        }catch(IOException e){
            System.err.println(local);
        }
    }
    
}
