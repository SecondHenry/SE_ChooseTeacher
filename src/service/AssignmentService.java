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
                return false;
            }
        }

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
        return true;
    }

    /**
     * Returns all assignment details
     */
    public List<String> listAssignmentDetails() {
        List<Assignment> assignments = assignmentRepo.getAllAssignments();

        return assignments.stream()
                .map(a -> {
                    Teacher t = teacherRepo.getTeacherById(a.getTeacherId());
                    TeachingRequirement r = requirementRepo.getRequirementById(a.getRequirementId());

                    String teacherName = (t != null) ? t.getName() : "unknown teacher";
                    String courseName = (r != null) ? r.getCourseName() : "Unknown course";

                    return String.format("[%s] %s - %s (term：%s) time: %s",
                            a.getAssignmentId(),
                            teacherName,
                            courseName,
                            a.getTerm(),
                            a.getAssignTime().toString().substring(0, 16)); //只显示到分钟
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns unfilled requirements
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
     * Query assignments for a specific teacher
     */
    public List<Assignment> getAssignmentsByRequirement(String requirementId) {
        return assignmentRepo.getAssignmentsByRequirementId(requirementId);
    }

    /**
     * Check whether the teacher meets a certain requirement (can be used for preview)
     */
    public boolean isTeacherSuitable(String teacherId, String requirementId) {
        Teacher teacher = teacherRepo.getTeacherById(teacherId);
        TeachingRequirement requirement = requirementRepo.getRequirementById(requirementId);

        if (teacher == null || requirement == null) {
            return false;
        }

        //check skill
        for (String requiredSkill : requirement.getRequiredSkills()) {
            if (!teacher.hasSkill(requiredSkill)) {
                return false;
            }
        }

        //Check if assigned
        if (assignmentRepo.isAssigned(teacherId, requirementId)) {
            System.out.println("Error: Teacher is already assigned to this requirement.");
            return false;
        }

        //Check if demand is met
        int assigned = assignmentRepo.countByRequirementId(requirementId);
        return assigned < requirement.getTeachersNeeded();
    }

    /**
     * Removes an assignment
     */
    public boolean unassignTeacher(String assignmentId) {
        Assignment assignment = assignmentRepo.getAssignmentById(assignmentId);
        if (assignment == null) {
            System.out.println("Error: Assignment ID does not exist - " + assignmentId);
            return false;
        }

        boolean deleted = assignmentRepo.deleteAssignment(assignmentId);
        if (deleted) {
            System.out.println("Successfully deallocated：" + assignmentId);
        }
        return deleted;
    }
}
