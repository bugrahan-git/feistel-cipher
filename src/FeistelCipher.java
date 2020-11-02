import java.lang.StringBuilder;

public class FeistelCipher {    
    private int round;
    private int blockSize; 
    private String key; // 96 bits binary string
    private SBox sbox;
    
    public FeistelCipher(int round, int blockSize, String key) {
	this.round = round;
	this.blockSize = blockSize;
	this.key = key;
	this.sbox = new SBox();
    }    
        
    public String subkeyGeneration(int round) {
	String shiftedKey = key.substring(round) + key.substring(0, round);
	StringBuilder sb = new StringBuilder();

	for(int i = round % 2 ; i < shiftedKey.length(); i+=2)
	    sb.append(shiftedKey.charAt(i));
	
	return sb.toString();
    }

    // String Ri: 48 Bits long binary string, message
    // String Ki: 48 Bits long binary string, key
    public String scramble(String Ri, String Ki) {
	String Ri_xor_Ki = this.XOR(Ri, Ki);
	String[] px = new String[12];

	for(int i = 0; i < 8 ; i++) 
	    px[i] = Ri_xor_Ki.substring(i*6, (i+1)*6);	
	
	for(int i = 0; i < 4; i++) 
	    px[i+8] = this.XOR(px[i*2], px[i*2+1]);

	StringBuilder sb = new StringBuilder();

	for(String str : px)
	    sb.append(this.sbox.S(str));
	
	System.out.println(sb.toString());
	return sb.toString();
    }
    

    private String XOR(String Ri, String Ki) {
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < Ri.length(); i++) {
	    if(Ri.charAt(i) == Ki.charAt(i))
		sb.append("0");
	    else
		sb.append("1");
	}

	return sb.toString();
	
    }
}
