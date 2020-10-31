import java.io.BufferedReader;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import java.io.FileReader;
import java.util.Base64;
import java.lang.StringBuilder;
public class Main {
    
    @Parameter (names={"-K", "-k"})	    
    String keyFile;	
    @Parameter (names={"-I", "-i"})
    String inputFile;
    @Parameter (names={"-O", "-o"})
    String outputFile;
    @Parameter (names={"-M", "-m"})
    String mode;
    
    public static void main(String ... argv) {
	Main main = new Main();
	JCommander.newBuilder().addObject(main).build().parse(argv);
	main.run();
    }
    
    public void run() {
	String key = readFile(keyFile);
	String input = readFile(inputFile);
	String output = readFile(outputFile);
		
	System.out.printf("Key: %s\n", key);
	System.out.printf("Binary Key: %s\n", Base64toBinary(key));
    }
	

    public static String Base64toBinary(String base64Str) {
	byte[] decodedKey = Base64.getDecoder().decode(base64Str);
	
	StringBuilder sb = new StringBuilder();

	for(int i = 0; i < decodedKey.length; i++) {
	    String tmp = Integer.toBinaryString(decodedKey[i]);
	    sb.append(String.format("%8s", tmp).replace(" ", "0"));
	}
	
	return sb.toString();	
    }
    public static String readFile(String file) {
	try {
	    String fileContent = "";
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) { 
		fileContent += line;
                line = br.readLine();
            }
            br.close();

            return fileContent;

            } catch (Exception e) {
                System.out.print("Error while reading " + file + "\n");
            }

            return null;
    }    
}
