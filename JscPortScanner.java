
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Pattern;

import com.fazecast.jSerialComm.SerialPort;


public class JscPortScanner {
	
	private Vector<String> foundPorts = new Vector<String>();
	
	private static final Pattern[] ACCEPTABLE_PORT_NAMES = {
        /*
         * The ports we're normally going to use: USB (or Bluetooth) 
         * to serial adapters.
        */
        Pattern.compile(".*tty\\.usbserial-.+"), // Mac OS X
        Pattern.compile(".*cu\\.wchusbserial.+"), // Mac OS X
        Pattern.compile(".*tty\\.usbmodem.+"), // Mac OS X
        Pattern.compile(".*ttyACM\\d+"), // Raspberry Pi
        Pattern.compile(".*ttyUSB\\d+"), // Linux
        Pattern.compile(".*rfcomm\\d+"), // Linux Bluetooth
        Pattern.compile(".*COM\\d+") // Windows
    };
	
	
	public Vector<String> getPorts()
	{
		return foundPorts;
	}
	
	public JscPortScanner()
	{
		SerialPort[] portList;		
		
		portList = SerialPort.getCommPorts();
		

		for (SerialPort myPort : portList)
		{
			String name = myPort.getSystemPortName();
			
			for (Pattern acceptablePortName : ACCEPTABLE_PORT_NAMES) {
                if (acceptablePortName.matcher(name).matches()) {
                	// found port of interest
                    foundPorts.add(name);
                    break;
                }
            }
			
		}
		
		// sort the collection in ascending order
		Collections.sort(foundPorts);		
	}
}
