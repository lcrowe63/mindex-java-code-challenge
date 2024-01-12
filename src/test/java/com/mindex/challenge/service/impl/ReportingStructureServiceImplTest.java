package com.mindex.challenge.service.impl;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

	private String reportingStructureUrl;

	@Autowired
	private EmployeeService employeeService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		reportingStructureUrl = "http://localhost:" + port + "/reportingStructure";
	}

	@Test
	public void testRead() {

		// use employee John Lennon since he has the most complex count
		Employee employee = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");

		// Read checks
		ReportingStructure readReportingStructure = restTemplate
				.getForEntity(reportingStructureUrl + "/" + employee.getEmployeeId(), ReportingStructure.class)
				.getBody();

		assertNotNull(readReportingStructure.getEmployee());
		assertEmployeeEquivalence(readReportingStructure.getEmployee(), employee);
		assertEquals(readReportingStructure.getNumberOfReports(), 4);

		// use employee Ringo Starr since he has the basic count
		employee = employeeService.read("03aa1462-ffa9-4978-901b-7c001562cf6f");

		readReportingStructure = restTemplate
				.getForEntity(reportingStructureUrl + "/" + employee.getEmployeeId(), ReportingStructure.class)
				.getBody();

		assertNotNull(readReportingStructure.getEmployee());
		assertEmployeeEquivalence(readReportingStructure.getEmployee(), employee);
		assertEquals(readReportingStructure.getNumberOfReports(), 2);

		// use employee Pete Best since he has has no reports
		employee = employeeService.read("62c1084e-6e34-4630-93fd-9153afb65309");

		readReportingStructure = restTemplate
				.getForEntity(reportingStructureUrl + "/" + employee.getEmployeeId(), ReportingStructure.class)
				.getBody();

		assertNotNull(readReportingStructure.getEmployee());
		assertEmployeeEquivalence(readReportingStructure.getEmployee(), employee);
		assertEquals(readReportingStructure.getNumberOfReports(), 0);

	}

	private static void assertEmployeeEquivalence(Employee read, Employee sent) {
		Assert.assertEquals(read.getFirstName(), sent.getFirstName());
		Assert.assertEquals(read.getLastName(), sent.getLastName());
		Assert.assertEquals(read.getDepartment(), sent.getDepartment());
		Assert.assertEquals(read.getPosition(), sent.getPosition());
	}
}