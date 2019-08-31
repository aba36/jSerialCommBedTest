
public class jscSimulator {
		
	public static void main(String[] args)
	{	
		jscSerialGUI pocGUI = new jscSerialGUI();
		Thread guiThread = new Thread(pocGUI, "gui1");
		
		guiThread.start();
			
	}
}
