import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertChineseLearningResources {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";

        // 删除英文内容的SQL语句
        String deleteSql = "DELETE FROM learning_resources WHERE id IN (7, 8, 9, 10)";
        
        // 插入中文内容的SQL语句
        String insertSql = "INSERT INTO learning_resources (title, type, category, author, upload_date, views, status, url, description, content, upload_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // 删除英文内容
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                int deleteCount = deleteStmt.executeUpdate();
                System.out.println("删除了 " + deleteCount + " 条英文学习资源记录");
            }

            // 插入中文内容
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                // 第一条数据
                insertStmt.setString(1, "2025年中央经济工作会议精神解读");
                insertStmt.setString(2, "video");
                insertStmt.setString(3, "policy");
                insertStmt.setString(4, "中央党校");
                insertStmt.setString(5, "2025-12-15");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "深入解读2025年中央经济工作会议精神，分析当前经济形势和政策方向。");
                insertStmt.setString(10, "2025年中央经济工作会议于12月12日至14日在北京举行，会议总结了2025年经济工作，分析了当前经济形势，部署了2026年经济工作任务。会议强调要坚持稳中求进工作总基调，完整、准确、全面贯彻新发展理念，加快构建新发展格局，着力推动高质量发展。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第二条数据
                insertStmt.setString(1, "新发展阶段党员教育手册");
                insertStmt.setString(2, "document");
                insertStmt.setString(3, "theory");
                insertStmt.setString(4, "党建出版社");
                insertStmt.setString(5, "2025-12-10");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "为适应新发展阶段要求而编写的党员教育重要参考资料。");
                insertStmt.setString(10, "本手册旨在帮助广大党员深入学习贯彻习近平新时代中国特色社会主义思想，提高政治素质和理论水平，增强“四个意识”、坚定“四个自信”、做到“两个维护”。手册内容涵盖党的基本理论、路线方针政策、党章党规党纪等方面。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第三条数据
                insertStmt.setString(1, "科技创新与高质量发展专题讲座");
                insertStmt.setString(2, "video");
                insertStmt.setString(3, "model");
                insertStmt.setString(4, "中国科学技术协会");
                insertStmt.setString(5, "2025-11-28");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "探讨科技创新在推动高质量发展中的重要作用和实践路径。");
                insertStmt.setString(10, "本讲座邀请了多位知名科学家和企业家，围绕科技创新与高质量发展的主题，分享了最新的研究成果和实践经验。讲座内容包括人工智能、生物医药、新能源等前沿领域的发展趋势和应用前景。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第四条数据
                insertStmt.setString(1, "数字经济时代的党建工作创新");
                insertStmt.setString(2, "article");
                insertStmt.setString(3, "theory");
                insertStmt.setString(4, "中共中央党校党建部");
                insertStmt.setString(5, "2025-11-20");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "分析数字经济时代党建工作面临的新挑战和新机遇，提出创新思路和方法。");
                insertStmt.setString(10, "随着数字经济的快速发展，党建工作面临着新的挑战和机遇。本文从数字技术与党建工作的融合角度，探讨了如何利用大数据、人工智能等技术创新党建工作方式方法，提高党建工作的针对性和实效性。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                System.out.println("成功插入了4条中文学习资源记录");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}