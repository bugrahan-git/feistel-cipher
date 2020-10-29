public class Cipher {
    
    private int round;
    private int blockSize;
    private int keySize;

    public Cipher() {
	this.round = 10;
	this.blockSize = 96;
	this.keySize = 96;
    }

    public void print() {
	System.out.printf("%d %d %d\n", this.round, this.blockSize, this.keySize);
    
    
    }



}
