package LSManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class LSFrame extends JDialog{
	
Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	
	Dimension framesize = new Dimension(550,190);
	
	TextField idField;
	TextField keyField;
	
	String id;
	String key;
	
	JButton okButton;
	JButton cancelButton;
	JButton settingsButton;
	
	public LSFrame() {
		
		
		
		
		//setUndecorated(true);
		setLocation(screensize.width/2-framesize.width/2,screensize.height/2-framesize.height/2);
		setSize(framesize);
		setAlwaysOnTop(true);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//setAlwaysOnTop(true);
		JPanel mainPanel = new JPanel(new GridLayout(4,0));
		//mainPanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), ""));
		add(mainPanel);
//		
		JPanel headPanel = new JPanel();
		headPanel.setBackground(new Color(200,200,240));
		mainPanel.add(headPanel);
		
		JLabel headlb = new JLabel("Не найдена регистрационнная информация. Введите ключ активации!");
		headPanel.add(headlb);
//		
		JPanel idPanel = new JPanel();
		//headPanel.setBackground(new Color(200,200,240));
		mainPanel.add(idPanel);
		
		JLabel idlb = new JLabel("Serial: ");
		idPanel.add(idlb);
		
		Dimension fieldSize = new Dimension(450,25);
		idField = new TextField();
		idField.setPreferredSize(fieldSize);
		idField.setText(this.id);
		idField.setEditable(false);
		idPanel.add(idField);
	//	
		JPanel keyPanel = new JPanel();
		//headPanel.setBackground(new Color(200,200,240));
		mainPanel.add(keyPanel);
		
		JLabel keylb = new JLabel("Key:     ");
		keyPanel.add(keylb);
		
		//Dimension
		keyField = new TextField();
		keyField.setPreferredSize(fieldSize);
		keyPanel.add(keyField);
		
		
	//
		JPanel buttonPanel = new JPanel();
		//headPanel.setBackground(new Color(200,200,240));
		mainPanel.add(buttonPanel);
		
		cancelButton = new JButton("Отмена");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
		});
		
		okButton = new JButton("Активировать");
		buttonPanel.add(okButton);
		
		settingsButton = new JButton("Опции");
		buttonPanel.add(settingsButton);
		settingsButton.setVisible(false);
		
	}

	/**
	 * Обработчик на кнопку ОК
	 * @param al
	 */
	public void setOkListener(ActionListener al)
	{
		okButton.addActionListener(al);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				key = keyField.getText();
				
			}
		});
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		idField.setText(this.id);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		
	}
	
	
	
}
