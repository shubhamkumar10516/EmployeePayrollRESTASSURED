package EmployeePayrollREST;

import java.util.*;
public class EmployeePayrollService {
	public enum IOService {
		REST_IO
	}

    List<EmployeePayrollData> employeePayrollList;   
	
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		//this.employeePayrollList = employeePayrollList;
		this.employeePayrollList = new ArrayList<>(employeePayrollList);
	}

	public EmployeePayrollService() {
		
	}

	public long countEntries(IOService ioService) {
		return employeePayrollList.size();
	}

	public void addEmployeeToPayroll(EmployeePayrollData employeePayrollData, IOService restIo) {
		employeePayrollList.add(employeePayrollData);
	}

	public void updateEmpSalary(String name, double salary, IOService restIo) {
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData != null)
			 employeePayrollData.salary = salary;
	}

	public EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream().filter(employeeData -> employeeData.name.equals(name)).findFirst()
				.orElse(null);	}
}
