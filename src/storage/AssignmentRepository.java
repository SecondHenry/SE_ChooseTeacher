package storage;

import model.Assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class for managing Assignment data
 * Uses FileStorage to read from and write to a text file
 * Storage format: assignmentId|teacherId|requirementId|assignTime|term
 */
public class AssignmentRepository {

    private static final String FILE_PATH = "assignments.txt";
    private List<Assignment> assignments;

    public AssignmentRepository() {
        this.assignments = new ArrayList<>();
    }

    /**
     * Loads all assignments from the next file into memory
     */
    public void loadAll() {
        assignments.clear();
        List<String> lines = FileStorage.readLines(FILE_PATH);

        for (String line : lines) {
            try {
<<<<<<< HEAD
                //使用之前定义的fromFileString方法
                Assignment assignment = Assignment.fromFileString(line);
                assignments.add(assignment);
            } catch (Exception e) {
                System.err.println("跳过无效的分配记录：" + line);
            }
        }
        System.out.println("已加载 " + assignments.size() + " 条分配记录");
=======
                //Use the previously defined fromFileString method
                Assignment assignment = Assignment.fromFileString(line);
                assignments.add(assignment);
            } catch (Exception e) {
                System.err.println("Skip invalid allocation records：" + line);
            }
        }
        System.out.println("loaded" + assignments.size() + " allocation record");
>>>>>>> 1fd0404 (test)
    }

    /**
     * Saves all assignments from memory back to the next file
     */
    public void saveAll() {
        List<String> lines = assignments.stream()
                .map(Assignment::toFileString)
                .collect(Collectors.toList());
        FileStorage.writeLines(FILE_PATH, lines);
<<<<<<< HEAD
        System.out.println("| 已保存 " + lines.size() + " 条分配记录到文件");
=======
        System.out.println("| saved" + lines.size() + "Assign records to file ");
>>>>>>> 1fd0404 (test)
    }

    /**
     * Adds a new assignment to the repository
     *
     * @param assignment The assignment to add
     */
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
<<<<<<< HEAD
        saveAll();  //实时持久化
        System.out.println("+ 已添加分配：" + assignment.getAssignmentId());
=======
        saveAll();  //real-time persistence
        System.out.println("+Allocation added ：" + assignment.getAssignmentId());
>>>>>>> 1fd0404 (test)
    }

    /**
     * Adds multiple assignments at once (batch operation)
     *
     * @param newAssignments List of assignments to add
     */
    public void addAllAssignments(List<Assignment> newAssignments) {
        assignments.addAll(newAssignments);
        saveAll();
<<<<<<< HEAD
        System.out.println("+ 已批量添加：" + newAssignments.size() + " 条分配记录");
=======
        System.out.println("+ Added in batches：" + newAssignments.size() + " allocation record");
>>>>>>> 1fd0404 (test)
    }

    /**
     * Returns the list of all assignments
     */
    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignments);
    }

    /**
     * Finds an assignment by its ID
     *
     * @param assignmentId The assignment ID to search for
     * @return Assignment object or null if not found
     */
    public Assignment getAssignmentById(String assignmentId) {
        return assignments.stream()
                .filter(a -> a.getAssignmentId().equals(assignmentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds assignments by teacher ID
     *
     * @param teacherId The Teacher ID to search for
     * @reutrn List of assignments (empty list if none)
     */
    public List<Assignment> getAssignmentsByTeacherId(String teacherId) {
        return assignments.stream()
                .filter(a -> a.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    /**
     * Finds all assignments for a specific requirement
     *
     * @param requirementId The requirement ID to search for
     * @return List of assignments (empty list if none)
     */
    public List<Assignment> getAssignmentsByRequirementId(String requirementId) {
        return assignments.stream()
                .filter(a -> a.getRequirementId().equals(requirementId))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a teacher is already assigned to a requirement
     *
     * @param teacherId     Teacher to check
     * @param requirementId Requirement to check
     * @return true if already assigned
     */
    public boolean isAssigned(String teacherId, String requirementId) {
        return assignments.stream()
                .anyMatch(a -> a.getTeacherId().equals(teacherId)
                        && a.getRequirementId().equals(requirementId));
    }

    /**
     * Counts how many teachers are assigned to a requirement
     *
     * @param requirementId The requirement to check
     * @return Number of assigned teachers
     */
    public int countByRequirementId(String requirementId) {
        return (int) assignments.stream()
                .filter(a -> a.getRequirementId().equals(requirementId))
                .count();
    }

    /**
     * Deletes an assignment by ID
     *
     * @param assignmentId The assignment to delete
     * @return true if deletion was successful
     */
    public boolean deleteAssignment(String assignmentId) {
        boolean removed = assignments.removeIf(a -> a.getAssignmentId().equals(assignmentId));
        if (removed) {
<<<<<<< HEAD
            saveAll();  //立刻保存
            System.out.println("- 已删除分配：" + assignmentId);
=======
            saveAll();  //instant save
            System.out.println("- Assignment deleted：" + assignmentId);
>>>>>>> 1fd0404 (test)
        }
        return removed;
    }

    /**
     * Deletes all assignments for a requirement (used when requirement is deleted)
     *
     * @param requirementId The requirement whose assignments should be deleted
     * @return Number of deleted assignments
     */
    public int deleteByRequirementId(String requirementId) {
        List<Assignment> toRemove = getAssignmentsByRequirementId(requirementId);
        if (!toRemove.isEmpty()) {
            assignments.removeAll(toRemove);
            saveAll();
<<<<<<< HEAD
            System.out.println("- 已删除：" + toRemove.size() + " 条相关分配记录");
=======
            System.out.println("- Deleted：" + toRemove.size() + " related allocation records");
>>>>>>> 1fd0404 (test)
        }
        return toRemove.size();
    }

    /**
     * Delete all assignment for a teacher (used when teacher is deleted)
     *
     * @param teacherId The teacher whose assignments should be deleted
     * @return Number of deleted assignments
     */
    public int deleteByTeacherId(String teacherId) {
        List<Assignment> toRemove = getAssignmentsByTeacherId(teacherId);
        if (!toRemove.isEmpty()) {
            assignments.removeAll(toRemove);
            saveAll();
<<<<<<< HEAD
            System.out.println("- 已删除：" + toRemove.size() + " 条相关分配记录");
=======
            System.out.println("- Deleted：" + toRemove.size() + " related allocation records");
>>>>>>> 1fd0404 (test)
        }
        return toRemove.size();
    }

    /**
     * Gets All assignments for a specific term
     *
     * @param term The term to filter by
     * @return List of assignments in that term
     */
    public List<Assignment> getAssignmentsByTerm(String term) {
        return assignments.stream()
                .filter(a -> a.getTerm().equals(term))
                .collect(Collectors.toList());
    }

    /**
     * Clear all assignments (use with caution!)
     */
    public void clearAll() {
        assignments.clear();
        saveAll();
<<<<<<< HEAD
        System.out.println("# 已清空所有分配记录！");
=======
        System.out.println("#All allocation records have been cleared! ");
>>>>>>> 1fd0404 (test)
    }
}
