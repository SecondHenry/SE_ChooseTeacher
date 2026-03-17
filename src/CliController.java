import model.Teacher;
import model.TeachingRequirement;
import service.AssignmentService;
import storage.RequirementRepository;
import storage.TeacherRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class CliController {
    private final Scanner scanner;
    private final TeacherRepository teacherRepo;
    private final RequirementRepository requirementRepo;
    private final AssignmentService assignmentService;

    public CliController(Scanner scanner, TeacherRepository teacherRepo,
                         RequirementRepository requirementRepo, AssignmentService assignmentService) {
        this.scanner = scanner;
        this.teacherRepo = teacherRepo;
        this.requirementRepo = requirementRepo;
        this.assignmentService = assignmentService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            System.out.println("\n-------------------------------------------");
            try {
                switch (choice) {
                    case "1":
                        handleAddTeacher();
                        break;
                    case "2":
                        handleAddRequirement();
                        break;
                    case "3":
                        handleAssignTeacher();
                        break;
                    case "4":
                        handleViewAssignments();
                        break;
                    case "5":
                        running = false;
                        break;
                    default:
                        System.out.println("[ERROR] Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] An unexpected error occurred: " + e.getMessage());
            }
            if (running) {
                System.out.println("-------------------------------------------");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add a new Teacher");
        System.out.println("2. Add a Teaching Requirement");
        System.out.println("3. Assign Teacher to Requirement");
        System.out.println("4. View Assignments & Requirements");
        System.out.println("5. Save and Exit");
        System.out.print("Please select an option (1-5): ");
    }

    private void handleAddTeacher() {
        System.out.println(">>> ADD NEW TEACHER <<<");
        System.out.print("Enter Teacher ID (e.g., T001): ");
        String id = scanner.nextLine().trim();
        
        System.out.print("Enter Teacher Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Skills (comma separated, e.g., Java,Python): ");
        Set<String> skills = parseCommaSeparated(scanner.nextLine());
        
        System.out.print("Enter Completed Trainings (comma separated, or press Enter to skip): ");
        Set<String> trainings = parseCommaSeparated(scanner.nextLine());

        try {
            Teacher teacher = new Teacher(id, name, skills, trainings);
            teacherRepo.addTeacher(teacher);
            System.out.println("[SUCCESS] Teacher added:\n" + teacher.toDisplayString());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Failed to add teacher: " + e.getMessage());
        }
    }

    private void handleAddRequirement() {
        System.out.println(">>> ADD TEACHING REQUIREMENT <<<");
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine().trim();
        
        System.out.print("Enter Term (e.g., 2026 Spring): ");
        String term = scanner.nextLine().trim();
        
        System.out.print("Enter Required Skills (comma separated): ");
        Set<String> skills = parseCommaSeparated(scanner.nextLine());
        
        System.out.print("Enter number of teachers needed: ");
        int needed = 1;
        try {
            needed = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[WARNING] Invalid number. Defaulting to 1.");
        }

        try {
            TeachingRequirement req = new TeachingRequirement(courseName, term, skills, needed);
            requirementRepo.addRequirement(req);
            System.out.println("[SUCCESS] Requirement added:\n" + req.toDisplayString());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] Failed to add requirement: " + e.getMessage());
        }
    }

    private void handleAssignTeacher() {
        System.out.println(">>> ASSIGN TEACHER TO REQUIREMENT <<<");
        System.out.print("Enter Teacher ID: ");
        String teacherId = scanner.nextLine().trim();

        System.out.print("Enter Requirement ID: ");
        String reqId = scanner.nextLine().trim();

        boolean success = assignmentService.assignTeacherToRequirement(teacherId, reqId);

        // Only update requirement state if assignment succeeded
        if (success) {
            TeachingRequirement req = requirementRepo.getRequirementById(reqId);
            if (req != null) {
                req.incrementAssignedCount();
                requirementRepo.saveAll();
            }
        }
    }

    private void handleViewAssignments() {
        System.out.println(">>> VIEW ALL ASSIGNMENTS <<<");
        List<String> details = assignmentService.listAssignmentDetails();
        if (details.isEmpty()) {
            System.out.println("No assignments found.");
        } else {
            for (String detail : details) {
                System.out.println(detail);
            }
        }

        System.out.println("\n>>> UNFILLED REQUIREMENTS <<<");
        List<TeachingRequirement> unfilled = assignmentService.listUnfilledRequirements();
        if (unfilled.isEmpty()) {
            System.out.println("All teaching requirements are fully staffed!");
        } else {
            for (TeachingRequirement req : unfilled) {
                System.out.println(req.toDisplayString());
            }
        }
    }

    // Converts a comma-separated string entered by the user into Set
    private Set<String> parseCommaSeparated(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new HashSet<>();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}