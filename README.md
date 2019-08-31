# jSerialCommTestBed
A test bed for the java serial port library jSerialcomm, to troubleshoot the hang problem when calling port.closePort()


Note:
jSerialComm is a Java library designed to provide a platform-independent way to access standard serial ports without requiring external libraries, native code, or any other tools. It is meant as an alternative to RxTx and the (deprecated) Java Communications API, with increased ease-of-use, an enhanced support for timeouts, and the ability to open multiple ports simultaneously.
jSerialComm home page: https://fazecast.github.io/jSerialComm/ 


--Problem description:
port.closePort() call hangs the application

After I ported my software from RXTX to jSerialComm, everything worked just fine except for the port.closePort() call. When closePort() is called it hangs the application. However, the hang occurs only after the port reads data. If all we do is write, the closePort() works fine.

--Tested System configuration:
Lenove ThinkPad laptop, Windows 10 64bit, JDK 12 64bit, jSerialComm version 2.5.1
It was tested on two different USB based serial devices: Digi Edgeport/4M, and TrippLite kespan model USA-19HS, with identical behavior on both.

--Installation:
There are 4 .java files. You will also need to link into the project the jar file jSerialComm-2.5.1.jar, which can be found at: https://fazecast.github.io/jSerialComm/

The test bed was developed on Eclipse 64bit, which can be found at: https://www.eclipse.org/downloads/

--Running the test:
You will need two serial ports. One for the test bed and the other for a dumb terminal. To emulate a dumb terminal I used putty. Putty can be download from: https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html

Note:
getOutputStream() & getInputStream() are used to send/receive the data, and addDataListener() to activate the reads. The program is using threads.

* Linkup the two ports with a serial cable.
* Connect putty to one of the two serial ports using the following configuration: Serial, COM-your#, 9600 baud, data bits 8, stop bits 1, parity none, flow control XON/XOFF (I tried with flow control none but it made no difference).
* You want to run the test bed from within Eclipse, since the program prints to the console test messages.
* The test is GUI based. After you start the program, it scans the system port for available ports and presents the ports list in a pulldown on the first screen. Select the free port, which is linked with the putty port, and click connect.
* On the second screen you will see 2 buttons: 
    “Write data to port” – clicking this button will send text through the port which should echo in the putty terminal. Do this step to verify that the ports are connected correctly.
    "Close the port hangs" - this button closes the port, but don't click it yet. Go to the next step.
* Next type some text into the putty terminal. You should see the text echoed in the Eclipse console window.
* "Close the port hangs" – this is the 2nd button which closes the port. Click on the button, which will freeze/hang the program. At this point the only way to exit the program is to exit Eclipse. Note that the program will hang only after the port received data. 

closePort() – is located in class jscSerialInterface.java method closeConnection(). The method is call from class jscSerialGUI.java. Also look for //JS: comments, I tried to provide brief descriptions there.

