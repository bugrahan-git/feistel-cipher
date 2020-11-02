public class SBox {
    
    
    private int[][] box = {
    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };
    
    // String str: 6 bits long binary string
    public String S(String str) {
	String mid = str.substring(1, 5);
	String other = str.charAt(0) + str.charAt(5);
	int midInt = Integer.parseInt(mid, 2);
	int otherInt = Integer.parseInt(other, 2);
	return Integer.toBinaryString(this.box[otherInt][midInt]);
    }

}
