
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;


public class JscSerialInterface implements Runnable {

	@SuppressWarnings("rawtypes")
	protected Enumeration portList;

	protected OutputStream outputStream;
	protected InputStream inStream;
	
	protected String outputBuffer = "";

	protected boolean keepReading = false;
	
	protected SerialPort jsserialPort;
	private static final Logger LOGGER = Logger.getLogger(JscSerialInterface.class.getName());


	public void closeConnection() throws IOException {
        if (jsserialPort != null) {
            try {
            	outputStream.flush();
            	outputStream.close();
                inStream.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "When flushing output before closing serial.", e);
            }
            
            // JS: close the port: closePort()
            
            jsserialPort.closePort();           
            jsserialPort = null;
        }
    }
	
	
	private void readSerial() {
		
		int rawInput = -1;
		
		try {
			while ((inStream.available() > 0) && ((rawInput = inStream.read()) != -1))
				System.out.print((char) rawInput);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void writeMessage(String message)
	{
		System.out.println("> " + message);
		
		for (int i = 0; i < message.length(); i++)
		{
			try {
					outputStream.write((int) message.charAt(i));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	}
	
	private class JSSerialPortEvent implements SerialPortDataListener {

		@Override
		public int getListeningEvents() {

			return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		}

		@Override
		public void serialEvent(com.fazecast.jSerialComm.SerialPortEvent event) {

			if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
				return;
			}
			
			readSerial();
		}
    }
	
	
	public JscSerialInterface(String comPort) {
		
		jsserialPort = SerialPort.getCommPort(comPort); 
		
		jsserialPort.setParity(SerialPort.NO_PARITY);
		jsserialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
		jsserialPort.setNumDataBits(8);
		jsserialPort.setBaudRate(9600);
		jsserialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		
		if (jsserialPort.openPort()) 
		{
			outputStream = jsserialPort.getOutputStream();
			inStream = jsserialPort.getInputStream();
			jsserialPort.addDataListener(new JSSerialPortEvent());

		} else {
		    System.out.println("Cannot open serial port. Port may be already in use.");
		    System.exit(1);
		}
	}

	@Override
	public void run() {

		
	}

}
