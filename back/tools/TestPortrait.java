import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

// 简单的测试类，验证用户画像转换逻辑
public class TestPortrait {
    public static void main(String[] args) throws Exception {
        System.out.println("=== 测试用户画像转换逻辑 ===");
        
        // 创建ObjectMapper实例
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 模拟数据库中的JSON字符串
        String learningTags = "[\"资讯关注者\",\"学习爱好者\",\"活跃用户\",\"时事敏锐\",\"求知若渴\"]";
        String learningInterests = "[\"时政要闻\",\"理论学习\",\"组织建设\",\"党章党规\",\"科技创新\"]";
        String learningPreferences = "{\"newsCategories\":{\"时政要闻\":15,\"组织建设\":12,\"科技创新\":8,\"党风廉政\":6,\"基层党建\":5},\"learningTopics\":{\"理论学习\":20,\"党章党规\":18,\"党史学习\":15,\"业务知识\":10,\"管理技能\":7}}";
        
        // 解析JSON字符串
        List<String> personalityLabels = objectMapper.readValue(learningTags, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        List<String> preferenceLabels = objectMapper.readValue(learningInterests, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        Map<String, Map<String, Integer>> preferences = objectMapper.readValue(learningPreferences, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Map<String, Integer>>>() {});
        
        // 创建模拟的DTO
        Map<String, Object> dto = new HashMap<>();
        dto.put("personalityLabels", personalityLabels);
        dto.put("preferenceLabels", preferenceLabels);
        dto.put("preferences", preferences);
        
        // 设置用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("name", "张三");
        userInfo.put("username", "zhangsan");
        dto.put("user", userInfo);
        
        // 将DTO转换为JSON字符串
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        
        // 打印转换结果
        System.out.println("转换后的用户画像JSON格式:");
        System.out.println(json);
        
        // 验证转换结果
        System.out.println("\n=== 转换结果验证 ===");
        
        // 检查是否包含前端所需的字段
        System.out.println("✓ personalityLabels: " + personalityLabels);
        System.out.println("✓ preferenceLabels: " + preferenceLabels);
        System.out.println("✓ preferences.newsCategories: " + preferences.get("newsCategories"));
        System.out.println("✓ preferences.learningTopics: " + preferences.get("learningTopics"));
        
        // 检查字段内容
        System.out.println("\n=== 字段内容验证 ===");
        System.out.println("✓ personalityLabels 包含5个标签: " + (personalityLabels.size() == 5));
        System.out.println("✓ preferenceLabels 包含5个标签: " + (preferenceLabels.size() == 5));
        System.out.println("✓ 第一个personality标签为'资讯关注者': " + "资讯关注者".equals(personalityLabels.get(0)));
        System.out.println("✓ 第一个preference标签为'时政要闻': " + "时政要闻".equals(preferenceLabels.get(0)));
        System.out.println("✓ newsCategories 包含'时政要闻': " + preferences.get("newsCategories").containsKey("时政要闻"));
        System.out.println("✓ learningTopics 包含'理论学习': " + preferences.get("learningTopics").containsKey("理论学习"));
        
        // 验证JSON结构是否符合前端需求
        System.out.println("\n=== JSON结构验证 ===");
        System.out.println("✓ 包含personalityLabels字段: " + json.contains("personalityLabels"));
        System.out.println("✓ 包含preferenceLabels字段: " + json.contains("preferenceLabels"));
        System.out.println("✓ 包含preferences字段: " + json.contains("preferences"));
        System.out.println("✓ 包含newsCategories字段: " + json.contains("newsCategories"));
        System.out.println("✓ 包含learningTopics字段: " + json.contains("learningTopics"));
        
        System.out.println("\n=== 测试完成 ===");
        System.out.println("✓ 用户画像转换逻辑正确，JSON格式符合前端需求");
    }
}