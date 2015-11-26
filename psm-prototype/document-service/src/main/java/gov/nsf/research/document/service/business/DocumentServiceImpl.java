package gov.nsf.research.document.service.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nsf.research.document.service.dao.DocumentServiceDao;
import gov.nsf.research.document.service.model.DocumentMetaData;
import gov.nsf.research.document.service.model.SectionType;
import gov.nsf.research.document.service.pdf.PDFUtility;
import gov.nsf.research.document.service.repository.ProjectDescRepository;

public class DocumentServiceImpl implements DocumentService {

	@Autowired
	DocumentServiceDao docServiceDao;
	
	@Autowired
	ProjectDescRepository pdr;
	
	@Override
	public DocumentMetaData uploadPropSection(ByteArrayInputStream inputStream, String tempPropId, SectionType sectionType) {
		System.out.println("saving doc on service layer.");
		return docServiceDao.saveDocument(inputStream, tempPropId, sectionType);
	}

	@Override
	public ByteArrayOutputStream getPropSection(String tempPropId, SectionType sectionType) {
		return docServiceDao.viewDocument(tempPropId, sectionType);
	}

	@Override
	public ByteArrayOutputStream getEntirePropSection(String tempPropId) {
		List<ByteArrayOutputStream> baosList = new ArrayList<ByteArrayOutputStream>();
		for(int i=0; i<2; i++){
			baosList.add(docServiceDao.viewDocument(tempPropId, SectionType.PROJECT_DESCRIPTION));
		}
		ByteArrayOutputStream baos = (ByteArrayOutputStream)PDFUtility.concatenateDocuments(baosList);
		return baos;
	}

	
	
}
