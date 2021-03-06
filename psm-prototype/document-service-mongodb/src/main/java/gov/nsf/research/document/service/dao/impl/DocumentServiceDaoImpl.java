package gov.nsf.research.document.service.dao.impl;

import gov.nsf.research.document.service.dao.DocumentServiceDao;
import gov.nsf.research.document.service.model.Document;
import gov.nsf.research.document.service.model.DocumentMetaData;
import gov.nsf.research.document.service.model.SectionType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;


public class DocumentServiceDaoImpl implements  DocumentServiceDao {
	
	MongoTemplate mongoTemplate;
	GridFsTemplate gridFsTemplate;
	
	private static final String CONTENT_TYPE_PDF = "application/pdf";
	private static final String SECTION_TYPE = "sectionType";
	private static final String FILENAME = "filename";
	private static final String META_DATA_SECTION_TYPE = "metadata.sectionType";
	private static final String FILE_LENGTH = "length";
	
	
	
	public DocumentServiceDaoImpl(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate){
		this.mongoTemplate = mongoTemplate;
		this.gridFsTemplate = gridFsTemplate;
	}

	@Override
	public DocumentMetaData saveDocument(InputStream inputStream,
			String tempPropId, SectionType sectionType) {
		
		if (isDocumentExists(tempPropId, sectionType)) {
			deleteDocument(tempPropId, sectionType);
		}

		DBObject metaData = new BasicDBObject();
		metaData.put(SECTION_TYPE, sectionType.toString());
		GridFSFile gridFSfile = gridFsTemplate.store(inputStream, tempPropId,
				CONTENT_TYPE_PDF, metaData);
		//System.out.println("Document saved in mongoDB.");
		return setDocmentMetaData(gridFSfile);

	}

	@Override
	public ByteArrayOutputStream viewDocument(String tempPropId, SectionType sectionType) {
		Query query = new Query().addCriteria(Criteria.where(FILENAME).is(tempPropId).and(META_DATA_SECTION_TYPE).is(sectionType).and(FILE_LENGTH).gt(0));
		List<GridFSDBFile> fileList = gridFsTemplate.find(query);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		for(GridFSDBFile file : fileList){
			try {			
					file.writeTo(outputStream);
					//System.out.println("Output Stream: " + outputStream != null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return outputStream;
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
	public List<Document> viewAllFilesFromDB() {
		
		List<SectionType> listOfDocs = new ArrayList<SectionType>();

		listOfDocs.add(SectionType.DATA_MANAGEMENT_PLAN);
		listOfDocs.add(SectionType.PROJECT_DESCRIPTION);
		listOfDocs.add(SectionType.CURR_PEND_SUPPORT);
		listOfDocs.add(SectionType.BIO_SKETCHES);
		listOfDocs.add(SectionType.MENTOR_PLAN);

		Query query = new Query().addCriteria(Criteria.where(
				META_DATA_SECTION_TYPE).in(listOfDocs));

		List<GridFSDBFile> fileList = gridFsTemplate.find(query);

		Document doc = null;
		List<Document> list = new ArrayList<Document>();

		for (GridFSDBFile file : fileList) {

			doc = new Document(file.getFilename(), SectionType.valueOf(file
					.getMetaData().get(SECTION_TYPE).toString()));
			list.add(doc);

		}
		return list;
	}

	@Override
	public void deleteDocument(String tempPropId, SectionType sectionType) {
		Query query = new Query().addCriteria(Criteria.where(FILENAME).is(tempPropId).and(META_DATA_SECTION_TYPE).is(sectionType));
			
		gridFsTemplate.delete(query);
	}

	@Override
	public boolean isDocumentExists(String tempPropId, SectionType sectionType) {
		
		boolean status = false;
		Query query = new Query().addCriteria(Criteria.where(FILENAME)
				.is(tempPropId).and(META_DATA_SECTION_TYPE).is(sectionType));
		//List<GridFSDBFile> fileList = gridFsTemplate.find(query);
		List<GridFSDBFile> fileList = gridFsTemplate.find(query);

		if (fileList.size() > 0) {
			status = true;
		}
		
		return status;
	}	
	
	
}
