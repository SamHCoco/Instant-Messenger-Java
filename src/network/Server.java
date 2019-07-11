package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private static int users = 0;
    private static HashMap<String, PrintWriter> clientDictionairy = new HashMap<>();

    public static void main(String[] args) {
	    try(ServerSocket server = new ServerSocket(2000)){
	        while(true){
                new ClientHandler(server.accept()).start();
            }
        }catch(IOException e){
            System.out.println("SERVER ERROR: " + e.getMessage());
        }
    }

    public static class ClientHandler extends Thread {
        private Socket socket;
        private String clientName;
        private String receivedData;

        public ClientHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            try{
                Scanner input = new Scanner(socket.getInputStream());
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                // server processes clients inputted username
                while(true){
                    clientName = input.nextLine().toUpperCase();
                    if(clientDictionairy.containsKey(clientName)){
                        output.print(clientName + " IS ALREADY TAKEN \nENTER USERNAME: ");
                        clientName = input.nextLine().toUpperCase();
                    } else{
                        clientDictionairy.put(clientName, output);
                        output.println(clientName + " has joined the chat!");
                        break;
                    }
                }

                // message from server indicating how many clients are connected
                users ++;
                if(users == 1){
                    System.out.println(users +  " CLIENT CONNECTED");
                } else if(users >= 2){
                    System.out.println(users +  " CLIENTS CONNECTED");
                }

                while(true){
                    receivedData = input.nextLine();
                    if(receivedData.equals("``quit")){
                        break;
                    }
                    for(String name : clientDictionairy.keySet()){
                      if(!name.equals(clientName)){
                          clientDictionairy.get(name).println(name + " ->> " + receivedData);
                      }
                    }

                }
            } catch(IOException e){
                System.out.println("CLIENT HANDLER ERROR: " + e.getMessage());
            } finally {
                try{
                    socket.close();
                } catch(IOException e){
                    System.out.println("CLIENT HANDLER - ERROR CLOSING SOCKET: " + e.getMessage());
                }
            }
        }
    }
}
