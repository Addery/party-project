import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateUserPortraitJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // 更新用户ID为2的画像（zhangsan）
            updateUserPortrait(conn, 2, 
                "[\"资讯关注者\", \"学习爱好者\", \"活跃用户\", \"时事敏锐\", \"求知若渴\", \"理论研究者\", \"政策解读达人\"]",
                "[\"时政要闻\", \"组织建设\", \"科技创新\", \"党风廉政\", \"基层党建\", \"政策解读\", \"思想理论\"]",
                "{\"newsReading\":{\"时政要闻\":25, \"组织建设\":20, \"科技创新\":18, \"党风廉政\":15, \"基层党建\":22}, \"learningTopics\":{\"理论学习\":30, \"党章党规\":25, \"政策解读\":28, \"思想建设\":22}}"
            );

            // 更新用户ID为3的画像（lisi）
            updateUserPortrait(conn, 3, 
                "[\"科技爱好者\", \"创新思维\", \"数据驱动\", \"学习型人才\", \"技术专家\", \"政策关注者\"]",
                "[\"科技创新\", \"数字经济\", \"人工智能\", \"大数据应用\", \"信息技术\", \"政策法规\"]",
                "{\"newsReading\":{\"科技创新\":30, \"数字经济\":25, \"人工智能\":28, \"大数据应用\":22, \"信息技术\":20}, \"learningTopics\":{\"技术前沿\":35, \"政策解读\":15, \"数字治理\":25, \"创新案例\":20}}"
            );

            // 更新用户ID为5的画像（newuser）
            updateUserPortrait(conn, 5, 
                "[\"新党员\", \"学习新人\", \"积极分子\", \"思想进步\", \"求知若渴\", \"组织参与者\"]",
                "[\"入党知识\", \"思想理论\", \"党章党规\", \"党史学习\", \"组织生活\", \"先进事迹\"]",
                "{\"newsReading\":{\"党史学习\":25, \"党章党规\":28, \"思想理论\":22, \"入党知识\":30, \"先进事迹\":20}, \"learningTopics\":{\"基础理论\":35, \"党性修养\":28, \"组织纪律\":20, \"党员义务\":18}}"
            );

            // 更新用户ID为6的画像（Addery）
            updateUserPortrait(conn, 6, 
                "[\"学习达人\", \"组织管理者\", \"政策专家\", \"思想引领者\", \"理论功底深厚\", \"学习型领导\"]",
                "[\"组织建设\", \"政策研究\", \"思想理论\", \"领导科学\", \"基层党建\", \"人才培养\"]",
                "{\"newsReading\":{\"组织建设\":30, \"政策研究\":25, \"思想理论\":22, \"领导科学\":20, \"基层党建\":18}, \"learningTopics\":{\"党建工作\":35, \"领导艺术\":28, \"政策解读\":20, \"人才发展\":18}}"
            );

            // 更新用户ID为7的画像（test）
            updateUserPortrait(conn, 7, 
                "[\"普通党员\", \"岗位能手\", \"学习进取\", \"工作认真\", \"组织纪律性强\", \"业务骨干\"]",
                "[\"业务知识\", \"岗位技能\", \"政策法规\", \"工作经验\", \"职业发展\", \"行业动态\"]",
                "{\"newsReading\":{\"业务知识\":28, \"岗位技能\":25, \"政策法规\":20, \"行业动态\":18, \"职业发展\":15}, \"learningTopics\":{\"专业技能\":35, \"工作方法\":25, \"政策应用\":20, \"职业规划\":15}}"
            );

            // 更新用户ID为8的画像（testtest）
            updateUserPortrait(conn, 8, 
                "[\"青年党员\", \"活力四射\", \"创新思维\", \"学习能力强\", \"社交达人\", \"网络活跃\"]",
                "[\"青年工作\", \"网络党建\", \"创新实践\", \"志愿服务\", \"文化活动\", \"社交网络\"]",
                "{\"newsReading\":{\"青年工作\":25, \"网络党建\":30, \"创新实践\":22, \"志愿服务\":18, \"文化活动\":15}, \"learningTopics\":{\"青年发展\":35, \"网络技术\":25, \"创新方法\":20, \"志愿服务\":15}}"
            );

            // 更新用户ID为9的画像
            updateUserPortrait(conn, 9, 
                "[\"资深党员\", \"经验丰富\", \"理论扎实\", \"思想成熟\", \"组织协调\", \"传帮带能手\"]",
                "[\"党史研究\", \"传统教育\", \"经验分享\", \"思想工作\", \"组织建设\", \"人才培养\"]",
                "{\"newsReading\":{\"党史研究\":30, \"传统教育\":25, \"经验分享\":22, \"组织建设\":18, \"人才培养\":15}, \"learningTopics\":{\"党史学习\":35, \"传统教育\":25, \"经验传承\":20, \"思想工作\":15}}"
            );

            // 更新用户ID为10的画像（admin）
            updateUserPortrait(conn, 10, 
                "[\"系统管理员\", \"平台维护者\", \"技术支持\", \"安全专家\", \"数据管理者\", \"服务保障\"]",
                "[\"系统安全\", \"技术维护\", \"数据管理\", \"平台优化\", \"用户体验\", \"安全策略\"]",
                "{\"newsReading\":{\"系统安全\":30, \"技术维护\":28, \"数据管理\":22, \"平台优化\":20, \"安全策略\":18}, \"learningTopics\":{\"技术维护\":35, \"系统安全\":30, \"数据管理\":20, \"平台优化\":15}}"
            );

            System.out.println("用户画像数据更新完成！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateUserPortrait(Connection conn, int userId, String learningTags, String learningInterests, String learningPreferences) throws SQLException {
        String sql = "UPDATE user_portrait SET learning_tags = ?, learning_interests = ?, learning_preferences = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, learningTags);
            pstmt.setString(2, learningInterests);
            pstmt.setString(3, learningPreferences);
            pstmt.setInt(4, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("更新用户ID " + userId + " 的画像数据成功！");
            } else {
                System.out.println("未找到用户ID " + userId + " 的画像数据！");
            }
        }
    }
}
