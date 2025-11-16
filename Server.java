import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket server; // a server socket for the server
    private ArrayList<LocalDateTime> date = new ArrayList<>();

    public Server(int year) {
        try {
            server = new ServerSocket(year);
        } catch (IOException e) {
            System.out.println("error");
        }

    }

    // needs to run for as many times as there are clients
    public int serve(int clients) throws IOException {
        try{
            for (int i = 0; i < clients; i++) {

                Socket client = server.accept(); // listens to see if there is a connection to accept, then it accepts it
                // same thing we did in Client, needs to be done in server as well. Only way to
                // do this I think
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true); // sets autoFlush to true, useful I
                                                                                // think? Doing it cause it says to in
                                                                                // the definition
                String handshake = in.readLine(); // reads the file and checks
                if (handshake == null || !handshake.equals("12345")) {
                    out.println("couldn't handshake");
                    client.close();// close socket
                    continue; // continue the loop
                }

                // now we need a way to handle multiple clients, thus we need to use threads
                date.add(LocalDateTime.now());


                new Thread(() -> helper(client)).start();
            }
        }catch(IOException e){
            System.out.println("caught");
        }
        return clients;
    }

    // gonna use a helper method for it, otherwise it would be hard to handle
    // multiple at once since we need threads
    //this thing cannot throw an IOException apparently, it catches too fast and fails to work 
    private void helper(Socket client){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            String read;
            while ((read = in.readLine()) != null) {
                try{
                    long n = Long.parseLong(read); // using this to convert text to integer, after all we are reading from a
                                                // string
                    if (n > Integer.MAX_VALUE) { // this is to get the largest integer which is somehwere around 2.1 million
                        out.println("There was an exception on the server");
                        continue; // continue with the loop
                    }
                    out.println("The number " + n + " has " + countFactors((int) n) + " factors"); //typecast
                }catch (Exception e){
                    out.println("There was an exception on the server");
                }
            }
        }catch (IOException e){
            System.out.println("Couldnt handshake");
        }finally{
            try{
                client.close();

            }catch (IOException e){
                System.out.println("failed to close client");
            }
        }
        
    }

    //this method checks to see how many factors (divisors) a number n has
    //count is used to find total number of factors
    //Loops from i to the square root of n
    //We use modulus to check if something divides evenly from that n, if it does then we
    //increment count which allows us to find the number of factors easily
    private int countFactors(int n){
        int count = 0;
        for (int i  = 1; i * (long) i  <= n; i++){
            if (n % i == 0){
                count++;
                if (i != n / i){
                    count++;
                }
            }
        }
        return count;
    }

    public void disconnect() {
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("failed to close");
        }
    }

    public ArrayList<LocalDateTime> getConnectedTimes() {
        return date; // has already been done earlier
    }

}
