package socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  public static void main(String[] args) {
    Socket socket = null;
    try {
      socket = new Socket("127.0.0.1", 8888);
      Scanner in = new Scanner(socket.getInputStream());
      System.out.println("Server response: " + in.nextLine());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}