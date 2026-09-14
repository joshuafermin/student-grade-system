package com.joshua;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    static Scanner input = new Scanner(System.in);

    static final String URL = "jdbc:mysql://localhost:3306/student_system";

    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public enum OPTION{ADD, VIEW, SEARCH, UPDATE, REMOVE, SORT, EXIT}

    public static void addStudent() {
        System.out.println("\n=== ADD STUDENT ===");

        int studentID;
        while (true) {
            try {
                System.out.print("Student ID: ");
                String tempID = input.nextLine().trim();

                if (tempID.isEmpty()) {
                    throw new IllegalArgumentException("ID can't be empty");
                }

                studentID = Integer.parseInt(tempID);

                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Student ID must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        String studentName;
        while (true) {
            System.out.print("Student Name: ");
            studentName = input.nextLine().trim();

            if (studentName.isEmpty()) {
                System.out.println("ERROR: Name cannot be empty.");
            } else {
                break;
            }
        }

        int studentScore;
        while (true) {
            try {
                System.out.print("Score: ");
                String tempScore = input.nextLine().trim();

                if (tempScore.isEmpty()) {
                    throw new IllegalArgumentException("Score can't be empty");
                }

                studentScore = Integer.parseInt(tempScore);

                if (studentScore < 0 || studentScore > 100) {
                    System.out.println("ERROR: Score must be 0-100.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Score must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        String sql = "INSERT INTO students (id, name, score) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)
            ){

            statement.setInt(1, studentID);
            statement.setString(2, studentName);
            statement.setInt(3, studentScore);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }
    
    public static void viewStudents(){
        System.out.println("\n=== STUDENTS ===");

        String sql = "SELECT id, name, score FROM students";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery()) {

        boolean found = false;

            while (result.next()) {

                found = true;

                int id = result.getInt("id");
                String name = result.getString("name");
                int score = result.getInt("score");

                System.out.println("ID: " + id + " | Name: " + name + " | Score: " + score);
        }

        if (!found) {
            System.out.println("No student records found.");
        }

        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    public static void searchStudent() {
        System.out.println("\n=== SEARCH STUDENT ===");

        int studentID;

        while (true) {
            try {
                System.out.print("Enter student ID to search: ");
                String tempID = input.nextLine().trim();

                if (tempID.isEmpty()) {
                    throw new IllegalArgumentException("Student ID cannot be empty.");
                }

                studentID = Integer.parseInt(tempID);
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Student ID must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        String sql = "SELECT id, name, score FROM students WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentID);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int score = result.getInt("score");

                    System.out.println("\nStudent found!");
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Score: " + score);
                } else {
                    System.out.println("Student not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    public static void updateStudent() {
        System.out.println("\n=== UPDATE STUDENT ===");
        int studentID;

        while (true) {
            try {

                System.out.print("Enter Student ID: ");
                String tempID = input.nextLine().trim();

                if (tempID.isEmpty()) {
                    throw new IllegalArgumentException("Student ID cannot be empty.");
                }

                studentID = Integer.parseInt(tempID);

                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Student ID must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }


        String searchSQL = "SELECT id, name, score FROM students WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement searchStatement = connection.prepareStatement(searchSQL)
            ){

            searchStatement.setInt(1, studentID);

            try (ResultSet result = searchStatement.executeQuery()) {

                if (!result.next()) {
                    System.out.println("\nStudent with ID " + studentID + " not found.");
                return;
                }

                String currentName = result.getString("name");

                int currentScore = result.getInt("score");

                System.out.println("\nCurrent Information:");
                System.out.println("ID: " + studentID);
                System.out.println("Name: " + currentName);
                System.out.println("Score: " + currentScore);

                String newName;
                while (true) {
                    System.out.print("\nEnter NEW student name: ");
                    newName = input.nextLine().trim();

                    if (newName.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                    } else {
                        break;
                    }
                }

                int newScore;
                while (true) {
                    try {
                        System.out.print("Enter NEW student score: ");

                        String tempScore = input.nextLine().trim();

                        if (tempScore.isEmpty()) {
                            throw new IllegalArgumentException("Score cannot be empty.");
                        }

                        newScore = Integer.parseInt(tempScore);

                        if (newScore < 0 || newScore > 100) {
                            System.out.println("ERROR: score must be 0-100.");
                            continue;
                        }

                        break;

                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Score must be an integer.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }

                System.out.println("\nNew Information:");
                System.out.println("ID: " + studentID);
                System.out.println("NEW name: " + newName);
                System.out.println("NEW score: " + newScore);

                while (true) {
                    System.out.print("\nProceed with modification? [Y/N]: ");
                    String choice = input.nextLine().trim();

                    if (choice.isEmpty()) {
                        System.out.println("Choice cannot be blank, choose either [Y/N]");
                    } else if (choice.equalsIgnoreCase("Y")) {
                        String updateSQL ="UPDATE students SET name = ?, score = ? WHERE id = ?";

                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                            updateStatement.setString(1, newName);
                            updateStatement.setInt(2, newScore);
                            updateStatement.setInt(3, studentID);

                            int rowsAffected = updateStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("\nStudent updated successfully!");
                            } else {
                                System.out.println("\nStudent update failed.");
                            }
                        } catch (SQLException e) {
                            System.out.println("Database error while updating student!");
                            e.printStackTrace();
                        }
                        return;

                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("Modification cancelled.");
                        return;
                    } else {
                        System.out.println("ERROR: Choose between [Y/N]");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    public static void removeStudent() {
        System.out.println("\n=== REMOVE STUDENT ===");
        
        int studentID;

        while (true) {
            try {
                System.out.print("Enter Student ID: ");
                String tempID = input.nextLine().trim();

                if (tempID.isEmpty()) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
                }

                studentID = Integer.parseInt(tempID);
                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Student ID must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        String searchSQL = "SELECT id, name, score FROM students WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement searchStatement = connection.prepareStatement(searchSQL)
            ){

            searchStatement.setInt(1, studentID);

            try (ResultSet result = searchStatement.executeQuery()) {

                if (!result.next()) {
                    System.out.println("\nStudent with ID " + studentID + " not found.");
                return;
                }

                String studentName = result.getString("name");

                int studentScore = result.getInt("score");

                System.out.println("\nStudent Information:");
                System.out.println("ID: " + studentID);
                System.out.println("Name: " + studentName);
                System.out.println("Score: " + studentScore);

                while (true) {
                    System.out.print("\nProceed with deletion? [Y/N]: ");
                    String choice = input.nextLine().trim();

                    if (choice.isEmpty()) {
                        System.out.println("Choice cannot be blank, choose either [Y/N]");
                    } else if (choice.equalsIgnoreCase("Y")) {
                        String deleteSQL = "DELETE FROM students WHERE id = ?";

                        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSQL)
                        ){
                            deleteStatement.setInt(1, studentID);

                            int rowsAffected = deleteStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("\nStudent removed successfully!");
                            } else {
                                System.out.println("\nStudent removal failed.");
                            }

                        } catch (SQLException e) {
                            System.out.println("Database error while removing student!");
                            e.printStackTrace();
                        }
                        return;
                    } else if (choice.equalsIgnoreCase("N")) {
                        System.out.println("Deletion cancelled.");
                        return;
                    } else {
                        System.out.println("ERROR: Choose between [Y/N]");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    public static void sortStudents() {
        System.out.println("\n=== SORT STUDENTS ===");
        System.out.println("1. ID ascending");
        System.out.println("2. Score ascending");
        System.out.println("3. Score descending");

        int choice;

        while (true) {
            try {
                System.out.print("Choose sorting option: ");
                String tempChoice = input.nextLine().trim();

                if (tempChoice.isEmpty()) {
                    throw new IllegalArgumentException("Choice cannot be empty.");
                }

                choice = Integer.parseInt(tempChoice);

                if (choice < 1 || choice > 3) {
                    System.out.println("ERROR: Choose between 1-3.");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Choice must be an integer.");
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        String sql;

        if (choice == 1) {
            sql = "SELECT id, name, score FROM students ORDER BY id ASC";
        } else if (choice == 2) {
            sql = "SELECT id, name, score FROM students ORDER BY score ASC";
        } else {
            sql = "SELECT id, name, score FROM students ORDER BY score DESC";
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n=== SORTED STUDENTS ===");

            while (result.next()) {
                found = true;

                int id = result.getInt("id");
                String name = result.getString("name");
                int score = result.getInt("score");

                System.out.println("ID: " + id +" | Name: " + name +" | Score: " + score);
            }

            if (!found) {
                System.out.println("No student records found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        boolean isRunning = true;

        while(isRunning){
            System.out.println("=== STUDENT GRADE SYSTEM v2===");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Remove Student");
            System.out.println("6. Sort Students");
            System.out.println("7. Exit");
            
            OPTION option;

            while (true) { 
                try{
                    System.out.println("What would you like to do?");
                    String tempChoice = input.nextLine().trim();

                    if(tempChoice.isEmpty()){
                        throw new IllegalArgumentException("choice can't be empty.");
                    }
                
                    int choice = Integer.parseInt(tempChoice);

                    if(choice < 1 || choice > 7){
                        throw new IllegalArgumentException("choose between 1-7.");
                    }

                    option = OPTION.values()[choice - 1];

                    break;
                }catch(NumberFormatException e){
                    System.out.println("ERROR: enter the number of process that you wish to access.");
                }catch(IllegalArgumentException e){
                    System.out.println("ERROR: " + e.getMessage());
                }
            }

            switch(option){
                case ADD:
                    addStudent();
                    break;
                case VIEW:
                    viewStudents();
                    break;
                case SEARCH:
                    searchStudent();
                    break;
                case UPDATE:
                    updateStudent();
                    break;
                case REMOVE:
                    removeStudent();
                    break;
                case SORT:
                    sortStudents();
                    break;
                case EXIT:
                    System.out.println("Exiting program...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("ERROR");
            }
        }
        input.close();
    }
}