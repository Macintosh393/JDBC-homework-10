package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {

    private String readSqlFile(String filepath) {
        try {
            return Files.readString(Path.of(filepath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL query file: " + filepath, e);
        }
    }

    public List<MaxProjectCountClient> findMaxProjectsClient() {
        List<MaxProjectCountClient> result = new ArrayList<>();
        String sql = readSqlFile("sql/find_max_projects_client.sql");
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                int projectCount = rs.getInt("project_count");
                result.add(new MaxProjectCountClient(name, projectCount));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker() {
        List<MaxSalaryWorker> result = new ArrayList<>();
        String sql = readSqlFile("sql/find_max_salary_worker.sql");
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                result.add(new MaxSalaryWorker(name, salary));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public List<LongestProject> findLongestProject() {
        List<LongestProject> result = new ArrayList<>();
        String sql = readSqlFile("sql/find_longest_project.sql");
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int monthCount = rs.getInt("month_count");
                result.add(new LongestProject(id, monthCount));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public List<YoungestEldestWorker> findYoungestEldestWorkers() {
        List<YoungestEldestWorker> result = new ArrayList<>();
        String sql = readSqlFile("sql/find_youngest_eldest_workers.sql");
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                String name = rs.getString("name");
                LocalDate birthday = rs.getDate("birthday").toLocalDate();
                result.add(new YoungestEldestWorker(type, name, birthday));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public List<ProjectPrice> printProjectPrices() {
        List<ProjectPrice> result = new ArrayList<>();
        String sql = readSqlFile("sql/print_project_prices.sql");
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                result.add(new ProjectPrice(name, price));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }
    
    // Add a demonstration of parameterized PreparedStatement as requested by Task 6
    public List<MaxSalaryWorker> findWorkersWithSalaryGreaterThan(int minSalary) {
        List<MaxSalaryWorker> result = new ArrayList<>();
        String sql = "SELECT name, salary FROM worker WHERE salary > ? ORDER BY salary DESC";
        Connection conn = Database.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minSalary);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int salary = rs.getInt("salary");
                    result.add(new MaxSalaryWorker(name, salary));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        DatabaseQueryService queryService = new DatabaseQueryService();
        
        System.out.println("--- Max Projects Client ---");
        List<MaxProjectCountClient> maxProjectsClients = queryService.findMaxProjectsClient();
        maxProjectsClients.forEach(System.out::println);
        
        System.out.println("\n--- Max Salary Worker ---");
        List<MaxSalaryWorker> maxSalaryWorkers = queryService.findMaxSalaryWorker();
        maxSalaryWorkers.forEach(System.out::println);
        
        System.out.println("\n--- Longest Project ---");
        List<LongestProject> longestProjects = queryService.findLongestProject();
        longestProjects.forEach(System.out::println);
        
        System.out.println("\n--- Youngest and Eldest Workers ---");
        List<YoungestEldestWorker> youngestEldest = queryService.findYoungestEldestWorkers();
        youngestEldest.forEach(System.out::println);
        
        System.out.println("\n--- Project Prices ---");
        List<ProjectPrice> projectPrices = queryService.printProjectPrices();
        projectPrices.forEach(System.out::println);

        System.out.println("\n--- Workers with salary greater than 3000 (PreparedStatement demo) ---");
        List<MaxSalaryWorker> salaryGreaterThan3000 = queryService.findWorkersWithSalaryGreaterThan(3000);
        salaryGreaterThan3000.forEach(System.out::println);
    }
}
