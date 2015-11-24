package printing;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.print.PrintException;
import javax.swing.*;

public class InputGUI extends JFrame{
		
	String userID;
	String userPW;
	
	public InputGUI(){
		super();
		
		JPanel panel = new JPanel(new FlowLayout());
		
		JPanel idPanel = new JPanel(new GridLayout(1,0,5,5));
		idPanel.add(new JLabel("PhoneNumber", SwingConstants.RIGHT));
		JTextField ID = new JTextField(10);
		idPanel.add(ID);
		panel.add(idPanel, BorderLayout.NORTH);
/*
	    JPanel pwPanel = new JPanel(new GridLayout(1,0,5,5));
	    pwPanel.add(new JLabel("Password", SwingConstants.RIGHT));
	    JPasswordField password = new JPasswordField(10);
	    pwPanel.add(password);
	    panel.add(pwPanel, BorderLayout.CENTER);
*/	    
	    JPanel buttons = new JPanel();
	    JButton okayButton = new JButton("»Æ¿Œ");
	    okayButton.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e){
	    		setUserID(ID.getText());
	    	    //setUserPW(new String(password.getPassword()));
	    	    try {
					new FilePrinting(getUserID());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	    	}
	    });
	    buttons.add(okayButton);
	    panel.add(buttons, BorderLayout.SOUTH);

	    add(panel);
	    setTitle("User Info");
	    
	    setVisible(true);
	    setSize(300,120);

	}
	
	private void setUserID(String userID){
		this.userID = userID;
	}
	
	public String getUserID(){
		return userID;
	}
	
	/*
	private void setUserPW(String userPW){
		this.userPW = userPW;
	}
	
	public String getUserPW(){
		return userPW;
	}
	 */
}
