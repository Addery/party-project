import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestPortraitResponse {
    public static void main(String[] args) {
        try {
            // 1. 先登录获取token
            System.out.println("=== 1. 登录获取token ===");
            String token = login();
            System.out.println("获取到的token: " + token);
            
            if (token == null || token.isEmpty()) {
                System.out.println("登录失败，无法继续测试");
                return;
            }
            
            // 2. 调用获取当前用户画像接口
            System.out.println("\n=== 2. 调用获取当前用户画像接口 ===");
            String portraitResponse = getCurrentUserPortrait(token);
            System.out.println("用户画像接口返回:");
            System.out.println(portraitResponse);
            
            // 3. 检查返回格式是否符合前端期望
            System.out.println("\n=== 3. 检查返回格式 ===");
            checkResponseFormat(portraitResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 登录方法
    private static String login() throws Exception {
        URL url = new URL("http://localhost:8080/api/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // 登录请求体
        String loginJson = "{\"username\": \"Addery\", \"password\": \"password123\"}";
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = loginJson.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        System.out.println("登录请求响应码: " + responseCode);
        
        String responseStr = "";
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                
                responseStr = response.toString();
                System.out.println("登录接口返回完整内容: " + responseStr);
                
                // 解析token
                int tokenStart = responseStr.indexOf("\"token\":\"fake-jwt-token-");
                if (tokenStart != -1) {
                    int tokenEnd = responseStr.indexOf("\"", tokenStart + 13);
                    if (tokenEnd != -1) {
                        return responseStr.substring(tokenStart + 9, tokenEnd);
                    }
                }
            }
        } else {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                responseStr = response.toString();
                System.out.println("登录失败响应: " + responseStr);
            }
        }
        
        return null;
    }
    
    // 获取当前用户画像
    private static String getCurrentUserPortrait(String token) throws Exception {
        URL url = new URL("http://localhost:8080/api/user/portrait/current");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        
        int responseCode = conn.getResponseCode();
        System.out.println("画像请求响应码: " + responseCode);
        
        if (responseCode == 200) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return response.toString();
            }
        } else {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return response.toString();
            }
        }
    }
    
    // 检查返回格式
    private static void checkResponseFormat(String response) {
        boolean formatCorrect = true;
        
        // 检查是否包含前端期望的字段
        String[] expectedFields = {
            "personalityLabels",
            "preferenceLabels",
            "preferences",
            "newsCategories",
            "learningTopics"
        };
        
        System.out.println("检查前端期望的字段是否存在:");
        for (String field : expectedFields) {
            if (response.contains("\"" + field + "\"")) {
                System.out.println("✓ 找到字段: " + field);
            } else {
                System.out.println("✗ 缺少字段: " + field);
                formatCorrect = false;
            }
        }
        
        // 检查旧的字段是否已被替换
        String[] deprecatedFields = {
            "learningTags",
            "learningInterests",
            "learningPreferences"
        };
        
        System.out.println("\n检查旧字段是否已被移除:");
        for (String field : deprecatedFields) {
            if (response.contains("\"" + field + "\"")) {
                System.out.println("✗ 发现旧字段: " + field);
                formatCorrect = false;
            } else {
                System.out.println("✓ 旧字段已移除: " + field);
            }
        }
        
        if (formatCorrect) {
            System.out.println("\n✓ 返回格式符合前端期望！");
        } else {
            System.out.println("\n✗ 返回格式不符合前端期望，请检查代码！");
        }
    }
}