package com.company.ch2.objectStream;

import java.io.*;

public class ObjectStreamTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Employee harry = new Employee("Harry", 50000, 1989, 10, 1);
        Manager carl = new Manager("Carl", 80000, 1987, 12, 15);
        Manager tony = new Manager("Tony", 40000, 1990, 3, 15);
        carl.setSecretary(harry);
        tony.setSecretary(harry);

        Employee[] staff = new Employee[3];
        staff[0] = carl;
        staff[1] = harry;
        staff[2] = tony;

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee3.dat"))) {
            out.writeObject(staff);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee3.dat"))) {
            Employee[] newStaff = (Employee[])in.readObject();
            newStaff[1].raiseSalary(10);
            for (Employee e : newStaff) {
                System.out.println(e);
            }
        }
    }
}
