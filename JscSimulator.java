
public class JscSimulator {
		
	public static void main(String[] args)
	{	
		JscSerialGUI pocGUI = new JscSerialGUI();
		Thread guiThread = new Thread(pocGUI, "gui1");
		
		guiThread.start();
			
	}
}
