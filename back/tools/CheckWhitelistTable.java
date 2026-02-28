import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckWhitelistTable {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/party_management_system?useUnicode=true&characterEncoding=UTF-8&characterSetResults=utf8mb4&connectionCollation=utf8mb4_unicode_ci&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false";
        String username = "root";
        String password = "123456";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'registration_whitelist'";
            ResultSet resultSet = statement.executeQuery(checkTableSql);
            
            if (resultSet.next()) {
                System.out.println("注册白名单表(registration_whitelist)已存在");
                
                // 查询表结构
                String describeTableSql = "DESCRIBE registration_whitelist";
                ResultSet describeResultSet = statement.executeQuery(describeTableSql);
                System.out.println("表结构:");
                while (describeResultSet.next()) {
                    System.out.println(describeResultSet.getString(1) + " " + describeResultSet.getString(2) + " " + describeResultSet.getString(3));
                }
                
                // 查询表中的数据
                String selectDataSql = "SELECT * FROM registration_whitelist";
                ResultSet dataResultSet = statement.executeQuery(selectDataSql);
                System.out.println("表中数据:");
                while (dataResultSet.next()) {
                    System.out.println("id: " + dataResultSet.getInt("id") + ", phone: " + dataResultSet.getString("phone") + ", id_card: " + dataResultSet.getString("id_card") + ", remark: " + dataResultSet.getString("remark"));
                }
            } else {
                System.out.println("注册白名单表(registration_whitelist)不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}