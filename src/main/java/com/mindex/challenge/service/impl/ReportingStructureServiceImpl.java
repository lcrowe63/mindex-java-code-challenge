package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
	private EmployeeRepository employeeRepository;

	public ReportingStructureServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public ReportingStructure findReportingStructure(String id) {
		return new ReportingStructure(employeeRepository.findByEmployeeId(id), findNumberOfReports(id));
	}

	// Count direct reports and indirect reports verifying that the employee is a
	// valid employee and that each employee is only counted once assuming that an
	// employee should only report to one person
	private int findNumberOfReports(String id) {

		List<Employee> employeeList = new ArrayList<>();
		Set<String> employeeIdSet = new HashSet<>();

		Employee requestedEmployee = employeeRepository.findByEmployeeId(id);
		if (requestedEmployee.getDirectReports() != null) {
			employeeList.addAll(requestedEmployee.getDirectReports());
		}

		// if no direct reports are found the list is empty and the count is zero
		while (!employeeList.isEmpty()) {

			// take the first employee from the list
			Employee employee = employeeList.remove(0);

			// read the employee to get it's direct reports if it's not found it's not a
			// valid employee and should not be included in the count
			boolean isEmployeeValid = employeeRepository.findByEmployeeId(employee.getEmployeeId()) != null;

			// if the employee has already been counted it's probably a data error and
			// should not be counted again
			boolean isEmployeeAlreadyFound = employeeIdSet.contains(employee.getEmployeeId());

			// if employee is valid and not already found
			if (isEmployeeValid && !isEmployeeAlreadyFound) {

				// add employee to the set of employees to count
				employeeIdSet.add(employee.getEmployeeId());

				// get this employees Direct reports
				List<Employee> indirectReports = employeeRepository.findByEmployeeId(employee.getEmployeeId())
						.getDirectReports();

				// if this employee has direct report add them to the employee list so they can
				// be checked in the processing loop
				if (indirectReports != null) {
					employeeList.addAll(indirectReports);
				}
			}
		}
		LOG.debug("Reporting employee count [{}]", employeeIdSet.size());
		return employeeIdSet.size();
	}
}