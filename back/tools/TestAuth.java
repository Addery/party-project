import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;

public class TestAuth {
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        System.out.println("开始测试认证功能...");
        try {
            // 测试注册功能
            testRegister();
            
            // 测试登录功能
            testLogin();
        } catch (Exception e) {
            System.out.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testRegister() throws IOException, InterruptedException {
        System.out.println("测试注册功能...");
        String registerJson = "{\"username\": \"testuser\", \"password\": \"password123\", \"name\": \"Test User\", \"gender\": \"男\", \"partyStatus\": \"正式党员\", \"branch\": \"测试支部\", \"role\": \"member\"}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(registerJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("注册状态码: " + response.statusCode());
        System.out.println("注册响应: " + response.body());
        System.out.println();
    }

    private static void testLogin() throws IOException, InterruptedException {
        System.out.println("测试登录功能...");
        String loginJson = "{\"username\": \"testuser\", \"password\": \"password123\"}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("登录状态码: " + response.statusCode());
        System.out.println("登录响应: " + response.body());
        System.out.println();
    }
}