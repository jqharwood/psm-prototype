package gov.nsf.research.document.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.pdf.PdfReader;

import gov.nsf.research.document.service.model.EditorText;
import gov.nsf.research.document.service.model.PDFDocument;
import gov.nsf.research.document.service.model.SectionType;
import gov.nsf.research.document.service.model.proposal.ProjectSummary;

public class PDFServicePdfBoxImpl implements PDFService {

	@Override
	public PDFDocument validatePDFDocument(ByteArrayInputStream inputStream,
			String tempPropId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream createPDF(ProjectSummary ps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream CreateEntireProposal(String tempPropId,
			List<ByteArrayOutputStream> outputStreamList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream CreateEntireProposal(Map<String, PdfReader> filesToMerge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream CreateEntireProposalWithBookMarks(
			Map<SectionType, ByteArrayOutputStream> filesToMerge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteArrayOutputStream stampPDF(ByteArrayOutputStream srcDocStream, String sampText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int pageCount(ByteArrayOutputStream baos) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ByteArrayOutputStream createProjectSummaryPDF(
			Set<EditorText> overView, Set<EditorText> brodImpt,
			Set<EditorText> intellectual) {
		// TODO Auto-generated method stub
		return null;
	}

	



}
