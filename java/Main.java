
import java.io.*;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
public class Main {
    
    @Parameter (names={"-K", "-k"})	    
    int key;	
    @Parameter (names={"-I", "-i"})
    String input;
    @Parameter (names={"-O", "-o"})
    String output;
    @Parameter (names={"-M", "-m"})
    String mode;
    
    public static void main(String ... argv) {
	Main main = new Main();
	    
	JCommander.newBuilder().addObject(main).build().parse(argv);

	main.run();
    }
    public void run() {
	System.out.printf("%d %s %s %s \n",
		key, input, output, mode);
    }
    
}
