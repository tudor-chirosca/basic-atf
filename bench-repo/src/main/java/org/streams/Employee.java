package org.streams;

public class Employee {
    private String name;
    private String department;
    private double salary;

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public static Employee[] getStaff() {
        Employee[] staff = {
                new Employee("Roy", "IT", 50000),
                new Employee("Bob", "IT", 50000),
                new Employee("Tob", "IT", 60000),
                new Employee("Rob", "Management", 70000),
                new Employee("Mac", "Management", 100000)
        };
        return staff;
    }

}
