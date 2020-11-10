package EmployeePayrollREST;

import java.time.LocalDate;

public class EmployeePayrollData {

	int id;
	String name;
	String gender;
	double salary;
	LocalDate startDate;
	
	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.salary = salary;
		this.startDate = startDate;
	}
	

}
