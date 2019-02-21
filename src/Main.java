import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int answer = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your login: ");
        String login = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        Login user = new Login(login, password, 1);
        try {
            answer = user.send(Utils.getURL() + "/auth");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (answer == 200)
            try {
                Thread th = new Thread(new GetThread());
                th.setDaemon(true);
                th.start();
                System.out.println("Enter your message: ");

                while (true) {
                    String text = scanner.nextLine();

                    if (text.equals("exit") || text.isEmpty()) {
                        Login status = new Login(login, 0);
                        status.send(Utils.getURL() + "/auth");
                        break;
                    }

                    if (text.equals("online")) {
                        Login status = new Login(2);

                        status.send(Utils.getURL() + "/auth");
                        continue;
                    }

                    Message m = new Message(login, text);
                    int res = m.send(Utils.getURL() + "/add");

                    if (res != 200) { // 200 OK
                        System.out.println("HTTP error occurred: " + res);
                        return;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                scanner.close();
            }
    }
}
