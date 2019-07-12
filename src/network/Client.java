package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String clientInput;
    private static String clientName;
    private static String receivedData;
    private static Scanner input;
    private static PrintWriter output;
    private static Scanner scanner;

    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 2000)){
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            while(true){
                System.out.print("ENTER USERNAME: ");
                clientName = scanner.nextLine().toUpperCase();
                output.println(clientName);
                receivedData = input.nextLine();
                System.out.println(receivedData);
                if(receivedData.equals(clientName + " has joined the chat!")){
                    break;
                }
            }

            boolean clientListenerOn = false;
            while(true){
                if(!clientListenerOn){
                    new ClientListener().start();
                    clientListenerOn = true;
                }
                System.out.print(clientName + " ->> ");
                clientInput = scanner.nextLine();
                output.println(clientInput);
                if(clientInput.equals("``quit")){
                    System.out.println("You have left the chat!");
                    break;
                }
            }
        } catch(IOException e){
            System.out.println("CLIENT ERROR: " + e.getMessage());
        }
    }

    public static class ClientListener extends Thread {

        /**
         * This method prevents the network IO blocking that would occur due to 'input.nextLine()' which blocks
         * the main client thread as the client waits for data from the server. A new thread is started to listen
         * to incoming data from the server instead.
         */
        @Override
        public void run(){
            if(input.hasNextLine()){
                receivedData = input.nextLine();
                System.out.println("\n"+receivedData);
            }
        }

    }
}
