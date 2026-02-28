import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TestDateFormat {
    public static void main(String[] args) {
        try {
            // 先注册一个新用户，包含joinDate字段
            registerUser();
            
            // 然后登录获取用户信息，查看日期格式
            loginAndGetUserInfo();
            
        } catch (Exception e) {
            System.out.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void registerUser() throws Exception {
        System.out.println("开始注册用户...");
        
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
        
        // 创建JSON数据，包含joinDate字段
        String jsonInputString = "{\"username\":\"dateuser\",\"password\":\"password123\",\"name\":\"日期测试用户\",\"gender\":\"男\",\"partyStatus\":\"正式党员\",\"branch\":\"测试支部\",\"role\":\"member\",\"joinDate\":\"2020-01-15\"}";
        
        // 写入JSON数据
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(jsonInputString);
            writer.flush();
        }
        
        // 获取响应状态码
        int responseCode = conn.getResponseCode();
        System.out.println("注册响应状态码: " + responseCode);
        
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
        
        System.out.println("注册响应内容: " + response.toString());
        
        conn.disconnect();
        
        System.out.println("用户注册完成！");
    }
    
    private static void loginAndGetUserInfo() throws Exception {
        System.out.println("\n开始登录获取用户信息...");
        
        // 创建URL对象
        URL url = new URL("http://localhost:8080/api/auth/login");
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
        String jsonInputString = "{\"username\":\"dateuser\",\"password\":\"password123\"}";
        
        // 写入JSON数据
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(jsonInputString);
            writer.flush();
        }
        
        // 获取响应状态码
        int responseCode = conn.getResponseCode();
        System.out.println("登录响应状态码: " + responseCode);
        
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
        
        System.out.println("登录响应内容: " + response.toString());
        
        // 检查日期格式
        String responseStr = response.toString();
        if (responseStr.contains("年") && responseStr.contains("月") && responseStr.contains("日")) {
            System.out.println("\n✅ 日期格式已成功修改为xx年xx月xx日！");
        } else {
            System.out.println("\n❌ 日期格式未修改成功！");
        }
        
        conn.disconnect();
    }
}
