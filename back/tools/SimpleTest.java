import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SimpleTest {
    public static void main(String[] args) {
        try {
            // 创建URL对象
            URL url = new URL("http://localhost:8080/api/auth/register");
            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方法
            conn.setRequestMethod("POST");
            // 设置请求头
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            // 允许输出
            conn.setDoOutput(true);
            
            // 创建JSON数据
            String jsonInputString = "{\"username\":\"testuser\",\"password\":\"password123\",\"name\":\"Test User\",\"gender\":\"男\",\"partyStatus\":\"正式党员\",\"branch\":\"测试支部\",\"role\":\"member\"}";
            
            // 写入JSON数据
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(jsonInputString);
                writer.flush();
            }
            
            // 获取响应状态码
            int responseCode = conn.getResponseCode();
            System.out.println("响应状态码: " + responseCode);
            
            // 读取响应内容
            StringBuilder response = new StringBuilder();
            Scanner scanner;
            if (responseCode >= 200 && responseCode < 300) {
                scanner = new Scanner(conn.getInputStream());
            } else {
                scanner = new Scanner(conn.getErrorStream());
            }
            
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            System.out.println("响应内容: " + response.toString());
            
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}