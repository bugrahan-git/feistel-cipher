import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Base64;
import java.lang.StringBuilder;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;

public class BBMcrypt {    
    String keyFile;	
    String inputFile;
    String outputFile;
    String mode;
    boolean isEnc = false;

    public static void main(String ... args) {
	BBMcrypt main = new BBMcrypt();
	Map<Character, String> argv = main.getArgs(args);
	main.run(argv);
    }
    
    public void run(Map<Character, String> args) {
	String key = readFile(args.get('k'));
	String input = readFile(args.get('i'));
	String mode = args.get('m');
    	
    	FeistelCipher fc = new FeistelCipher(10, 96, Base64toBinary(key), mode); 
	
	String output;
	if(isEnc)
	    output = fc.encrypt(input);
	else
	    output = fc.decrypt(input);
	
	writeFile(args.get('o'), output); 
    }

    public Map<Character, String> getArgs(String ... args) {
	if(args[0].toLowerCase().equals("enc"))
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
	    StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) { 
		sb.append(line);
                line = br.readLine();
            }
            
	    br.close();
            return sb.toString();

        } catch (Exception e) {
                System.out.print("Error while reading " + file + "\n");
	}

	return null;
    }    

    public static void writeFile(String file, String output){
        try {
            File outputF = new File(file);
            FileWriter writer = new FileWriter(outputF);
            writer.write(output);
            writer.close();
        } catch(Exception e){
            System.out.print("Error while writing to " + file + "\n");
        }
    }
}
