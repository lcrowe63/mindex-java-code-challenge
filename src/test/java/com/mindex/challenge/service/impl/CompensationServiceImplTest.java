package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

	private String compensationUrl;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Before
	public void setup() {
		compensationUrl = "http://localhost:" + port + "/compensation";
	}

	@Test
	public void testCompensationCreateRead() {

		Compensation testCompensation = new Compensation();
		Employee employee = new Employee();
		employee.setEmployeeId("TestEmployee");
		employee.setFirstName("Test First Name");
		employee.setLastName("Test Lastr Name");
		employee.setDepartment("Test Department");
		employee.setPosition("Test Position");
		testCompensation.setEmployee(employee);
		testCompensation.setSalary(new BigDecimal("150000"));
		testCompensation.setEffectiveDate(LocalDate.of(2024, 1, 11));

		// Create checks
		Compensation createdCompensation = restTemplate
				.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
		// compare created object to compensation object used to create the compensation
		assertCompensationEquivalence(testCompensation, createdCompensation);

		// Read checks
		Compensation readCompensation = restTemplate
				.getForEntity(compensationUrl + "/" + employee.getEmployeeId(), Compensation.class).getBody();
		// compare read compensation object to the compensation objected created
		assertCompensationEquivalence(createdCompensation, readCompensation);

	}

	private static void assertCompensationEquivalence(Compensation created, Compensation read) {
		assertEmployeeEquivalence(created.getEmployee(), read.getEmployee());
		assertEquals(created.getEffectiveDate(), read.getEffectiveDate());
		assertEquals(created.getSalary(), read.getSalary());

	}

	private static void assertEmployeeEquivalence(Employee created, Employee read) {
		assertEquals(created.getEmployeeId(), read.getEmployeeId());
		assertEquals(created.getFirstName(), read.getFirstName());
		assertEquals(created.getLastName(), read.getLastName());
		assertEquals(created.getDepartment(), read.getDepartment());
		assertEquals(created.getPosition(), read.getPosition());
	}

}