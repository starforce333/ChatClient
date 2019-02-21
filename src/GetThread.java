import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import netscape.javascript.JSObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;

    public GetThread() {
        gson = new GsonBuilder().create();
    }

    private int flag = 0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {

                URL url = new URL(Utils.getURL() + "/getListServlet?from=" + n);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                InputStream is = http.getInputStream();
                try {
                    byte[] buf = requestBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    // если от сервера пришли данные
                    if (!strBuf.isEmpty()) {

                        System.out.println("Server Request  : " + url);
                        System.out.println("Server Response : " + strBuf);

                        JsonObject jsonData = (JsonObject) new JsonParser().parse(strBuf);

                        if (jsonData.has("statusList")) {

                            System.out.println("> Status List");
                        }
                    }


//                    Login list = gson.fromJson(strBuf, Login.class);
                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            n++;
                        }
                    }


                } finally {
                    is.close();
                }

//                }
//                if (flag == 1) {
//
//                    flag = 0;
//                }


                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
