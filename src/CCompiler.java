
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


// how to use this class:
// String path = "/home/ermiya/eclipse-workspace/SaminJoon/New Folder/ref/";
// String cfile = "fips202.c";
// String output = CCompiler.compile(path, cfile);


public class CCompiler {

	static String className;
	static String command, command1, command2;

	public static String compile(String filePath, String fileName) {
		File dir = new File(filePath);
		return CCompiler.getMsg(fileName, dir);
	}

	public static String getMsg(String fileName, File Path1) {
		command = "gcc " + fileName;
		String compiled = executeCommand(command, Path1);
		String output = executeCommand("./a.out", Path1);
		if (compiled.compareTo("") == 0)
			output = "Compilation Successfull!!\n" + output;
		return output;
	}

	private static String executeCommand(String command, File Path1) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command, null, Path1);
			p.waitFor();
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader1.readLine()) != null) {
				output.append(line + "\n");
			}
			while ((line = reader2.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

}
