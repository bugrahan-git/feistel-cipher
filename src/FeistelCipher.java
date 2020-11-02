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
     
    private String FEncrypt(String Bi) {
	String Li = Bi.substring(0, this.blockSize/2);
	String Ri = Bi.substring(this.blockSize/2, this.blockSize);

	for(int i = 0; i < this.round; i++) {
	    String Ki = this.subkeyGeneration(i);
	    String afterScramble = this.scramble(Ri, Ki);    
	    String tmp = Ri;
	    Ri = this.XOR(afterScramble, Li);
	    Li = tmp;
	}

	return Li + Ri;
    }

    private String FDecrypt(String Bi) {
	String Li = Bi.substring(0, this.blockSize/2);
	String Ri = Bi.substring(this.blockSize/2, this.blockSize);
	
	for(int i = this.round - 1; i >= 0; i--) {
	    String Ki = this.subkeyGeneration(i);
	    String afterScramble = this.scramble(Li, Ki);
	    String tmp = Li;
	    Li = this.XOR(afterScramble, Ri);
	    Ri = tmp;
	}
	
	return Li + Ri;
    }

    public String encrypt(String message) {
	if(this.mode.equals("ecb")) 
	    return this.ECBEncrypt(message);
	else if(this.mode.equals("cbc"))
	    return this.CBCEncrypt(message);
	else if(this.mode.equals("ofb"))
	    return this.OFB(message);
	return null;
    }

    public String decrypt(String message) {
	if(this.mode.equals("ecb"))
	    return this.ECBDecrypt(message);
	else if(this.mode.equals("cbc"))
	    return this.CBCDecrypt(message);
	else if(this.mode.equals("ofb"))
	    return this.OFB(message);
	return null;
    }

    private String OFB(String message) {
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < this.blockSize; i++)
	    sb.append("1");

	String Vinit = sb.toString();

	sb.delete(0, sb.length());

	for(int i = 0; i < message.length(); i+=this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    Vinit = this.FEncrypt(Vinit);
	    String tmp = Vinit;
	    Bi = this.XOR(Vinit, Bi);
	    Vinit = tmp;
	    sb.append(Bi);
	}

	return sb.toString();
    }    

    private String CBCEncrypt(String message) {
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < this.blockSize; i++)
	    sb.append("1");
	
	String Vinit = sb.toString();
	sb.delete(0, sb.length());
	for(int i = 0; i < message.length(); i+=this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    Bi = this.XOR(Vinit, Bi);
	    Vinit = this.FEncrypt(Bi);
	    sb.append(Vinit);    
	}

	return sb.toString();
    }
    
    private String CBCDecrypt(String message) {
	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < this.blockSize; i++) 
	    sb.append("1");

	String Vinit = sb.toString();

	sb.delete(0, sb.length());
	for(int i = 0; i < message.length(); i+=this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    String tmp = Bi;
	    Bi = this.FDecrypt(Bi);
	    Bi = this.XOR(Vinit, Bi);	
	    sb.append(Bi);
	    Vinit = tmp;    
	}
    
	return sb.toString();
    }

    private String ECBEncrypt(String message) {
    	StringBuilder sb = new StringBuilder();
	for(int i = 0; i < message.length(); i+=this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    Bi = this.FEncrypt(Bi); 
	    sb.append(Bi);
	}

	return sb.toString();
    }
   
    private String ECBDecrypt(String message) {
	StringBuilder sb = new StringBuilder(); 
	for(int i = 0; i < message.length(); i+= this.blockSize) {
	    String Bi = message.substring(i, i+this.blockSize);
	    Bi = this.FDecrypt(Bi);   
	    sb.append(Bi);
	}

	return sb.toString();	
    } 

}
