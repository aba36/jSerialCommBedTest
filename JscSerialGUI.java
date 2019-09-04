
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class JscSerialGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel portsPanel;
	private JPanel portSelectPanel;
	private JPanel mainScreenPanel;
	private JPanel instructionsPanel;
	private JPanel portButtonPanel;
	private JscSerialInterface serialDevice;
	private JComboBox<String> ports;

	private JButton connectButton;

	private String magReadError = "Sending text to port\n";
	
	private void portWindowInit()
	{	
		portsPanel = new JPanel(new BorderLayout());
		mainScreenPanel = new JPanel(new GridLayout(1,1));
		portsPanel.add(mainScreenPanel, BorderLayout.CENTER);
		instructionsPanel = new JPanel();
		portSelectPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		mainScreenPanel.add(instructionsPanel);
		
		JLabel portLabel = new JLabel("Select Port: ");
		
		// populated the pull down Box with port list
		ports = new JComboBox<String>();
		
		JscPortScanner pScanner = new JscPortScanner();
		final Vector<String> serialPorts = pScanner.getPorts();
		
		ports.addItem("----");
		for (String pName : serialPorts)
		{
			ports.addItem(pName);
		}
		
		
		connectButton = new JButton("Connect");
		
		connectButton.setEnabled(false);
		
		// Port pull-down
		ports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				connectButton.setEnabled(false);												
				
				if (ports.getSelectedIndex() == 0)
				{
					return;
				}
				
				connectButton.setEnabled(true);	
			}
		});
		
		connectButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				if (! (ports.getSelectedIndex() > 0)) 
				{
					return;
				}

				String portName = "";

				portName = serialPorts.elementAt(ports.getSelectedIndex() - 1);									

				System.out.println("Connecting to: " + portName);
				serialDevice = new JscSerialInterface(portName);
				Thread readerThread = new Thread(serialDevice);
				readerThread.start();
				System.out.println("SerialGUI: thread started...");
				
				JButton writeToPortButton = new JButton("Write to Port");
				JButton closePortButton = new JButton("Close the Port");

				
				writeToPortButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						serialDevice.writeMessage(magReadError);
					}
				});
				
				
				closePortButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {
						 
						//JS: calling closeConnection() in class jscSerialInterface

						 try {
							serialDevice.closeConnection();
						 } catch (IOException e) {
							e.printStackTrace();
						 }
						 
						 
						 if (serialDevice.jsserialPort == null) {
							 // port was closed successfully
							 ports.setSelectedIndex(0);
							 ports.setEnabled(true);
							 instructionsPanel.remove(writeToPortButton);
							 instructionsPanel.remove(closePortButton);
							 validate();
							 repaint();
						 }
					}
				});

				
				instructionsPanel.add(writeToPortButton);
				instructionsPanel.add(closePortButton);

				connectButton.setEnabled(false);
				ports.setEnabled(false);
				validate();
				repaint();

			}

		});
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 20;
		c.ipady = 10;
		portSelectPanel.add(portLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 50;
		c.ipady = 10;
		portSelectPanel.add(ports, c);
		
				
		portButtonPanel = new JPanel();
		portButtonPanel.add(connectButton);
		portsPanel.add(portButtonPanel, BorderLayout.SOUTH);
		portsPanel.add(portSelectPanel, BorderLayout.NORTH);
		
		add(portsPanel, BorderLayout.CENTER);
	}
	
	
	public JscSerialGUI()
	{
		this.setSize(800, 600);
		this.setLayout(new BorderLayout());
		setTitle("Point of Care SIMULATOR");
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	@Override
	public void run() {
		portWindowInit();
		
		setVisible(true);

		while(this.isVisible())
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}	
}
