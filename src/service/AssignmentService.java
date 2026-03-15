package service;

import model.Teacher;
import model.TeachingRequirement;
import model.Assignment;
import storage.TeacherRepository;
import storage.RequirementRepository;
import storage.AssignmentRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AssignmentService {
    private final TeacherRepository teacherRepo;
    private final RequirementRepository requirementRepo;
    private final AssignmentRepository assignmentRepo;

    // Constructor
    public AssignmentService(TeacherRepository teacherRepo,
                             RequirementRepository requirementRepo,
                             AssignmentRepository assignmentRepo) {
        this.teacherRepo = teacherRepo;
        this.requirementRepo = requirementRepo;
        this.assignmentRepo = assignmentRepo;
    }

    /**
     * 核心业务：分配老师需求
     */
    public boolean assignTeacherToRequirement(String teacherId, String requirementId) {
        //1、检查老师和需求是否存在
        Teacher teacher = teacherRepo.getTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("错误：老师ID不存在 - " + teacherId);
            return false;
        }

        TeachingRequirement requirement = requirementRepo.getRequirementById(requirementId);
        if (requirement == null) {
            System.out.println("错误：需求ID不存在 - " + requirementId);
            return false;
        }

        //2、检查需求是否已满
        int assignedCount = assignmentRepo.countByRequirementId(requirementId);
        if (assignedCount >= requirement.getTeachersNeeded()) {
            System.out.println("错误：该需求已满 - 需要 " + requirement.getTeachersNeeded() +
                    " 位老师，已分配 " + assignedCount + " 位");
            return false;
        }

        //3、技能匹配检查
        for (String requiredSkill : requirement.getRequiredSkills()) {
            if (!teacher.hasSkill(requiredSkill)) {
                System.out.println("错误：老师缺少必要技能 - " + requiredSkill);
                System.out.println("老师现有技能：" + teacher.getSkills());
                return false;
            }
        }

        //4、检查是否重复分配
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            System.out.println("错误：老师已经分配到这个需求");
        }

        //5、通过检查，创建分配
        Assignment assignment = new Assignment(teacherId, requirementId, requirement.getTerm());
        assignmentRepo.addAssignment(assignment);
        assignmentRepo.saveAll();   //立即保存

        System.out.println("成功！已将 " + teacher.getName() + " 分配到需求 " +
                requirement.getCourseName() + " [" + requirementId + "]");
        System.out.println("分配ID：" + assignment.getAssignmentId());
        return true;
    }

    /**
     * 查询所有分配详情
     */
    public List<String> listAssignmentDetails() {
        List<Assignment> assignments = assignmentRepo.getAllAssignments();

        return assignments.stream()
                .map(a -> {
                    Teacher t = teacherRepo.getTeacherById(a.getTeacherId());
                    TeachingRequirement r = requirementRepo.getRequirementById(a.getRequirementId());

                    String teacherName = (t != null) ? t.getName() : "未知老师";
                    String courseName = (r != null) ? r.getCourseName() : "未知课程";

                    return String.format("[%s] %s - %s (学期：%s) 时间：%s",
                            a.getAssignmentId(),
                            teacherName,
                            courseName,
                            a.getTerm(),
                            a.getAssignTime().toString().substring(0, 16)); //只显示到分钟
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询未满足的需求
     */
    public List<TeachingRequirement> listUnfilledRequirements() {
        return requirementRepo.getAllRequirement().stream()
                .filter(req -> {
                    int assigned = assignmentRepo.countByRequirementId(req.getRequirementId());
                    return assigned < req.getTeachersNeeded();
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询特定老师的分配
     */
    public List<Assignment> getAssignmentsByRequirement(String requirementId) {
        return assignmentRepo.getAssignmentsByRequirementId(requirementId);
    }

    /**
     * 检查老师是否符合某需求（可用于预览）
     */
    public boolean isTeacherSuitable(String teacherId, String requirementId) {
        Teacher teacher = teacherRepo.getTeacherById(teacherId);
        TeachingRequirement requirement = requirementRepo.getRequirementById(requirementId);

        if (teacher == null || requirement == null) {
            return false;
        }

        //检查技能
        for (String requiredSkill : requirement.getRequiredSkills()) {
            if (!teacher.hasSkill(requiredSkill)) {
                return false;
            }
        }

        //检查是否已分配
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            return false;
        }

        //检查是否需求已满
        int assigned = assignmentRepo.countByRequirementId(requirementId);
        return assigned < requirement.getTeachersNeeded();
    }

    /**
     * 取消分配
     */
    public boolean unassignTeacher(String assignmentId) {
        Assignment assignment = assignmentRepo.getAssignmentById(assignmentId);
        if (assignment == null) {
            System.out.println("错误：分配ID不存在 - " + assignmentId);
            return false;
        }

        boolean deleted = assignmentRepo.deleteAssignment(assignmentId);
        if (deleted) {
            System.out.println("成功取消分配：" + assignmentId);
        }
        return deleted;
    }
}
