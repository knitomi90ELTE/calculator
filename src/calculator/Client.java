package calculator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private PrintWriter pw;
    private Scanner serverOutput;
    private Scanner userInput;

    public Client() throws IOException {
        Socket client = new Socket("localhost", 3334);
        pw = new PrintWriter(client.getOutputStream(), true);
        serverOutput = new Scanner(client.getInputStream());
        userInput = new Scanner(System.in);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (process() == 0) {
                        break;
                    }
                }
            }
        }.start();
    }

    private int process() {
        int status = 1;
        String input = "";
        System.out.println("írj be valamit");
        input = userInput.nextLine();
        if(input.equals("exit")){
            status = 0;
        } else {
            pw.println(input);
            pw.flush();
            String answer = serverOutput.nextLine();
            System.out.println("eredmény: " + answer);
        }
        return status;
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

}
