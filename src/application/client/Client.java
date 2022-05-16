package application.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements AutoCloseable {
    
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public Client(String host, int port) throws Exception {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 500);
        socket.setSoTimeout(500);
        System.out.println("Connected.");
        inputStreamReader = new InputStreamReader(socket.getInputStream());
        outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        bufferedWriter = new BufferedWriter(outputStreamWriter);
    }

    public String send(String message) throws Exception {
        System.out.println("[CLIENT] " + message);
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        String response = bufferedReader.readLine();
        System.out.println("[SERVER] " + response);
        return response;
    }

    @Override
    public void close() throws Exception {
        send("BYE");
        if (socket != null) socket.close();
        if (inputStreamReader != null) inputStreamReader.close();
        if (outputStreamWriter != null) outputStreamWriter.close();
        if (bufferedReader != null) bufferedReader.close();
        if (bufferedWriter != null) bufferedWriter.close();
    }
}
