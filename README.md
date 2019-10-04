# jSerialCommTestBed
A note from the author:
I first developed my serial interface code on RxTx, some 10 years ago. However, RxTx is no longer maintained and it doesn’t work on Windows 10 64bit with JDS 12 64bit. I was looking for a replacement and found jSerialComm to be an excellent choice. JSerialComm is simpler to use, self-contained, and works flawlessly on latest Windows and JDK 64bits. In addition, Will Hedgecock the author of jSerialComm is very attentive and immensely helpful in addressing any problem that might arises with the use of jSerialComm.

A note from jSerialComm web site: jSerialComm is a Java library designed to provide a platform-independent way to access standard serial ports without requiring external libraries, native code, or any other tools. It is meant as an alternative to RxTx and the (deprecated) Java Communications API, with increased ease-of-use, an enhanced support for timeouts, and the ability to open multiple ports simultaneously. jSerialComm home page: https://fazecast.github.io/jSerialComm/


How does this test bed works:
Developers who start out with serial port communication, may find it daunting to get all of the details right. To make it easy to get a fully working project going, I created this test bed which is a fully functional project. I made the code as minimal and simple as possible, with a GUI interface to enable easy communication with a dumb terminal to allow the developer to observe the characters transmitted and received through the port.


Setup:
--Tested System configuration: Windows 10 64bit, JDK 12 64bit, jSerialComm version 2.5.1 It was tested successfully on two different USB based serial devices: Digi Edgeport/4M, and TrippLite kespan model USA-19HS.
--Project installation: There are 4 .java files. You will also need to link into the project the jar file jSerialComm-2.5.1.jar, which can be found at: https://fazecast.github.io/jSerialComm/
The test bed was developed on Eclipse 64bit, which can be found at: https://www.eclipse.org/downloads/
--To run the test: You will need two serial ports. One for the test bed and the other for a dumb terminal. I used putty to emulate a dumb terminal. Putty can be download from: https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html
--The project uses streams, getOutputStream() & getInputStream() to write & read data, and event-listener addDataListener() to activate the reads. The program is using threads.
Running the test:

•	Once you set up the project in Eclipse do the following:
•	Linkup the two ports with a serial cable.
•	Connect putty to one of the two serial ports using the following configuration: Serial, COM-your#, 9600 baud, data bits 8, stop bits 1, parity none, flow control XON/XOFF.
•	You want to run the test bed from within Eclipse, since the program prints to the console test messages.
•	The test is GUI based. After you start the program, it scans the system for available ports and presents the ports list in a pulldown on the first screen. Select the free port, which is linked with the putty port, and click connect.
•	You should see inside the Eclipse console the messages:
o		Connecting to: COM5
o		SerialGUI: thread started...
•	If you see the above 2 messages, you have successfully connected to the port. On the next screen you should see two buttons: ‘Write to Port’ & ‘Close the Port’
•	Write to Port – clicking on this button writes out the message “Sending text to port”, you should see it echoing inside the putty dumb terminal.
•	Next type some string into the putty, you should see it echo inside the Eclipse console.
•	Close the Port – clicking this button closes the port and puts you back to the port selection screen. I included it as an example to show how to properly close a serial port.

