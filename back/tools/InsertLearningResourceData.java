import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertLearningResourceData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // 插入学习资源数据
            insertLearningResource(conn, "2025年中央经济工作会议精神解读", "中央党校教授", "2025-12-15", "2.5小时", "video", "一级", "video", "解读2025年中央经济工作会议精神的权威视频课程");
            insertLearningResource(conn, "新发展阶段党员教育手册", "党建出版社", "2025-12-10", "3.0小时", "document", "二级", "document", "新发展阶段党员教育的重要参考资料");
            insertLearningResource(conn, "科技创新与高质量发展", "科技研究院", "2025-11-28", "1.8小时", "video", "一级", "video", "科技创新推动高质量发展的专题讲座");
            insertLearningResource(conn, "数字经济时代的党建工作创新", "数字党建中心", "2025-11-20", "2.2小时", "course", "一级", "course", "数字经济时代党建工作创新的实践指南");
            insertLearningResource(conn, "党章修正案学习辅导", "党史出版社", "2025-11-15", "1.5小时", "document", "二级", "document", "党章修正案的详细解读和学习辅导");
            insertLearningResource(conn, "基层党组织建设案例精选", "组织部门", "2025-11-08", "2.0小时", "case", "二级", "case", "基层党组织建设的优秀案例汇编");
            insertLearningResource(conn, "党风廉政建设专题教育", "纪委监委", "2025-11-01", "1.2小时", "video", "一级", "video", "党风廉政建设的专题教育视频");
            insertLearningResource(conn, "党员先锋模范作用发挥", "党建研究中心", "2025-10-25", "1.0小时", "article", "二级", "article", "党员先锋模范作用发挥的理论与实践");

            System.out.println("学习资源数据插入完成！");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertLearningResource(Connection conn, String title, String author, String publishDate, String duration, String type, String level, String category, String description) throws SQLException {
        String sql = "INSERT INTO learning_resource (title, author, publish_date, duration, type, level, category, description, image, views, rating, content) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'default.jpg', 0, 0, '{}')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publishDate);
            pstmt.setString(4, duration);
            pstmt.setString(5, type);
            pstmt.setString(6, level);
            pstmt.setString(7, category);
            pstmt.setString(8, description);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("插入学习资源：" + title + " 成功！");
            } else {
                System.out.println("插入学习资源：" + title + " 失败！");
            }
        }
    }
}