package printing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.JobAttributes;
import java.awt.JobAttributes.DefaultSelectionType;
import java.awt.PageAttributes;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class FilePrinting implements Printable{
	
	static AttributedString myStyledText = null;
	
	public FilePrinting() {
	}
	
	public FilePrinting(String userID, String userPW){
		String fileName = "test.txt";
		//fileName = ID와 비밀번호를 이용하여 DB에서 filename을 읽어옴.
		String mText = readContent(fileName);
		myStyledText = new AttributedString(mText);
	    printToPrinter();
	}	

	public String readContent(String fileName){
		String printingContent = "";
		
		try{
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String line = "";
			while( (line = input.readLine()) != null){
				printingContent += line +"\n";
			}
		}catch(Exception e){
			return printingContent;
		}
		
		return printingContent;
	}
	
    public void printToPrinter() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PageFormat pageFormat = new PageFormat();

        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
       /*
        //인쇄 부수 설정
        JobAttributes jobAttributes = new JobAttributes();
        jobAttributes.setCopies(1);

        //인쇄범위설정
        // DefaultSelectionType.ALL:모두인쇄
        // DefaultSelectionType.Range : 범위지정
        // DefaultSelectionType.SELECTION : 선택영역 
        jobAttributes.setDefaultSelection(DefaultSelectionType.ALL);
        if(jobAttributes.getDefaultSelection().equals(DefaultSelectionType.RANGE)){
        	jobAttributes.setMaxPage(5);
        	jobAttributes.setMinPage(1);
        }
        
        //페이지 방향 설정(가로 : LANDSCAPE / 세로 : PORTRAIT )
        PageAttributes pageAttributes = new PageAttributes();
        pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
        
        
        */
        Book book = new Book();
        
        book.append(new FilePrinting(), new PageFormat());
        
        printerJob.setPageable(book);
        
        try {
        	printerJob.print();
           
        }  catch (PrinterException ex) {
            System.err.println("Error occurred while trying to Print: " + ex);
        }
        
    
    }
   
    public int print(Graphics g, PageFormat format, int pageIndex) {
        Graphics2D graphics2d = (Graphics2D) g;
 
        graphics2d.translate(format.getImageableX(), format.getImageableY());
 
        graphics2d.setPaint(Color.black);
 
        Point2D.Float pen = new Point2D.Float();
        AttributedCharacterIterator charIterator = myStyledText.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(charIterator, graphics2d.getFontRenderContext());
        float wrappingWidth = (float)format.getImageableWidth();
        while (measurer.getPosition() < charIterator.getEndIndex()) {
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            pen.y += layout.getAscent();
            float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout.getAdvance());
            layout.draw(graphics2d, pen.x + dx, pen.y);
            pen.y += layout.getDescent() + layout.getLeading();           
        }
        return PAGE_EXISTS;
    }

}
