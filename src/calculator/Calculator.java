package calculator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Calculator {

    private final ServerSocket server;

    public Calculator() throws IOException {
        server = new ServerSocket(3333);
        System.out.println("server running on port 3333");
    }

    public void handleClients() {

        while (true) {
            try {
                new Handler(server.accept()).start();
                System.out.println("client connected");
            } catch (IOException e) {
                System.out.println("SERVER-LOG: Hiba a kliensek fogadasakor.");
                break;
            }
        }
    }

    private static class Handler extends Thread {

        private final Socket client;
        private final PrintWriter pw;
        private final Scanner sc;

        public Handler(Socket s1) throws IOException {
            client = s1;
            pw = new PrintWriter(client.getOutputStream(), true);
            sc = new Scanner(client.getInputStream());
        }

        @Override
        public void run() {
            String pack = null;
            while (true) {
                System.out.println("Szerver inputra vár");
                pack = sc.nextLine();
                System.out.println(pack);
                if (pack.equals("exit")) {
                    break;
                }
                String[] parts = pack.split(" ");
                double a = Double.parseDouble(parts[0]);
                double b = Double.parseDouble(parts[1]);
                switch (parts[2]) {
                    case "+":
                        pw.println(a + b);
                        break;
                    case "-":
                        pw.println(a - b);
                        break;
                    case "*":
                        pw.println(a * b);
                        break;
                    case "/":
                        pw.println(a / b);
                        break;
                    case "%":
                        pw.println(a % b);
                        break;
                }
                pw.flush();
                System.out.println("szerver elküldte");
                //break;
            }
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.close();
            sc.close();
            

        }
    }

    public static void main(String[] args) throws IOException {
        Calculator c = new Calculator();
        c.handleClients();
    }

}
