import com.addery.back.entity.Branch;
import com.addery.back.repository.BranchRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(scanBasePackages = "com.addery.back")
@EnableJpaRepositories("com.addery.back.repository")
public class InsertBranchData {

    public static void main(String[] args) {
        SpringApplication.run(InsertBranchData.class, args);
    }

    @Bean
    public CommandLineRunner loadData(BranchRepository branchRepository) {
        return args -> {
            // 检查是否已有数据
            if (branchRepository.count() == 0) {
                // 创建样板支部数据
                List<Branch> branches = new ArrayList<>();

                // 创建第一个样板支部
                Branch branch1 = new Branch();
                branch1.setName("计算机学院学生第一党支部");
                branch1.setDescription("全国样板党支部，连续三年被评为先进党支部");
                branch1.setImage("https://picsum.photos/id/2/150/100");
                branch1.setLevel("national");
                branch1.setIntroduction("计算机学院学生第一党支部成立于2018年，现有党员35名，其中正式党员28名，预备党员7名。党支部始终坚持以习近平新时代中国特色社会主义思想为指导，深入贯彻落实党的二十大精神，不断加强党的建设，取得了显著成效。");
                List<String> achievements1 = new ArrayList<>();
                achievements1.add("2022年被评为全国样板党支部");
                achievements1.add("2023年被评为先进党支部");
                achievements1.add("2024年被评为先进党支部");
                achievements1.add("2025年被评为先进党支部");
                branch1.setAchievements(achievements1);

                List<Branch.BranchActivity> activities1 = new ArrayList<>();
                Branch.BranchActivity activity1_1 = new Branch.BranchActivity();
                activity1_1.setTitle("主题党日活动");
                activity1_1.setDate("2025-05-20");
                activity1_1.setContent("组织党员参观革命纪念馆，重温入党誓词");
                activities1.add(activity1_1);

                Branch.BranchActivity activity1_2 = new Branch.BranchActivity();
                activity1_2.setTitle("志愿服务活动");
                activity1_2.setDate("2025-06-10");
                activity1_2.setContent("组织党员到社区开展志愿服务，帮助困难群众");
                activities1.add(activity1_2);
                branch1.setActivities(activities1);
                branch1.setCreatedAt(LocalDateTime.of(2025, 1, 15, 8, 30, 0));
                branch1.setUpdatedAt(LocalDateTime.of(2025, 6, 20, 14, 45, 0));
                branches.add(branch1);

                // 创建第二个样板支部
                Branch branch2 = new Branch();
                branch2.setName("马克思主义学院教工党支部");
                branch2.setDescription("省级样板党支部，党建与教学融合成效显著");
                branch2.setImage("https://picsum.photos/id/3/150/100");
                branch2.setLevel("provincial");
                branch2.setIntroduction("马克思主义学院教工党支部成立于2017年，现有党员22名，全部为一线教师。党支部坚持党建与教学科研深度融合，积极探索马克思主义理论教育的新方法、新途径，取得了丰硕的教学科研成果。");
                List<String> achievements2 = new ArrayList<>();
                achievements2.add("2023年被评为省级样板党支部");
                achievements2.add("2024年被评为先进党支部");
                achievements2.add("2025年获得优秀党建项目奖");
                branch2.setAchievements(achievements2);

                List<Branch.BranchActivity> activities2 = new ArrayList<>();
                Branch.BranchActivity activity2_1 = new Branch.BranchActivity();
                activity2_1.setTitle("教学研讨活动");
                activity2_1.setDate("2025-04-15");
                activity2_1.setContent("组织教师开展习近平新时代中国特色社会主义思想教学研讨");
                activities2.add(activity2_1);

                Branch.BranchActivity activity2_2 = new Branch.BranchActivity();
                activity2_2.setTitle("理论学习活动");
                activity2_2.setDate("2025-05-25");
                activity2_2.setContent("学习贯彻党的二十大精神专题讲座");
                activities2.add(activity2_2);
                branch2.setActivities(activities2);
                branch2.setCreatedAt(LocalDateTime.of(2025, 2, 20, 10, 15, 0));
                branch2.setUpdatedAt(LocalDateTime.of(2025, 7, 10, 16, 30, 0));
                branches.add(branch2);

                // 创建第三个样板支部
                Branch branch3 = new Branch();
                branch3.setName("机械工程学院学生第二党支部");
                branch3.setDescription("市级样板党支部，学生党建工作特色鲜明");
                branch3.setImage("https://picsum.photos/id/4/150/100");
                branch3.setLevel("municipal");
                branch3.setIntroduction("机械工程学院学生第二党支部成立于2019年，现有党员28名，其中学生党员25名。党支部以\"机械先锋\"为品牌，积极开展科技创新、志愿服务等特色活动，培养了一批又一批优秀学生党员。");
                List<String> achievements3 = new ArrayList<>();
                achievements3.add("2024年被评为市级样板党支部");
                achievements3.add("2025年获得科技竞赛优秀组织奖");
                achievements3.add("2025年被评为志愿服务先进集体");
                branch3.setAchievements(achievements3);

                List<Branch.BranchActivity> activities3 = new ArrayList<>();
                Branch.BranchActivity activity3_1 = new Branch.BranchActivity();
                activity3_1.setTitle("科技创新活动");
                activity3_1.setDate("2025-03-20");
                activity3_1.setContent("组织学生参加机器人竞赛培训");
                activities3.add(activity3_1);

                Branch.BranchActivity activity3_2 = new Branch.BranchActivity();
                activity3_2.setTitle("志愿服务活动");
                activity3_2.setDate("2025-04-25");
                activity3_2.setContent("到敬老院开展义务维修活动");
                activities3.add(activity3_2);
                branch3.setActivities(activities3);
                branch3.setCreatedAt(LocalDateTime.of(2025, 3, 5, 14, 20, 0));
                branch3.setUpdatedAt(LocalDateTime.of(2025, 6, 15, 9, 15, 0));
                branches.add(branch3);

                // 创建第四个样板支部
                Branch branch4 = new Branch();
                branch4.setName("电子工程学院教工第一党支部");
                branch4.setDescription("省级样板党支部，科研与党建协同发展");
                branch4.setImage("https://picsum.photos/id/5/150/100");
                branch4.setLevel("provincial");
                branch4.setIntroduction("电子工程学院教工第一党支部成立于2016年，现有党员18名，其中教授5名，副教授8名。党支部坚持科研与党建协同发展，积极发挥党员教师的先锋模范作用，在电子信息领域取得了多项重要科研成果。");
                List<String> achievements4 = new ArrayList<>();
                achievements4.add("2022年被评为省级样板党支部");
                achievements4.add("2023年获得国家自然科学基金项目10项");
                achievements4.add("2024年获得科技进步奖2项");
                branch4.setAchievements(achievements4);

                List<Branch.BranchActivity> activities4 = new ArrayList<>();
                Branch.BranchActivity activity4_1 = new Branch.BranchActivity();
                activity4_1.setTitle("科研交流活动");
                activity4_1.setDate("2025-02-28");
                activity4_1.setContent("组织教师参加国际电子信息学术会议");
                activities4.add(activity4_1);

                Branch.BranchActivity activity4_2 = new Branch.BranchActivity();
                activity4_2.setTitle("主题党日活动");
                activity4_2.setDate("2025-03-15");
                activity4_2.setContent("参观电子信息产业园区");
                activities4.add(activity4_2);
                branch4.setActivities(activities4);
                branch4.setCreatedAt(LocalDateTime.of(2025, 1, 10, 11, 45, 0));
                branch4.setUpdatedAt(LocalDateTime.of(2025, 7, 5, 15, 20, 0));
                branches.add(branch4);

                // 创建第五个样板支部
                Branch branch5 = new Branch();
                branch5.setName("经济管理学院学生第三党支部");
                branch5.setDescription("市级样板党支部，实践育人成效显著");
                branch5.setImage("https://picsum.photos/id/6/150/100");
                branch5.setLevel("municipal");
                branch5.setIntroduction("经济管理学院学生第三党支部成立于2019年，现有党员32名，其中学生党员30名。党支部以\"经世济民\"为宗旨，积极开展社会实践活动，培养学生的社会责任感和实践能力。");
                List<String> achievements5 = new ArrayList<>();
                achievements5.add("2023年被评为市级样板党支部");
                achievements5.add("2024年获得社会实践优秀团队奖");
                achievements5.add("2025年被评为先进党支部");
                branch5.setAchievements(achievements5);

                List<Branch.BranchActivity> activities5 = new ArrayList<>();
                Branch.BranchActivity activity5_1 = new Branch.BranchActivity();
                activity5_1.setTitle("社会实践活动");
                activity5_1.setDate("2025-05-10");
                activity5_1.setContent("到企业开展调研活动");
                activities5.add(activity5_1);

                Branch.BranchActivity activity5_2 = new Branch.BranchActivity();
                activity5_2.setTitle("志愿服务活动");
                activity5_2.setDate("2025-06-05");
                activity5_2.setContent("到乡村开展电商培训");
                activities5.add(activity5_2);
                branch5.setActivities(activities5);
                branch5.setCreatedAt(LocalDateTime.of(2025, 2, 15, 13, 30, 0));
                branch5.setUpdatedAt(LocalDateTime.of(2025, 6, 25, 10, 45, 0));
                branches.add(branch5);

                // 创建第六个样板支部
                Branch branch6 = new Branch();
                branch6.setName("外国语学院教工第二党支部");
                branch6.setDescription("市级样板党支部，国际化党建特色突出");
                branch6.setImage("https://picsum.photos/id/7/150/100");
                branch6.setLevel("municipal");
                branch6.setIntroduction("外国语学院教工第二党支部成立于2018年，现有党员15名，其中具有海外留学经历的教师8名。党支部以国际化党建为特色，积极开展中外文化交流活动，推动党建工作与国际化教育深度融合。");
                List<String> achievements6 = new ArrayList<>();
                achievements6.add("2024年被评为市级样板党支部");
                achievements6.add("2025年获得国际化教育先进集体奖");
                achievements6.add("2025年被评为先进党支部");
                branch6.setAchievements(achievements6);

                List<Branch.BranchActivity> activities6 = new ArrayList<>();
                Branch.BranchActivity activity6_1 = new Branch.BranchActivity();
                activity6_1.setTitle("中外文化交流活动");
                activity6_1.setDate("2025-04-10");
                activity6_1.setContent("组织教师与国际学生开展文化交流");
                activities6.add(activity6_1);

                Branch.BranchActivity activity6_2 = new Branch.BranchActivity();
                activity6_2.setTitle("主题党日活动");
                activity6_2.setDate("2025-05-05");
                activity6_2.setContent("学习习近平外交思想");
                activities6.add(activity6_2);
                branch6.setActivities(activities6);
                branch6.setCreatedAt(LocalDateTime.of(2025, 3, 10, 9, 20, 0));
                branch6.setUpdatedAt(LocalDateTime.of(2025, 7, 15, 14, 30, 0));
                branches.add(branch6);

                // 保存所有样板支部数据
                branchRepository.saveAll(branches);
                System.out.println("样板支部数据插入完成！");
            } else {
                System.out.println("样板支部数据已存在，无需插入！");
            }

            // 退出程序
            System.exit(0);
        };
    }
}