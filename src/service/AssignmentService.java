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
<<<<<<< HEAD
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
=======
     * Core business logic for assigning a teacher to a requirement
     */
    public boolean assignTeacherToRequirement(String teacherId, String requirementId) {
        // 1. Check whether the teacher exists
        Teacher teacher = teacherRepo.getTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Error: Teacher ID does not exist - " + teacherId);
            return false;
        }

        // 2. Check whether the requirement exists
        TeachingRequirement requirement = requirementRepo.getRequirementById(requirementId);
        if (requirement == null) {
            System.out.println("Error: Requirement ID does not exist - " + requirementId);
            return false;
        }

        // 3. Check whether the requirement is already full
        int assignedCount = assignmentRepo.countByRequirementId(requirementId);
        if (assignedCount >= requirement.getTeachersNeeded()) {
            System.out.println("Error: The requirement is full - required "
                    + requirement.getTeachersNeeded() + " teachers, assigned "
                    + assignedCount + " teachers");
            return false;
        }

        // 4. Check skill compatibility
        for (String requiredSkill : requirement.getRequiredSkills()) {
            if (!teacher.hasSkill(requiredSkill)) {
                System.out.println("Error: Teacher lacks required skill - " + requiredSkill);
                System.out.println("Teacher's existing skills: " + teacher.getSkills());
>>>>>>> 1fd0404 (test)
                return false;
            }
        }

<<<<<<< HEAD
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
=======
        // 5. Check for duplicate assignment
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            System.out.println("Error: The teacher is already assigned to this requirement.");
            return false;
        }

        // 6. Create assignment
        Assignment assignment = new Assignment(teacherId, requirementId, requirement.getTerm());
        assignmentRepo.addAssignment(assignment);

        System.out.println("Success! " + teacher.getName() + " has been assigned to requirement "
                + requirement.getCourseName() + " [" + requirementId + "].");
        System.out.println("Assignment ID: " + assignment.getAssignmentId());
>>>>>>> 1fd0404 (test)
        return true;
    }

    /**
<<<<<<< HEAD
     * 查询所有分配详情
=======
     * Returns all assignment details
>>>>>>> 1fd0404 (test)
     */
    public List<String> listAssignmentDetails() {
        List<Assignment> assignments = assignmentRepo.getAllAssignments();

        return assignments.stream()
                .map(a -> {
                    Teacher t = teacherRepo.getTeacherById(a.getTeacherId());
                    TeachingRequirement r = requirementRepo.getRequirementById(a.getRequirementId());

<<<<<<< HEAD
                    String teacherName = (t != null) ? t.getName() : "未知老师";
                    String courseName = (r != null) ? r.getCourseName() : "未知课程";

                    return String.format("[%s] %s - %s (学期：%s) 时间：%s",
=======
                    String teacherName = (t != null) ? t.getName() : "unknown teacher";
                    String courseName = (r != null) ? r.getCourseName() : "Unknown course";

                    return String.format("[%s] %s - %s (term：%s) time: %s",
>>>>>>> 1fd0404 (test)
                            a.getAssignmentId(),
                            teacherName,
                            courseName,
                            a.getTerm(),
                            a.getAssignTime().toString().substring(0, 16)); //只显示到分钟
                })
                .collect(Collectors.toList());
    }

    /**
<<<<<<< HEAD
     * 查询未满足的需求
=======
     * Returns unfilled requirements
>>>>>>> 1fd0404 (test)
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
<<<<<<< HEAD
     * 查询特定老师的分配
=======
     * Query assignments for a specific teacher
>>>>>>> 1fd0404 (test)
     */
    public List<Assignment> getAssignmentsByRequirement(String requirementId) {
        return assignmentRepo.getAssignmentsByRequirementId(requirementId);
    }

    /**
<<<<<<< HEAD
     * 检查老师是否符合某需求（可用于预览）
=======
     * Check whether the teacher meets a certain requirement (can be used for preview)
>>>>>>> 1fd0404 (test)
     */
    public boolean isTeacherSuitable(String teacherId, String requirementId) {
        Teacher teacher = teacherRepo.getTeacherById(teacherId);
        TeachingRequirement requirement = requirementRepo.getRequirementById(requirementId);

        if (teacher == null || requirement == null) {
            return false;
        }

<<<<<<< HEAD
        //检查技能
=======
        //check skill
>>>>>>> 1fd0404 (test)
        for (String requiredSkill : requirement.getRequiredSkills()) {
            if (!teacher.hasSkill(requiredSkill)) {
                return false;
            }
        }

<<<<<<< HEAD
        //检查是否已分配
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            return false;
        }

        //检查是否需求已满
=======
        //Check if assigned
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            System.out.println("Error: Teacher is already assigned to this requirement.");
            return false;
        }

        //Check if demand is met
>>>>>>> 1fd0404 (test)
        int assigned = assignmentRepo.countByRequirementId(requirementId);
        return assigned < requirement.getTeachersNeeded();
    }

    /**
<<<<<<< HEAD
     * 取消分配
=======
     * Removes an assignment
>>>>>>> 1fd0404 (test)
     */
    public boolean unassignTeacher(String assignmentId) {
        Assignment assignment = assignmentRepo.getAssignmentById(assignmentId);
        if (assignment == null) {
<<<<<<< HEAD
            System.out.println("错误：分配ID不存在 - " + assignmentId);
=======
            System.out.println("Error: Assignment ID does not exist - " + assignmentId);
>>>>>>> 1fd0404 (test)
            return false;
        }

        boolean deleted = assignmentRepo.deleteAssignment(assignmentId);
        if (deleted) {
<<<<<<< HEAD
            System.out.println("成功取消分配：" + assignmentId);
=======
            System.out.println("Successfully deallocated：" + assignmentId);
>>>>>>> 1fd0404 (test)
        }
        return deleted;
    }
}
