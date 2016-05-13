package gov.nsf.research.document.service.pdf.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import gov.nsf.research.document.service.model.SectionType;
import gov.nsf.research.document.service.pdf.PDFService;
import gov.nsf.research.psm.proposal.transfer.proposals.GetProposalResponse;
import gov.nsf.research.psm.proposal.webservice.client.ProposalDataServiceClient;

public class ITextPDFServiceImpl implements PDFService {

	@Autowired
	public ProposalDataServiceClient proposalDataServiceClient;
	
	
	/**
	 * 
	 */
	@Override
	public ByteArrayOutputStream createPDF(SectionType sectionType,
			String tempPropId) {
		System.out.println("ITextPDFServiceImpl.createPDF()");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (SectionType.PROJ_SUMM.equals(sectionType)) {
			
			baos = generateProjectSummaryPDF(tempPropId);
			
		} else if (SectionType.REF_CITED.equals(sectionType)) {

			baos = generateRefCitedPDF(tempPropId);
		}

		return baos;
	}
	
	

	@Override
	public ByteArrayOutputStream CreateEntireProposalPDF(String tempPropId) {

		System.out.println("ITextPDFServiceImpl.CreateEntireProposalPDF()");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		List<ByteArrayOutputStream> baosList = new ArrayList<ByteArrayOutputStream>();
		
		//Project Summary
		ByteArrayOutputStream projsumm = createPDF(SectionType.PROJ_SUMM, tempPropId);
		projsumm = stampPDF(projsumm, SectionType.PROJ_SUMM, "Supplement");
		
		//Bio Sketches
		ByteArrayOutputStream refcited = createPDF(SectionType.REF_CITED,	tempPropId);
		String stampText ="PI Transfer/Award No:1100423/Submitted on:"+new SimpleDateFormat("MMMM dd yyyy hh:mm a").format(new Date())+" /Electronic Signature";
		refcited = stampPDF(refcited, SectionType.REF_CITED,stampText);
		
		//add Proposal Sections to the list
		baosList.add(projsumm);
		baosList.add(refcited);

		//concatente PDF
		baos = concatentePDF(baosList);

		return baos;

	}

	

	

	private ByteArrayOutputStream generateProjectSummaryPDF(String tempPropId) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		GetProposalResponse proposal = proposalDataServiceClient.getProposal(tempPropId);
		
		String filepath = proposal.getProposal().getProposalSections().getProjectSummary().getFilePath();
		
		//If file path is not empty , then read PDF from NFS mount
		if (filepath != null) {		
			filepath = "C:/Users/spendyal/Desktop/psm/Psummary.pdf";
			outputStream = getPdfFromNfsMount(filepath);	
			
			return outputStream;
		}

		//Read the text from database and generate PDF
		// Project Summary template
		Document document = new Document(PageSize.A4, 72, 72, 72, 72);

		Paragraph overViewParagraph = null;
		Paragraph brodrImptParagraph = null;
		Paragraph intulMeritParagraph = null;
		Paragraph mainHeader = null;

		try {
			PdfWriter.getInstance(document, outputStream);

			document.open();

			// Proposal section Title format
			mainHeader = new Paragraph("Project Summary", getTitleFont());
			mainHeader.setAlignment(Element.ALIGN_CENTER);
			document.add(mainHeader);

			// Overview Paragraph set up
			overViewParagraph = new Paragraph("Overview :", getSectioinFont());
			document.add(overViewParagraph);
			document.add(new Paragraph(proposal.getProposal()
					.getProposalSections().getProjectSummary()
					.getProjSummaryText()));

			// BrodrImpt Paragraph set up
			brodrImptParagraph = new Paragraph("Broader Impacts :",
					getSectioinFont());
			document.add(brodrImptParagraph);
			document.add(new Paragraph(proposal.getProposal()
					.getProposalSections().getProjectSummary()
					.getBroaderImpacts()));

			// IntulMerit Paragraph set up
			intulMeritParagraph = new Paragraph("Intellectual Merit :",
					getSectioinFont());
			document.add(intulMeritParagraph);
			document.add(new Paragraph(proposal.getProposal()
					.getProposalSections().getProjectSummary()
					.getIntellectualMerit()));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return outputStream;

	}

