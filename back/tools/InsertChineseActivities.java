import java.sql.*;

public class InsertChineseActivities {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // 关闭自动提交
            conn.setAutoCommit(false);
            System.out.println("数据库连接成功");

            // 清空现有数据
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM activity_registrations");
                stmt.execute("DELETE FROM activity_attachments");
                stmt.execute("DELETE FROM activities");
                stmt.execute("ALTER TABLE activities AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE activity_attachments AUTO_INCREMENT = 1");
                stmt.execute("ALTER TABLE activity_registrations AUTO_INCREMENT = 1");
            }

            // 插入6条活动数据（近一年以内的日期）
            String insertActivitySql = "INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertActivitySql)) {
                // 活动1
                pstmt.setString(1, "党史学习教育主题党日活动");
                pstmt.setDate(2, Date.valueOf("2024-12-15"));
                pstmt.setString(3, "09:00-11:30");
                pstmt.setString(4, "大礼堂");
                pstmt.setString(5, "theme");
                pstmt.setString(6, "published");
                pstmt.setString(7, "深入学习党史，增强党性修养");
                pstmt.setString(8, "党史学习教育主题党日活动内容");
                pstmt.setBoolean(9, true);
                pstmt.setDate(10, Date.valueOf("2024-12-10"));
                pstmt.setInt(11, 3);
                pstmt.setInt(12, 50);
                pstmt.setInt(13, 120);
                pstmt.setString(14, "/images/activity1.jpg");
                pstmt.executeUpdate();

                // 活动2
                pstmt.setString(1, "习近平新时代中国特色社会主义思想学习会");
                pstmt.setDate(2, Date.valueOf("2024-11-20"));
                pstmt.setString(3, "14:00-16:30");
                pstmt.setString(4, "会议室A");
                pstmt.setString(5, "learning");
                pstmt.setString(7, "学习贯彻习近平新时代中国特色社会主义思想");
                pstmt.setString(8, "思想学习会内容");
                pstmt.setDate(10, Date.valueOf("2024-11-15"));
                pstmt.setInt(11, 2);
                pstmt.setInt(12, 40);
                pstmt.setInt(13, 95);
                pstmt.setString(14, "/images/activity2.jpg");
                pstmt.executeUpdate();

                // 活动3
                pstmt.setString(1, "社区志愿服务活动");
                pstmt.setDate(2, Date.valueOf("2024-10-25"));
                pstmt.setString(3, "08:30-12:00");
                pstmt.setString(4, "阳光社区");
                pstmt.setString(5, "volunteer");
                pstmt.setString(7, "走进社区，服务群众");
                pstmt.setString(8, "志愿服务内容");
                pstmt.setDate(10, Date.valueOf("2024-10-20"));
                pstmt.setInt(11, 3);
                pstmt.setInt(12, 60);
                pstmt.setInt(13, 150);
                pstmt.setString(14, "/images/activity3.jpg");
                pstmt.executeUpdate();

                // 活动4
                pstmt.setString(1, "支部委员会会议");
                pstmt.setDate(2, Date.valueOf("2024-09-08"));
                pstmt.setString(3, "15:00-17:00");
                pstmt.setString(4, "支部会议室");
                pstmt.setString(5, "meeting");
                pstmt.setString(7, "讨论近期党建工作安排");
                pstmt.setString(8, "支部会议内容");
                pstmt.setDate(10, Date.valueOf("2024-09-05"));
                pstmt.setInt(11, 3);
                pstmt.setInt(12, 10);
                pstmt.setInt(13, 45);
                pstmt.setString(14, "/images/activity4.jpg");
                pstmt.executeUpdate();

                // 活动5
                pstmt.setString(1, "新年茶话会");
                pstmt.setDate(2, Date.valueOf("2024-12-31"));
                pstmt.setString(3, "16:00-18:00");
                pstmt.setString(4, "多功能厅");
                pstmt.setString(5, "social");
                pstmt.setString(7, "辞旧迎新，共话发展");
                pstmt.setString(8, "新年茶话会内容");
                pstmt.setDate(10, Date.valueOf("2024-12-25"));
                pstmt.setInt(11, 4);
                pstmt.setInt(12, 80);
                pstmt.setInt(13, 200);
                pstmt.setString(14, "/images/activity5.jpg");
                pstmt.executeUpdate();

                // 活动6
                pstmt.setString(1, "党风廉政教育专题讲座");
                pstmt.setDate(2, Date.valueOf("2024-11-18"));
                pstmt.setString(3, "10:00-12:00");
                pstmt.setString(4, "报告厅");
                pstmt.setString(5, "other");
                pstmt.setString(7, "加强党风廉政建设，筑牢思想防线");
                pstmt.setString(8, "廉政教育讲座内容");
                pstmt.setDate(10, Date.valueOf("2024-11-15"));
                pstmt.setInt(11, 4);
                pstmt.setInt(12, 100);
                pstmt.setInt(13, 280);
                pstmt.setString(14, "/images/activity6.jpg");
                pstmt.executeUpdate();
            }

            // 提交事务
            conn.commit();
            System.out.println("中文活动数据插入成功");

            // 测试查询
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT id, title, date FROM activities LIMIT 3");
                System.out.println("查询结果：");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", 标题: " + rs.getString("title") + ", 日期: " + rs.getDate("date"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}