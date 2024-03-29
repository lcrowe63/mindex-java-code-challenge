package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	@Autowired
	private CompensationRepository compensationRepository;

	@Override
	public Compensation create(Compensation compensation) {
		LOG.debug("Creating compensation [{}]", compensation);

		compensationRepository.insert(compensation);

		return compensation;
	}

	@Override
	public Compensation readByEmployeeId(String employeeId) {
		LOG.debug("Finding compensation for employee with id [{}]", employeeId);

		Compensation compensation = compensationRepository.readByEmployeeEmployeeId(employeeId);

		if (compensation == null) {
			throw new RuntimeException("Invalid employeeId: " + employeeId);
		}

		return compensation;
	}
}