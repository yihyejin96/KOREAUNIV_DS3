import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Test{
    public static void main(String[] args) throws IOException{
    	RedBlackTree rbt = new RedBlackTree();
    	BufferedReader intput = new BufferedReader(new FileReader("input.txt"));
    	while(true) {
    		String line = intput.readLine();
    		int linenum = Integer.parseInt(line);
    		if (linenum > 0) {
    			rbt.testinsert(linenum);
    		}
    		else if (linenum < 0) {
    			linenum = linenum * -1;
    			rbt.testdelete(linenum);
    		}
    		else if (linenum == 0 || line==null) {
    			break;
    		}
    	}
    	BufferedReader output = new BufferedReader(new FileReader("search.txt"));
    	while(true) {
    		String line = output.readLine();
    		int linenum = Integer.parseInt(line);
    		if (linenum > 0) {
    			rbt.printoutputfunc(linenum);
    		}
    		
    		else if (linenum <= 0 || line==null) {
    			break;
    		}
    	}
    	
    	intput.close();
    	output.close();
        
    }
}