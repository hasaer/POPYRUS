package printing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Finishings;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.NumberUp;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

import javafx.print.PageRange;

public class FilePrinting {
	
	public FilePrinting(String userID) throws IOException{
		//DB에서 파일 이름과 설정 가져오기
		String fileName = "miso.jpg";
		PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		
		attributes.add(OrientationRequested.PORTRAIT);
		//attributes.add(new PageRanges(1,5));
		attributes.add(new Copies(1));
		
		print(fileName, attributes);
	}
	
	  private void print(String filename, PrintRequestAttributeSet attributes)
	      throws IOException {
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
		/*
	    DocFlavor[] docFalvor = service.getSupportedDocFlavors();
        for (int i = 0; i < docFalvor.length; i++) {
            System.out.println(docFalvor[i].getMimeType());
        }
        */
	    if (service == null) {
	      System.out.println("Can't find a printer ");
	      return;
	    }
	    printToService(service, filename, attributes);
	    System.out.println("Printed " + filename + " to " + service.getName());
	  }
	
	
	  private void printToService(PrintService service, String filename,
			  PrintRequestAttributeSet attributes) throws IOException {

		  DocFlavor flavor = getFlavorFromFilename(filename);

		  InputStream in = new FileInputStream(filename);

		  Doc doc = new SimpleDoc(in, flavor, null);
	  
		  DocPrintJob job = service.createPrintJob();

		  job.addPrintJobListener(new PrintJobAdapter() { 
			  public void printJobCompleted(PrintJobEvent e) {
				  System.out.println("Print job complete");
				  System.exit(0);
			  }

			  public void printDataTransferCompleted(PrintJobEvent e) {
				  System.out.println("Document transfered to printer");
			  }

			  public void printJobRequiresAttention(PrintJobEvent e) {
				  System.out.println("Print job requires attention");
				  System.out.println("Check printer: out of paper?");
			  }

			  public void printJobFailed(PrintJobEvent e) {
				  System.out.println("Print job failed");
				  System.exit(1);
			  }
		  	});

	    try {
	      job.print(doc, attributes);
	    } catch (PrintException e) {
	      System.out.println(e);
	      System.exit(1);
	    }
	  }
	  
	  
	  private DocFlavor getFlavorFromFilename(String filename) {
	    String extension = filename.substring(filename.lastIndexOf('.') + 1);
	    extension = extension.toLowerCase();
	    if (extension.equals("gif"))
	      return DocFlavor.INPUT_STREAM.GIF;
	    else if (extension.equals("jpeg"))
	      return DocFlavor.INPUT_STREAM.JPEG;
	    else if (extension.equals("jpg"))
	      return DocFlavor.INPUT_STREAM.JPEG;
	    else if (extension.equals("png"))
	      return DocFlavor.INPUT_STREAM.PNG;
	    else if (extension.equals("ps"))
	      return DocFlavor.INPUT_STREAM.POSTSCRIPT;
	    else if (extension.equals("txt"))
	      return DocFlavor.INPUT_STREAM.TEXT_PLAIN_HOST;
	    else
	      return DocFlavor.INPUT_STREAM.AUTOSENSE;
	  }
	
	
	

}
