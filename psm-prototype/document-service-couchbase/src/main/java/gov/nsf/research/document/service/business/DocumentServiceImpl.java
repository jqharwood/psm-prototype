package gov.nsf.research.document.service.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nsf.research.document.service.dao.DocumentServiceDao;
import gov.nsf.research.document.service.dao.MetaDataServiceDao;
import gov.nsf.research.document.service.model.DocumentMetaData;
import gov.nsf.research.document.service.model.SectionType;
import gov.nsf.research.document.service.pdf.DocServiceUtility;
import gov.nsf.research.document.service.pdf.PDFUtility;

public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentServiceDao docServiceDao;
	
	@Autowired
	MetaDataServiceDao metadataServiceDao;
	
	
	@Override
	public DocumentMetaData uploadPropSection(ByteArrayInputStream inputStream, String tempPropId, SectionType sectionType) {		
		return docServiceDao.saveDocument(inputStream, tempPropId, sectionType);
	}

	@Override
	public ByteArrayOutputStream getPropSection(String tempPropId, SectionType sectionType) {
		return docServiceDao.viewDocument(tempPropId, sectionType, 0);
	}

	@Override
	public ByteArrayOutputStream getEntirePropSection(String tempPropId) {
		List<ByteArrayOutputStream> baosList = new ArrayList<ByteArrayOutputStream>();
				
		baosList.add(docServiceDao.viewDocument(tempPropId, SectionType.PROJECT_DESCRIPTION, 0));
		baosList.add(docServiceDao.viewDocument(tempPropId, SectionType.DATA_MANAGEMENT_PLAN, 0));
		
		ByteArrayOutputStream baos = (ByteArrayOutputStream)PDFUtility.concatenateDocuments(baosList);
		
		return baos;
	}

	@Override
	public void deletePropSection(String tempPropId, SectionType sectionType) {
		docServiceDao.deleteDocument(tempPropId, sectionType, 0);
	}

	@Override
	public DocumentMetaData getSectionMetaData(String tempPropId, SectionType sectionType) {
		return metadataServiceDao.viewDocumentMetaData(DocServiceUtility.getKey(tempPropId, sectionType));
	}
	
	
}
