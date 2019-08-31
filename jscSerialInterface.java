
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;


public class jscSerialInterface implements Runnable {

	@SuppressWarnings("rawtypes")
	protected Enumeration portList;

	protected OutputStream outputStream;
	protected InputStream inStream;
	
	protected String outputBuffer = "";

	protected boolean keepReading = false;
	
	protected SerialPort jsserialPort;
	private static final Logger LOGGER = Logger.getLogger(jscSerialInterface.class.getName());


	public void closeConnection() throws IOException {
        if (jsserialPort != null) {
            try {
            	outputStream.flush();
            	outputStream.close();
                inStream.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "When flushing output before closing serial.", e);
            }
            
            // JS: program hangs on closePort()
            System.out.println("BEGIN: System hangs here on: port.closePort() - inside class jscSerialInterface");
            jsserialPort.closePort();
            System.out.println("END: port.closePort() - this message we don't see because of the hang");
            
            jsserialPort = null;
        }
    }
	
	
	private void readSerial() {
		
		BufferedReader inBuf;
		inBuf = new BufferedReader(new InputStreamReader(inStream), 1);
		
		try {
			parseInput(inBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void parseInput(BufferedReader inBuf) throws IOException {
		
		int rawInput = -1;
				
		while (inStream.available() > 0) 
		{			
			while ((rawInput = inBuf.read()) != -1)
			{	
				System.out.print((char) rawInput);
			}
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
	
	
	public jscSerialInterface(String comPort) {
		
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

		keepReading = true;

		while (keepReading) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}			
		}
		
System.out.println("run: System.exit(1)...");
		System.exit(1);
		
	}

}
