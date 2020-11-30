package EmployeePayrollREST;
import org.junit.Test;
import com.google.gson.Gson;
import EmployeePayrollREST.EmployeePayrollService.IOService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Before;

public class EmployeePayrollRESTTest {
    @Before
    public void setUp() {
    	RestAssured.baseURI = "http://localhost";
    	RestAssured.port = 3000;
    }
    //  retrieving  total number of entries 
    @Test
    public void onRetrieveShouldReturnCount() {
    	EmployeePayrollData[] arrOfEmp = getListOfEmployee();
    	EmployeePayrollService employeePayrollService;
    	employeePayrollService = new EmployeePayrollService(Arrays.asList(arrOfEmp));
    	long entries = employeePayrollService.countEntries(IOService.REST_IO);
    	System.out.println("------"+entries);
    	assertEquals(7, entries);
    }
    
    @Test 
    public void newEmployeeAddedToJsonReturnUpdatedCount() {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrOfEmps = getListOfEmployee();
    	employeePayrollService = new EmployeePayrollService(Arrays.asList(arrOfEmps));
    	EmployeePayrollData employeePayrollData = new EmployeePayrollData(0,"Mark","M",3000000.0, LocalDate.now());
    	Response response = addEmployeeToJsonServer(employeePayrollData);
    	int statusCode = response.getStatusCode();
    	assertEquals(201, statusCode);
    	employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
    	employeePayrollService.addEmployeeToPayroll(employeePayrollData, IOService.REST_IO);
    	long entries =  employeePayrollService.countEntries(IOService.REST_IO);
    	assertEquals(7, entries);
    }

    @Test
    public void addingListOfEmployeeAndChecking201ResponseAndCount() {
     	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrOfEmps = getListOfEmployee();
    	employeePayrollService = new EmployeePayrollService(Arrays.asList(arrOfEmps));
       	EmployeePayrollData[] arrOfEmpPayrolls = {
    		new EmployeePayrollData(0, "Sunder", "M", 100000, LocalDate.now()),
    		new EmployeePayrollData(0, "Mukesh", "M", 300000, LocalDate.now()),
    		new EmployeePayrollData(0, "Anil", "M", 400000, LocalDate.now())
    	}; 
    	
    	for(EmployeePayrollData employeePayrollData : arrOfEmpPayrolls ) {
    		Response response = addEmployeeToJsonServer(employeePayrollData);
    		int statusCode  = response.getStatusCode();
    		assertEquals(201, statusCode);
    		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
    		employeePayrollService.addEmployeeToPayroll(employeePayrollData, IOService.REST_IO);
    	}
    	long entries = employeePayrollService.countEntries(IOService.REST_IO);
    	//assertEquals(10, entries);
    }

    @Test
    public void updateNewSalaryShouldMatch200Response() {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrOfEmps = getListOfEmployee();
    	employeePayrollService = new EmployeePayrollService(Arrays.asList(arrOfEmps));
    	employeePayrollService.updateEmpSalary("Anil",1000.0,IOService.REST_IO);
    	EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");
    	String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(empJson);
		Response response =requestSpecification.put("/employees/"+employeePayrollData.id);
		int statusCode = response.getStatusCode();
		assertEquals(200,  statusCode);
    }
    
    @Test
    public void deleteEmployeeShouldMatch200Response() {
    	EmployeePayrollService employeePayrollService;
    	EmployeePayrollData[] arrOfEmps = getListOfEmployee();
    	employeePayrollService = new EmployeePayrollService(Arrays.asList(arrOfEmps));
    	EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Anil");
    	RequestSpecification requestSpecification = RestAssured.given();
	requestSpecification.header("Content-Type", "application/json");
		Response response =requestSpecification.delete("/employees/"+employeePayrollData.id);
		int statusCode = response.getStatusCode();
		assertEquals(200,  statusCode);
	 }
    
    
	// methods
    private Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(empJson);
		return requestSpecification.post("/employees");
    }

	private EmployeePayrollData[] getListOfEmployee() {
		Response response = RestAssured.get("/employees");
		System.out.println("Employee payroll data: "+ response.asString());
		Gson gson = new Gson();
		EmployeePayrollData[] arrOfEmp = gson.fromJson(response.asString(), EmployeePayrollData[].class);
		return arrOfEmp;
	}
}
