
import java.io.*;


// how to use this class:
// String path = "/home/ermiya/eclipse-workspace/SaminJoon/New Folder/ref/";
// String cfile = "fips202.c";
// String output = CCompiler.compile(path, cfile);


public class CCompiler {

	static String className;
	static String command, command1, command2;

	public static void compile(String filePath, String fileName) {
		File file = new File("poly_test1.txt");
		file.delete();
		File dir = new File(filePath);
		CCompiler.getMsg(fileName, dir);
	}

	public static void getMsg(String fileName, File Path1) {
		command = "gcc " + fileName;
		 executeCommand(command, Path1);
		executeCommand("./a.out", Path1);
	}

	private static void executeCommand(String command, File Path1) {
		//StringBuffer output = new StringBuffer();
		File file = new File("poly_test1.txt");
		Process p;
		try {
			p = Runtime.getRuntime().exec(command, null, Path1);
			//BufferedReader reader1 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			/*while ((line = reader1.readLine()) != null) {
				//output.append(line + "\n");
				System.out.println(line);
				write(line + "\n",file);
			}*/
			//System.out.println(((line = reader1.readLine()) != null));
			while ((line = reader2.readLine()) != null) {
				//p.waitFor();System.out.println(line);
				//output.append(line + "\n");
				write(line + "\n",file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return output.toString();
	}

	static public void write(String string,File file) {


		BufferedWriter bw = null;


		try {
			// create string writer
			FileWriter fw = new FileWriter(file,true);

			//create buffered writer
			bw = new BufferedWriter(fw);

			// append subsequence of character sequence.
			bw.append(string);
			bw.flush();
			bw.close();


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}