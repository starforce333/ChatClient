import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Login {

    private String login;
    private String password;
    private int cmd;

    public Login(String login, String password, int cmd) {
        this.login = login;
        this.password = password;
        this.cmd = cmd;
    }

    public Login(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Login(String login, int cmd) {
        this.login = login;
        this.cmd = cmd;
    }

    public Login(int cmd) {
        this.cmd = cmd;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }


    public int send(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        try {
            String json = toJSON();
//            System.out.println(json);
            os.write(json.getBytes(StandardCharsets.UTF_8));
            return conn.getResponseCode();
        } finally {
            os.close();
        }
    }
}
