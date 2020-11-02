import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Base64;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

public class Main {    
    String keyFile;	
    String inputFile;
    String outputFile;
    String mode;
    boolean isEnc = false;

    public static void main(String ... args) {
	Main main = new Main();
	Map<Character, String> argv = main.getArgs(args);
	main.run(argv);
	
    }
    
    public void run(Map<Character, String> args) {
	String key = readFile(args.get('k'));
	String input = readFile(args.get('i'));
	String output = readFile(args.get('o'));
		
	FeistelCipher fc = new FeistelCipher(10, 96, Base64toBinary(key));    

    }

    public Map<Character, String> getArgs(String ... args) {
	if(args[0].toLowerCase() == "enc")
	    isEnc = true;

	final Map<Character, String> argv= new HashMap<>();
	for(int i = 1; i < args.length; i+=2) {
	    String tmp = args[i].toLowerCase();
	    if(tmp.charAt(0) == '-') 
		argv.put(tmp.charAt(1), args[i + 1]);
	}
	
	return argv;    
    }
 
    public static String Base64toBinary(String base64Str) {
	byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
	String decodedStr = new String(decodedBytes);	
	return decodedStr;	
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
