package printing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.JOptionPane;

public class FilePrinting {
	
	public FilePrinting(String userID) throws IOException{
		
		SocketClient socketClient = new SocketClient();
		String[] fileAttributes = socketClient.getData(userID);
		
		if(fileAttributes[0]==null){
			JOptionPane.showMessageDialog(null, "존재하지 않는 ID입니다.");
		}else{
			
			String fileName = fileAttributes[1];
			int attrCopies = Integer.parseInt(fileAttributes[2]); 
			int attrOt = Integer.parseInt(fileAttributes[3]);//가로 :1 세로:2
			int attrRange = Integer.parseInt(fileAttributes[4]);//1:all, 2:range 3:now
			
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			
			attributes.add(new Copies(attrCopies));
			
			if(attrOt==1){
				attributes.add(OrientationRequested.LANDSCAPE);
			}else{
				attributes.add(OrientationRequested.PORTRAIT);
			}
			
			if(attrRange==2){
				int attrMin = Integer.parseInt(fileAttributes[5]);
				int attrMax = Integer.parseInt(fileAttributes[6]);
				attributes.add(new PageRanges(attrMin,attrMax));
			}

			print(fileName, attributes);
			System.exit(0);	
		}
	}
	
	@SuppressWarnings("unused")
	private void print(String filename, PrintRequestAttributeSet attributes)
	      throws IOException {
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
		
	    DocFlavor[] docFalvor = service.getSupportedDocFlavors();
       /*
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

		  InputStream in = new FileInputStream(new File(filename));

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
