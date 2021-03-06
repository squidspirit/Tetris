package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map.Entry;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import application.main.Arguments;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Server started.");

        final String FILENAME = "ranking";
        final int TIMEOUT = 500;

        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Socket socket = null;
        String command = null;
        ServerSocket serverSocket = new ServerSocket(9217);

        try {
            while (true) {

                socket = serverSocket.accept();
                socket.setSoTimeout(TIMEOUT);
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                reader = new BufferedReader(inputStreamReader);
                writer = new BufferedWriter(outputStreamWriter);

                try {

                    System.out.println("New connection.");
                    command = readMessage(reader);
                    if (!command.equals(Arguments.VERSION)) {
                        sendMessage(writer, "REJECT");
                        continue;
                    }
                    sendMessage(writer, "ACCEPT");

                    while (true) {

                        command = readMessage(reader);

                        if (command.toUpperCase().equals("SEND")) {
                            sendMessage(writer, "ACCEPT");
                            command = readMessage(reader);
                            String[] requestList = command.split(",");
                            
                            TreeMultimap<Integer, String> multimap = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
                            multimap.put(Integer.parseInt(requestList[0]), requestList[1]);

                            try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(FILENAME)))) {
                                while (true) {
                                    String line = fileReader.readLine();
                                    if (line == null) break;
                                    multimap.put(
                                        Integer.parseInt(line.substring(0, 6)),
                                        line.substring(6)
                                    );
                                }
                            } catch (FileNotFoundException exception) {}

                            try (FileWriter fileWriter = new FileWriter(new File(FILENAME))) {
                                for (Entry<Integer, String> entry : multimap.entries()) {
                                    fileWriter.write(String.format("%06d%s\n", entry.getKey(), entry.getValue()));
                                }
                            }
                            sendMessage(writer, "ACCEPT");
                        }
                        else if (command.toUpperCase().equals("REQUEST")) {
                            sendMessage(writer, "ACCEPT");
                            try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(FILENAME)))) {
                                while (true) {
                                    command = readMessage(reader);
                                    if (!command.toUpperCase().equals("NEXT")) break;
                                    String line = fileReader.readLine();
                                    if (line == null) break;
                                    sendMessage(writer, String.format("%s %10s",
                                        line.substring(0, 6), line.substring(6)
                                    ));
                                }
                            } catch (FileNotFoundException exception) {}
                            sendMessage(writer, "DONE");
                        }
                        else if (command.toUpperCase().equals("BYE")) {
                            sendMessage(writer, "BYE");
                            break;
                        }
                        else sendMessage(writer, "REJECT");
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                } 

                socket.close();
                inputStreamReader.close();
                outputStreamWriter.close();
                reader.close();
                writer.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (socket != null) socket.close();
            if (inputStreamReader != null) inputStreamReader.close();
            if (outputStreamWriter != null) outputStreamWriter.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (serverSocket != null) serverSocket.close();
        }
    }



    private static void sendMessage(BufferedWriter writer, String message) throws IOException {
        System.out.println("[SERVER] " + message);
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    private static String readMessage(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        System.out.println("[CLIENT] " + line);
        return line;
    }
}
