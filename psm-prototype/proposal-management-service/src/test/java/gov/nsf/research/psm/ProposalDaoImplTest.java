package gov.nsf.research.psm;

import gov.nsf.research.psm.dao.ProposalDao;
import gov.nsf.research.psm.model.Directorate;
import gov.nsf.research.psm.model.Division;
import gov.nsf.research.psm.model.FundingOpportunity;
import gov.nsf.research.psm.model.ProgramElement;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProposalManagementServiceApplication.class)
@WebAppConfiguration
public class ProposalDaoImplTest {

	@Autowired
	ProposalDao proposalDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testgetAllFundingOpportunities() {

		List<FundingOpportunity> FundingOpportunityList = proposalDao
				.getAllFundingOpportunities();

		for (FundingOpportunity FundingOpportunity : FundingOpportunityList) {
			System.out.println(FundingOpportunity.toString());
		}

	}

	@Test
	public void testgetAllDivisions() {

		
		List<Division> divisionList = proposalDao.getAllDivisions();

		for (Division Division : divisionList) {
			System.out.println("********************************"
					+ Division.toString());
		}

	}
	
	@Test
	public void testgetAllDirectorates() {

		
		List<Directorate> directorateList = proposalDao.getAllDirectorates();

		for (Directorate directorate : directorateList) {
			System.out.println("********************************"
					+ directorate.toString());
		}

	}
	
	@Test
	public void testgetAllProgramElements() {

		
		List<ProgramElement> programElementList = proposalDao.getAllProgramElements();

		for (ProgramElement programElement : programElementList) {
			System.out.println("********************************"
					+ programElement.toString());
		}

	}
	
	
	
	

}