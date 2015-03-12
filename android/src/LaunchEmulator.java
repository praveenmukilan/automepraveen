import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import com.migme.util.Log;


public class LaunchEmulator {
	static DefaultExecutor executor = new DefaultExecutor();
	static DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
	public static void launchEmulator(String vmName) throws ExecuteException, IOException, InterruptedException{
		
		
		Log.infoTitle("launchEmulator Starts");

		
		CommandLine launchEmul = new CommandLine("/Applications/Genymotion.app/Contents/MacOS/player");

		


		launchEmul.addArgument("--vm-name", false);
		launchEmul.addArgument(vmName);
		
		  

		executor.setExitValue(1);
		
		executor.execute(launchEmul, resultHandler);
		Thread.sleep(20000);
		
		Log.infoTitle("launchEmulator --Pass");

	}
	private static void killPlayer(String vmName) throws ExecuteException, IOException, Exception{
		Log.info("kill Player Starts");

		
//		CommandLine killNode = new CommandLine("kill -9 $(lsof -i | grep 6723 | awk '{print $2}')");
//		CommandLine killPlayer = new CommandLine("kill -9 $(lsof -i | grep 4724 | awk '{print $2}')");
		StringBuffer sb = new StringBuffer();
		sb.append("kill -9 $(ps aux | grep 'player --vm-name ");
		sb.append(vmName);
		sb.append("' | awk '{print $2}')");
		CommandLine killPlayer = new CommandLine(sb.toString());
		Log.info(sb.toString());
	//ps aux | grep 'player --vm-name SamsungGalaxyS4-4.4.4-API19' | awk '{print $2}'
		executor.setExitValue(1);
//		executor.execute(killNode,resultHandler);
		executor.execute(killPlayer,resultHandler);

		Log.info("kill Player Ends");

	}
	public static void main(String args[]){
		
		try {
			launchEmulator("SamsungGalaxyS4-4.4.4-API19");
	
			launchEmulator("GoogleNexus6-5.0.0-API21");
			
			Thread.sleep(120000);
			killPlayer("SamsungGalaxyS4-4.4.4-API19");
			killPlayer("GoogleNexus6-5.0.0-API21");
			
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
