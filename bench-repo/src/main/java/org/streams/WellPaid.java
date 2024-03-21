package org.streams;

import java.util.function.Predicate;

public class WellPaid implements Predicate<Employee> {
    public boolean test(Employee e) {
        String d = e.getDepartment().toLowerCase();
        return d.equals("it") && e.getSalary() > 52000;
    }
}
