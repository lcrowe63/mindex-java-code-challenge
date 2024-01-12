package com.mindex.challenge.data;

public class ReportingStructure {

	private Employee employee;
	private Integer numberOfReports;

	public ReportingStructure() {
	}

	public ReportingStructure(Employee employee) {
		this.setEmployee(employee);
		this.setNumberOfReports(Integer.valueOf(0));
	}

	public ReportingStructure(Employee employee, Integer numberOfReports) {
		this.setEmployee(employee);
		this.setNumberOfReports(numberOfReports);
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getNumberOfReports() {
		return numberOfReports;
	}

	public void setNumberOfReports(Integer numberOfReports) {
		this.numberOfReports = numberOfReports;
	}

}
