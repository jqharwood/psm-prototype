package gov.nsf.research.document.service.dao.impl;

import gov.nsf.research.document.service.dao.DocumentServiceDao;
import gov.nsf.research.document.service.model.DocumentMetaData;
import gov.nsf.research.document.service.model.SectionType;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;


public class DocumentServiceDaoImpl implements  DocumentServiceDao {
	
	MongoTemplate mongoTemplate;
	GridFsTemplate gridFsTemplate;
	
	private static final String CONTENT_TYPE_PDF = "application/pdf";
	private static final String PD_COLLECTION = "proj_desc";
	private static final String DMP_COLLECTION = "data_mgnt_plan";
	
	
	public DocumentServiceDaoImpl(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate){
		this.mongoTemplate = mongoTemplate;
		this.gridFsTemplate = gridFsTemplate;
	}
	
	

	@Override
	public DocumentMetaData saveDocument(InputStream inputStream,
			String tempPropId, SectionType sectionType) {

		DBObject metaData = new BasicDBObject();
		metaData.put("_id", tempPropId);
				
		metaData.put("sectionType", sectionType.DATA_MANAGEMENT_PLAN.toString());
		GridFSFile gridFSfile = gridFsTemplate.store(inputStream, tempPropId,
				CONTENT_TYPE_PDF, metaData);
		
		return setDocmentMetaData(gridFSfile);

	}

	@Override
	public OutputStream viewDocument(String tempPropId, SectionType sectionType) {
		
		
		return null;
	}

	@Override
	public void viewEntireProposal(String tempPropId) {
		// TODO Auto-generated method stub
		
	}
	
	private DocumentMetaData setDocmentMetaData(GridFSFile gridFSfile)
	{
		DocumentMetaData documentMetaData = new DocumentMetaData();	
		documentMetaData.setContentType(gridFSfile.getContentType());
		documentMetaData.setFileName(gridFSfile.getFilename());
		documentMetaData.setLength(gridFSfile.getLength());
		documentMetaData.setUploadDate(gridFSfile.getUploadDate());
		documentMetaData.setMD5(gridFSfile.getMD5());
		
		return documentMetaData;
		
		
	}



	@Override
	public OutputStream viewDMP(String tempPropId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public OutputStream viewProjectDesc(String tempPropId) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
			
}
