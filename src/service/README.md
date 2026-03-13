/**
 * 在AssignmentService中可以
 * 调用teacherRepo.getTeacherById()检查老师
 * 调用requirementRepo.getRequirementById()检查需求
 * 调用assignmentRepo.isAssigned()避免重复
 * 调用assignmentRepo.countByRequirementId()检查需求是否已满
 * 调用assignmentRepo.addAssignment()+saveAll()保存分配
 */
 
 //E同学建议在Main.java中如此初始化
 
import service.AssignmentService;
import storage.AssignmentRepository;
import storage.RequirementRepository;
import storage.TeacherRepository;

public class Main {
    public static void main(String[] args) {

        //1. 创建所有Repository
        TeacherRepository teacherRepo = new TeacherRepository();
        RequirementRepository requirementRepo = new RequirementRepository();
        AssignmentRepository assignmentRepo = new AssignmentRepository();

        //2. 加载所有数据
        teacherRepo.loadAll();
        requirementRepo.loadAll();
        assignmentRepo.loadAll();

        //3. 创建Service
        AssignmentService assignmentService = new AssignmentService(
                teacherRepo, requirementRepo, assignmentRepo
        );

        //4.启动CLI……

    }
}
