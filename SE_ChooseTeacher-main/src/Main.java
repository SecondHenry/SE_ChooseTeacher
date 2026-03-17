import service.AssignmentService;
import storage.AssignmentRepository;
import storage.RequirementRepository;
import storage.TeacherRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("[System] Booting up Teaching Assignment System...");

        // 1. create all Repository
        TeacherRepository teacherRepo = new TeacherRepository();
        RequirementRepository requirementRepo = new RequirementRepository();
        AssignmentRepository assignmentRepo = new AssignmentRepository();

        // 2. load all data
        teacherRepo.loadAll();
        requirementRepo.loadAll();
        assignmentRepo.loadAll();

        // 3. create Service business logic
        AssignmentService assignmentService = new AssignmentService(
                teacherRepo, requirementRepo, assignmentRepo
        );

        // 4. start CLI controller
        Scanner scanner = new Scanner(System.in);
        CliController cli = new CliController(scanner, teacherRepo, requirementRepo, assignmentService);

        System.out.println("\n===========================================");
        System.out.println("  Welcome to the Teaching Assignment System");
        System.out.println("===========================================");

        // Start interactive menu
        cli.start();

        // 5. Save all data before exiting
        System.out.println("\n[System] Saving all data to files before exit...");
        teacherRepo.saveAll();
        requirementRepo.saveAll();
        assignmentRepo.saveAll();
        System.out.println("[System] Shutdown complete. Goodbye!");

        scanner.close();
    }
}