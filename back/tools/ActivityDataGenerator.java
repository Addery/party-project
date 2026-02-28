import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ActivityDataGenerator {
    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/party_management_system?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    
    // 随机数生成器
    private static final Random random = new Random();
    
    // 活动分类
    private static final String[] CATEGORIES = {"theme", "study", "volunteer", "democratic", "social", "training"};
    private static final String[] CATEGORY_NAMES = {"主题党日", "学习教育", "志愿服务", "民主生活", "社会实践", "培训交流"};
    
    // 活动地点
    private static final String[] LOCATIONS = {
        "党建活动室", "会议室A", "会议室B", "多功能厅", "户外广场", "社区服务中心", "图书馆", "体育馆"
    };
    
    // 活动状态
    private static final String[] STATUSES = {"published", "ended"};
    
    // 活动标题前缀
    private static final String[] TITLE_PREFIXES = {
        "学习贯彻", "深入开展", "积极参与", "组织召开", "热烈庆祝", "认真落实"
    };
    
    // 活动标题后缀
    private static final String[] TITLE_SUFFIXES = {
        "主题党日活动", "学习教育活动", "志愿服务活动", "民主生活会", "社会实践活动", "培训交流活动"
    };
    
    // 活动描述模板
    private static final String DESCRIPTION_TEMPLATE = "本次活动将聚焦%s，通过%s等形式，引导党员干部%s，增强%s意识，提升%s能力。";
    
    // 活动内容模板
    private static final String CONTENT_TEMPLATE = "<h2>活动详情</h2><p>活动时间：%s %s</p><p>活动地点：%s</p><p>活动内容：%s</p><p>报名截止时间：%s</p>";
    
    // 活动附件名称
    private static final String[] ATTACHMENT_NAMES = {
        "活动议程.pdf", "学习资料.docx", "报名须知.pdf", "活动方案.pptx"
    };
    
    // 姓名模板
    private static final String[] FIRST_NAMES = {"张", "王", "李", "刘", "陈", "杨", "赵", "黄", "周", "吴"};
    private static final String[] LAST_NAMES = {"明", "华", "芳", "强", "静", "丽", "涛", "磊", "军", "洋"};
    
    // 日期格式化器
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 建立连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            // 设置事务不自动提交
            connection.setAutoCommit(false);
            
            // 删除现有数据
            deleteExistingData(connection);
            
            // 插入模拟数据
            insertMockData(connection);
            
            // 提交事务
            connection.commit();
            
            System.out.println("活动数据生成成功！");
            
        } catch (ClassNotFoundException e) {
            System.out.println("找不到数据库驱动：" + e.getMessage());
            rollbackTransaction(connection);
        } catch (SQLException e) {
            System.out.println("数据库操作错误：" + e.getMessage());
            e.printStackTrace();
            rollbackTransaction(connection);
        } finally {
            closeConnection(connection);
        }
    }
    
    /**
     * 删除现有活动相关数据
     */
    private static void deleteExistingData(Connection connection) throws SQLException {
        System.out.println("正在删除现有活动数据...");
        
        // 先删除活动报名表数据（外键约束）
        String deleteRegistrationsSQL = "DELETE FROM activity_registrations";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteRegistrationsSQL);
        }
        
        // 再删除活动附件表数据（外键约束）
        String deleteAttachmentsSQL = "DELETE FROM activity_attachments";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteAttachmentsSQL);
        }
        
        // 最后删除活动表数据
        String deleteActivitiesSQL = "DELETE FROM activities";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteActivitiesSQL);
        }
        
        System.out.println("现有活动数据删除完成！");
    }
    
    /**
     * 插入模拟活动数据
     */
    private static void insertMockData(Connection connection) throws SQLException {
        System.out.println("正在生成模拟活动数据...");
        
        // 生成10条活动数据
        for (int i = 0; i < 10; i++) {
            // 生成活动信息
            String title = generateTitle();
            LocalDate date = generateDate();
            String time = generateTime();
            String location = LOCATIONS[random.nextInt(LOCATIONS.length)];
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            String status = STATUSES[random.nextInt(STATUSES.length)];
            String description = generateDescription(category);
            String content = String.format(CONTENT_TEMPLATE, date.format(DATE_FORMATTER), time, location, description, generateRegisterDeadline(date).format(DATE_FORMATTER));
            boolean canRegister = status.equals("published") && LocalDate.now().isBefore(date);
            LocalDate registerDeadline = generateRegisterDeadline(date);
            int participants = random.nextInt(50);
            int capacity = participants + random.nextInt(50) + 10;
            int views = random.nextInt(500) + 100;
            String image = generateImageUrl();
            
            // 插入活动数据
            String insertActivitySQL = "INSERT INTO activities (title, date, time, location, category, status, description, content, can_register, register_deadline, participants, capacity, views, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(insertActivitySQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, title);
                pstmt.setDate(2, Date.valueOf(date));
                pstmt.setString(3, time);
                pstmt.setString(4, location);
                pstmt.setString(5, category);
                pstmt.setString(6, status);
                pstmt.setString(7, description);
                pstmt.setString(8, content);
                pstmt.setBoolean(9, canRegister);
                pstmt.setDate(10, Date.valueOf(registerDeadline));
                pstmt.setInt(11, participants);
                pstmt.setInt(12, capacity);
                pstmt.setInt(13, views);
                pstmt.setString(14, image);
                
                pstmt.executeUpdate();
                
                // 获取生成的活动ID
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int activityId = generatedKeys.getInt(1);
                    
                    // 为活动生成附件（1-3个）
                    int attachmentCount = random.nextInt(3) + 1;
                    for (int j = 0; j < attachmentCount; j++) {
                        insertAttachment(connection, activityId, j);
                    }
                    
                    // 为活动生成报名记录（如果活动已发布）
                    if (status.equals("published")) {
                        insertRegistrations(connection, activityId, participants);
                    }
                }
            }
        }
        
        System.out.println("模拟活动数据生成完成！");
    }
    
    /**
     * 生成活动标题
     */
    private static String generateTitle() {
        String prefix = TITLE_PREFIXES[random.nextInt(TITLE_PREFIXES.length)];
        String suffix = TITLE_SUFFIXES[random.nextInt(TITLE_SUFFIXES.length)];
        return prefix + suffix;
    }
    
    /**
     * 生成活动日期（最近3个月内）
     */
    private static LocalDate generateDate() {
        LocalDate now = LocalDate.now();
        int days = random.nextInt(90); // 0-89天内
        return now.plusDays(days);
    }
    
    /**
     * 生成活动时间
     */
    private static String generateTime() {
        int startHour = random.nextInt(10) + 8; // 8-17点
        int endHour = startHour + random.nextInt(4) + 1; // 1-4小时后结束
        return String.format("%02d:00-%02d:00", startHour, endHour);
    }
    
    /**
     * 生成活动描述
     */
    private static String generateDescription(String category) {
        String[] focuses = {"党的二十大精神", "党风廉政建设", "基层党组织建设", "党员教育管理", "乡村振兴战略", "疫情防控工作"};
        String[] forms = {"集中学习", "专题研讨", "实地考察", "志愿服务", "经验交流"};
        String[] guides = {"深刻领会精神实质", "提高政治站位", "增强服务意识", "提升工作能力"};
        String[] consciousnesses = {"党性", "服务", "责任", "担当"};
        String[] abilities = {"履职尽责", "服务群众", "解决问题", "创新发展"};
        
        return String.format(DESCRIPTION_TEMPLATE,
                focuses[random.nextInt(focuses.length)],
                forms[random.nextInt(forms.length)],
                guides[random.nextInt(guides.length)],
                consciousnesses[random.nextInt(consciousnesses.length)],
                abilities[random.nextInt(abilities.length)]);
    }
    
    /**
     * 生成报名截止日期
     */
    private static LocalDate generateRegisterDeadline(LocalDate activityDate) {
        return activityDate.minusDays(random.nextInt(7) + 1); // 活动前1-7天截止
    }
    
    /**
     * 生成图片URL
     */
    private static String generateImageUrl() {
        int width = 800;
        int height = 450;
        int id = random.nextInt(1000);
        return String.format("https://picsum.photos/id/%d/%d/%d", id, width, height);
    }
    
    /**
     * 插入活动附件
     */
    private static void insertAttachment(Connection connection, int activityId, int index) throws SQLException {
        String name = ATTACHMENT_NAMES[index % ATTACHMENT_NAMES.length];
        String url = String.format("/attachments/activity_%d_%s", activityId, name);
        
        String insertAttachmentSQL = "INSERT INTO activity_attachments (name, url, activity_id) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(insertAttachmentSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, url);
            pstmt.setInt(3, activityId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * 插入活动报名记录
     */
    private static void insertRegistrations(Connection connection, int activityId, int count) throws SQLException {
        String insertRegistrationSQL = "INSERT INTO activity_registrations (user_id, name, phone, register_time, status, activity_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        for (int i = 0; i < count; i++) {
            int userId = i + 1; // 简单生成用户ID
            String name = generateName();
            String phone = generatePhone();
            LocalDateTime registerTime = LocalDateTime.now().minusDays(random.nextInt(30)).minusHours(random.nextInt(24));
            String status = "registered";
            
            try (PreparedStatement pstmt = connection.prepareStatement(insertRegistrationSQL)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, name);
                pstmt.setString(3, phone);
                pstmt.setTimestamp(4, Timestamp.valueOf(registerTime));
                pstmt.setString(5, status);
                pstmt.setInt(6, activityId);
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * 生成姓名
     */
    private static String generateName() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + lastName;
    }
    
    /**
     * 生成电话号码
     */
    private static String generatePhone() {
        StringBuilder phone = new StringBuilder("138");
        for (int i = 0; i < 8; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
    
    /**
     * 回滚事务
     */
    private static void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
                System.out.println("事务回滚成功！");
            } catch (SQLException e) {
                System.out.println("事务回滚失败：" + e.getMessage());
            }
        }
    }
    
    /**
     * 关闭数据库连接
     */
    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已关闭！");
            } catch (SQLException e) {
                System.out.println("关闭数据库连接失败：" + e.getMessage());
            }
        }
    }
}