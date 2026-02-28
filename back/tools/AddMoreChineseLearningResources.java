import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMoreChineseLearningResources {
    public static void main(String[] args) {
        // 数据库连接信息
        String url = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";

        // 插入中文内容的SQL语句
        String insertSql = "INSERT INTO learning_resources (title, type, category, author, upload_date, views, status, url, description, content, upload_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                // 第五条数据 - 乡村振兴战略专题培训
                insertStmt.setString(1, "乡村振兴战略实施与农业现代化发展");
                insertStmt.setString(2, "video");
                insertStmt.setString(3, "policy");
                insertStmt.setString(4, "农业农村部政策研究室");
                insertStmt.setString(5, "2025-11-15");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "深入解读乡村振兴战略的实施路径和农业现代化发展方向。");
                insertStmt.setString(10, "本课程围绕乡村振兴战略的总体要求，详细讲解了产业振兴、人才振兴、文化振兴、生态振兴、组织振兴的具体内涵和实施路径。同时，结合农业现代化发展的最新趋势，探讨了数字农业、智慧农业等新技术在乡村振兴中的应用。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第六条数据 - 党史学习教育系列丛书
                insertStmt.setString(1, "中国共产党百年奋斗历程与历史经验");
                insertStmt.setString(2, "document");
                insertStmt.setString(3, "history");
                insertStmt.setString(4, "中共党史出版社");
                insertStmt.setString(5, "2025-11-10");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "系统梳理中国共产党百年奋斗的光辉历程和宝贵历史经验。");
                insertStmt.setString(10, "本书以习近平总书记关于党的历史的重要论述为指导，系统梳理了中国共产党从成立到新时代的百年奋斗历程，总结了党在各个历史时期的重大理论创新和实践成果，深刻阐释了中国共产党为什么能、马克思主义为什么行、中国特色社会主义为什么好的历史逻辑和理论逻辑。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第七条数据 - 意识形态工作专题讲座
                insertStmt.setString(1, "新时代意识形态工作的挑战与应对");
                insertStmt.setString(2, "video");
                insertStmt.setString(3, "theory");
                insertStmt.setString(4, "中共中央党校马克思主义学院");
                insertStmt.setString(5, "2025-11-05");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "分析新时代意识形态工作面临的挑战，提出加强意识形态工作的有效对策。");
                insertStmt.setString(10, "本讲座深入分析了当前意识形态领域面临的复杂形势和严峻挑战，重点讲解了如何坚持马克思主义在意识形态领域的指导地位，如何加强网络意识形态阵地建设，如何提高党员干部的意识形态辨别力、引领力和管控力。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第八条数据 - 生态文明建设与绿色发展
                insertStmt.setString(1, "生态文明思想与美丽中国建设实践");
                insertStmt.setString(2, "article");
                insertStmt.setString(3, "model");
                insertStmt.setString(4, "生态环境部政策法规司");
                insertStmt.setString(5, "2025-10-30");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "深入学习习近平生态文明思想，探索美丽中国建设的实践路径。");
                insertStmt.setString(10, "本文系统阐述了习近平生态文明思想的核心要义、精神实质和实践要求，结合美丽中国建设的实际案例，探讨了如何推动绿色发展、循环发展、低碳发展，如何加强生态环境保护和治理，如何构建人与自然和谐共生的现代化。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第九条数据 - 科技创新政策解读
                insertStmt.setString(1, "国家科技创新体系建设与科技强国战略");
                insertStmt.setString(2, "document");
                insertStmt.setString(3, "policy");
                insertStmt.setString(4, "科技部政策法规与创新体系建设司");
                insertStmt.setString(5, "2025-10-25");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "解读国家科技创新体系建设的政策措施和科技强国战略的实施路径。");
                insertStmt.setString(10, "本文件详细解读了国家科技创新体系建设的总体框架和主要任务，包括强化国家战略科技力量、提升企业技术创新能力、激发人才创新活力、完善科技评价体系等方面的政策措施，为深入实施科技强国战略提供了重要指导。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                // 第十条数据 - 文化自信与中华民族伟大复兴
                insertStmt.setString(1, "文化自信与社会主义文化强国建设");
                insertStmt.setString(2, "video");
                insertStmt.setString(3, "theory");
                insertStmt.setString(4, "中央社会主义学院");
                insertStmt.setString(5, "2025-10-20");
                insertStmt.setInt(6, 0);
                insertStmt.setString(7, "published");
                insertStmt.setNull(8, java.sql.Types.VARCHAR);
                insertStmt.setString(9, "探讨文化自信的内涵和社会主义文化强国建设的实践路径。");
                insertStmt.setString(10, "本讲座围绕文化自信与社会主义文化强国建设的主题，深入阐述了中华优秀传统文化、革命文化和社会主义先进文化的内在联系，讲解了如何弘扬中华优秀传统文化、继承革命文化、发展社会主义先进文化，推动社会主义文化强国建设。");
                insertStmt.setNull(11, java.sql.Types.INTEGER);
                insertStmt.executeUpdate();

                System.out.println("成功添加了6条更多的中文学习资源记录");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}