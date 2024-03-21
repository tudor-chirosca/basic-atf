package org.streams;

import java.util.Arrays;

public class EmployeesPredicate {
    public static void main(String[] args) {
        Employee[] staff = Employee.getStaff();
        WellPaid makes52k = new WellPaid();
        System.out.println("Well-paid IT: ");
        Arrays.stream(staff).filter(makes52k).forEach(e -> System.out.println("Match: " + e.getName()));
    }
}