	private ByteArrayOutputStream generateRefCitedPDF(String tempPropId) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GetProposalResponse proposal = proposalDataServiceClient
				.getProposal(tempPropId);
		
		String filepath = null;
		
		filepath =  proposal.getProposal()	.getProposalSections().getReferencesCited().getFilePath();
		
	
		//If file path is not empty , then read PDF from NFS mount
		if (filepath != null) {	
			filepath = "C:/Users/spendyal/Desktop/psm/biosk.pdf";
			outputStream = getPdfFromNfsMount(filepath);	
			
			return outputStream;
		}

		//Read the text from database and generate PDF
		// Reference Cited template		
		

		// page size
		Document document = new Document(PageSize.A4, 72, 72, 72, 72);

		Paragraph mainHeader = null;

		try {
			PdfWriter.getInstance(document, outputStream);

			document.open();

			// Proposal Section Title format
			mainHeader = new Paragraph("REFERENCES CITED", getTitleFont());
			mainHeader.setAlignment(Element.ALIGN_CENTER);
			document.add(mainHeader);

			
			document.add(new Paragraph(proposal.getProposal()	.getProposalSections().getReferencesCited().getRefCitedTxt(), getTextFont()));
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return outputStream;

	}

	
	
	@Override
	public ByteArrayOutputStream stampPDF(ByteArrayOutputStream srcDocStream,SectionType sectionType,String stampText) {
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(srcDocStream.toByteArray());
			int n = reader.getNumberOfPages();
			System.out.println("ITextPDFServiceImpl.stampPDF() No of Pages : "+n);
			stamper = new PdfStamper(reader, srcDocStream);

			if(SectionType.PROJ_SUMM.equals(sectionType)){
				Rectangle rect = reader.getPageSize(1);
				PdfContentByte canvas = stamper.getOverContent(1);
				Phrase stampPhrase = new Phrase(stampText, new Font(FontFamily.HELVETICA, 12, 0, BaseColor.BLUE));
				ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, stampPhrase,rect.getRight(305), rect.getTop(60), 0);
			}else{
				for(int i=1; i <= n;i++){
    				Rectangle rect = reader.getPageSize(i);
					PdfContentByte canvas = stamper.getOverContent(i);
					Phrase stampPhrase = new Phrase(stampText, new Font(FontFamily.HELVETICA, 12, 0, BaseColor.RED));
					ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, stampPhrase,rect.getLeft(35), rect.getBottom(30), 0);	
				}
			}

			stamper.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		if(SectionType.PROJ_SUMM.equals(sectionType)){
//			srcDocStream = stampOnSinglePage(srcDocStream, stampText);
//		}else{
//			srcDocStream = stampOnMultiplePages(srcDocStream, stampText);
//		}

		return srcDocStream;

	}
	
	private ByteArrayOutputStream getPdfFromNfsMount( String filepath) {
		
	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				File file = new File(filepath);

				byte b[] = FileCopyUtils.copyToByteArray(file);

				outputStream = new ByteArrayOutputStream(b.length);

				outputStream.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return outputStream;
		}
	
	

	@Override
	public ByteArrayOutputStream stampPDF(ByteArrayOutputStream srcDocStream, SectionType sectionType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private ByteArrayOutputStream concatentePDF( List<ByteArrayOutputStream> baosList) {
		System.out.println("ITextPDFServiceImpl.CreateEntireProposalInternal()");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Document document = new Document();

		PdfWriter writer = null;
		PdfReader reader = null;
		try {
			writer = PdfWriter.getInstance(document, baos);

			document.open();
			PdfContentByte cb = writer.getDirectContent();
			for (ByteArrayOutputStream ba : baosList) {

				reader = new PdfReader(ba.toByteArray());

				for (int i = 1; i <= reader.getNumberOfPages(); i++) {
					document.newPage();
					PdfImportedPage page = writer.getImportedPage(reader, i);
					cb.addTemplate(page, 0, 0);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		document.close();

		return baos;
	}
	
	
	private Font getTitleFont() {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
		return font;
	}

	private Font getSectioinFont() {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
		return font;
	}
	
	private Font getTextFont() {
		Font font = new Font(Font.FontFamily.TIMES_ROMAN, 11);
		return font;
	}
	
	

}
