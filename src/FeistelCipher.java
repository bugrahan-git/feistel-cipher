import java.lang.StringBuilder;

public class FeistelCipher {    
    private int round;
    private int blockSize; 
    private int keySize; 
    private String key; // Binary string

    public FeistelCipher(int round, int blockSize, int keySize, String key) {
	this.round = round;
	this.blockSize = blockSize;
	this.keySize = keySize;
	this.key = key;
    }

    public void print() {
	System.out.printf("%d %d %d\n", this.round, this.blockSize, this.keySize);  
    }
    
        
    public String subkeyGeneration(int round) {
	String shiftedKey = key.substring(round) + key.substring(0, round);
	StringBuilder sb = new StringBuilder();

	for(int i = round % 2 ; i < shiftedKey.length(); i+=2)
	    sb.append(shiftedKey.charAt(i));
	
	return sb.toString();
    }

}
