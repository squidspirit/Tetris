import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        for (String s : args) {
            if (s.equals("server")) {
                server.Main.main(args);
                return;
            }
            else {
                System.out.println("Unknown argument: " + s);
                return;
            }
        }
        application.main.Main.main(args);
    }
}
