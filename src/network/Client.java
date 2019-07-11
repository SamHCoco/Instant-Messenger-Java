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

    public static void main(String[] args){
        try(Socket socket = new Socket("localhost", 2000)){
            socket.setSoTimeout(50);
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
            System.out.print("ENTER USER NAME: ");
            clientName = scanner.nextLine().toUpperCase();
            output.println(clientName);
            System.out.println(input.nextLine());

            while(true){
                System.out.print(clientName + " ->> ");
                clientInput = scanner.nextLine();
                output.println(clientInput);
                if(input.hasNextLine()){
                    System.out.println(input.nextLine());
                }
            }
        }catch(IOException e){
            System.out.println("CLIENT ERROR: " + e.getMessage());
        }
    }

}
