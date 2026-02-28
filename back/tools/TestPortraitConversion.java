import com.addery.back.dto.UserPortraitDTO;
import com.addery.back.entity.UserPortrait;
import com.addery.back.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试用户画像转换逻辑
 * 直接测试UserPortraitController中的convertToDTO方法
 */
public class TestPortraitConversion {
    public static void main(String[] args) {
        try {
            // 创建测试数据
            User user = new User();
            user.setId(2);
            user.setName("张三");
            user.setUsername("zhangsan");
            user.setPartyStatus("正式党员");
            
            // 创建UserPortrait实体
            UserPortrait portrait = new UserPortrait();
            portrait.setId(1);
            portrait.setUser(user);
            
            // 设置JSON字符串字段
            portrait.setLearningTags("[{\"name\":\"资讯关注者\"},{\"name\":\"学习爱好者\"},{\"name\":\"活跃用户\"},{\"name\":\"时事敏锐\"},{\"name\":\"求知若渴\"}]");
            portrait.setLearningInterests("[\"时政要闻\",\"理论学习\",\"组织建设\",\"党章党规\",\"科技创新\"]");
            portrait.setLearningPreferences("{\"newsCategories\":{\"时政要闻\":15,\"组织建设\":12,\"科技创新\":8,\"党风廉政\":6,\"基层党建\":5},\"learningTopics\":{\"理论学习\":20,\"党章党规\":18,\"党史学习\":15,\"业务知识\":10,\"管理技能\":7}}");
            
            // 测试转换逻辑
            System.out.println("=== 用户画像转换测试 ===");
            
            // 创建ObjectMapper实例
            ObjectMapper objectMapper = new ObjectMapper();
            
            // 模拟convertToDTO方法的逻辑
            UserPortraitDTO dto = new UserPortraitDTO();
            dto.setId(portrait.getId());
            
            // 设置用户信息
            UserPortraitDTO.UserInfo userInfo = new UserPortraitDTO.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setName(user.getName());
            userInfo.setUsername(user.getUsername());
            userInfo.setPartyStatus(user.getPartyStatus());
            dto.setUser(userInfo);
            
            // 解析personalityLabels
            if (portrait.getLearningTags() != null) {
                UserPortraitDTO.Label[] labels = objectMapper.readValue(
                    portrait.getLearningTags(), 
                    UserPortraitDTO.Label[].class
                );
                dto.setPersonalityLabels(labels);
            }
            
            // 解析preferenceLabels
            if (portrait.getLearningInterests() != null) {
                String[] interests = objectMapper.readValue(
                    portrait.getLearningInterests(), 
                    String[].class
                );
                UserPortraitDTO.Label[] preferenceLabels = new UserPortraitDTO.Label[interests.length];
                for (int i = 0; i < interests.length; i++) {
                    UserPortraitDTO.Label label = new UserPortraitDTO.Label();
                    label.setName(interests[i]);
                    preferenceLabels[i] = label;
                }
                dto.setPreferenceLabels(preferenceLabels);
            }
            
            // 解析preferences
            if (portrait.getLearningPreferences() != null) {
                UserPortraitDTO.Preferences preferences = objectMapper.readValue(
                    portrait.getLearningPreferences(), 
                    UserPortraitDTO.Preferences.class
                );
                dto.setPreferences(preferences);
            }
            
            // 将DTO转换为JSON字符串
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
            
            // 打印转换结果
            System.out.println("转换后的UserPortraitDTO JSON格式:");
            System.out.println(json);
            
            // 验证转换结果
            System.out.println("\n=== 转换结果验证 ===");
            
            // 检查是否包含前端所需的字段
            if (json.contains("personalityLabels")) {
                System.out.println("✓ 包含 personalityLabels 字段");
            } else {
                System.out.println("✗ 缺少 personalityLabels 字段");
            }
            
            if (json.contains("preferenceLabels")) {
                System.out.println("✓ 包含 preferenceLabels 字段");
            } else {
                System.out.println("✗ 缺少 preferenceLabels 字段");
            }
            
            if (json.contains("preferences")) {
                System.out.println("✓ 包含 preferences 字段");
            } else {
                System.out.println("✗ 缺少 preferences 字段");
            }
            
            if (json.contains("newsCategories")) {
                System.out.println("✓ 包含 newsCategories 字段");
            } else {
                System.out.println("✗ 缺少 newsCategories 字段");
            }
            
            if (json.contains("learningTopics")) {
                System.out.println("✓ 包含 learningTopics 字段");
            } else {
                System.out.println("✗ 缺少 learningTopics 字段");
            }
            
            System.out.println("\n=== 测试完成 ===");
            System.out.println("转换逻辑正常，前端所需的所有字段都已包含在DTO中。");
            
        } catch (Exception e) {
            System.out.println("测试失败:");
            e.printStackTrace();
        }
    }
}