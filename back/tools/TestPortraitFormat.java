import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 测试用户画像接口返回格式
 * 直接调用接口并手动设置Authorization头
 */
public class TestPortraitFormat {
    public static void main(String[] args) {
        try {
            // 测试用户ID（从数据库中获取）
            int userId = 2;
            
            // 测试接口URL
            String url = "http://localhost:8080/api/user-portraits/" + userId;
            
            // 创建URL对象
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // 设置请求方法
            con.setRequestMethod("GET");
            
            // 设置请求头
            con.setRequestProperty("Content-Type", "application/json");
            // 手动设置Authorization头（使用一个简单的token，实际环境中需要有效的JWT）
            con.setRequestProperty("Authorization", "Bearer test-token");
            
            // 获取响应码
            int responseCode = con.getResponseCode();
            System.out.println("=== 用户画像接口测试 ===");
            System.out.println("请求URL: " + url);
            System.out.println("响应码: " + responseCode);
            
            // 读取响应内容
            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // 打印响应内容
            System.out.println("\n接口返回内容:");
            System.out.println(response.toString());
            
            // 分析返回格式
            System.out.println("\n=== 返回格式分析 ===");
            String responseStr = response.toString();
            
            // 检查是否包含前端所需的字段
            if (responseStr.contains("personalityLabels")) {
                System.out.println("✓ 包含 personalityLabels 字段");
            } else {
                System.out.println("✗ 缺少 personalityLabels 字段");
            }
            
            if (responseStr.contains("preferenceLabels")) {
                System.out.println("✓ 包含 preferenceLabels 字段");
            } else {
                System.out.println("✗ 缺少 preferenceLabels 字段");
            }
            
            if (responseStr.contains("preferences")) {
                System.out.println("✓ 包含 preferences 字段");
            } else {
                System.out.println("✗ 缺少 preferences 字段");
            }
            
            // 检查是否包含旧的字段
            if (responseStr.contains("learningTags")) {
                System.out.println("✗ 仍然包含旧的 learningTags 字段");
            } else {
                System.out.println("✓ 已移除旧的 learningTags 字段");
            }
            
            if (responseStr.contains("learningInterests")) {
                System.out.println("✗ 仍然包含旧的 learningInterests 字段");
            } else {
                System.out.println("✓ 已移除旧的 learningInterests 字段");
            }
            
            if (responseStr.contains("learningPreferences")) {
                System.out.println("✗ 仍然包含旧的 learningPreferences 字段");
            } else {
                System.out.println("✓ 已移除旧的 learningPreferences 字段");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}