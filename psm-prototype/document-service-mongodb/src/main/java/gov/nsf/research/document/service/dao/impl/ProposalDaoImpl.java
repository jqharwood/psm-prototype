package gov.nsf.research.document.service.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nsf.research.document.service.dao.ProposalDao;

public class ProposalDaoImpl implements ProposalDao {

	
	@Autowired
	private JdbcTemplate psmFLJdbcTemplate;
	
	
	@Override
	public String getProjectSummary(String tempPropID) {
		String sql = "select PROJ_SUMM_TXT from flp.proj_summ where TEMP_PROP_ID = ?";
		
		String projSummtext = psmFLJdbcTemplate.queryForObject(
				sql, new Object[] { tempPropID }, String.class);
		
				
		return projSummtext;
			
	}

}
