import java.lang.StringBuilder;

public class FeistelCipher {    
    private int round;
    private int blockSize; 
    private String key; // 96 bits binary string
    private SBox box;
    private String mode; // encryption mode

    public FeistelCipher(int round, int blockSize, String key, String mode) {
	this.round = round;
	this.blockSize = blockSize;
	this.key = key;
	this.box = new SBox();
	this.mode = mode;
    }    
        
    private String subkeyGeneration(int round) {
	String shiftedKey = key.substring(round+1) + key.substring(0, round+1);
	StringBuilder sb = new StringBuilder();

	for(int i = round % 2 ; i < shiftedKey.length(); i+=2)
	    sb.append(shiftedKey.charAt(i));
	
	return sb.toString();
    }

    // String Ri: 48 Bits long binary string, message
    // String Ki: 48 Bits long binary string, key
    private String scramble(String Ri, String Ki) {
	String Ri_xor_Ki = this.XOR(Ri, Ki);
	String[] px = new String[12];
	
	for(int i = 0; i < 8 ; i++) 
	    px[i] = Ri_xor_Ki.substring(i*6, (i+1)*6);	
	
	for(int i = 0; i < 4; i++) 
	    px[i+8] = this.XOR(px[i*2], px[i*2+1]);

	StringBuilder sb = new StringBuilder();
	
	for(String str : px) 
	    sb.append(this.box.S(str));
	

	for(int i = 0; i < this.blockSize / 4 ; i++) {
	    char l = sb.charAt(2*i);
	    char r = sb.charAt(2*i+1);
	    sb.setCharAt(2*i, r);
	    sb.setCharAt(2*i+1, l);
	}
	
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

    public String encrypt(String message) {
	if(this.mode.equals("ecb")) 
	    return this.ECB(message);
	else if(this.mode.equals("cbc"))
	    return this.CBC(message);
	else if(this.mode.equals("ofb"))
	    return this.OFB(message);
	return null;
    }

    
    private String ECB(String message) {
    	StringBuilder sb = new StringBuilder();
	
	/*
	    Bi: i'th block 
	    Li: Left half of the i'th block
	    Ri: Right half of the i'th block 
	 */

	for(int i = 0; i < message.length(); i+=this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    String Li = Bi.substring(0, this.blockSize/2);
	    String Ri = Bi.substring(this.blockSize/2, this.blockSize);	

	    for(int j = 0; j < this.round; j++) {
		String Ki = this.subkeyGeneration(j);
		String after_scramble = this.scramble(Ri, Ki);

		String tmp = Ri;
		Ri = this.XOR(after_scramble, Li);
		Li = tmp;
	    }

	    sb.append(Li).append(Ri);
	}
	
	return sb.toString();
    }

    private String CBC(String message) {
	return null;
    }
    private String OFB(String message) {
	return null;
    }
}
