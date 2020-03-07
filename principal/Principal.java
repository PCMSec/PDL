package principal;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexico.Lex;

public class Principal {

	public static void main(String[] args) {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		Lex lexico;
		try {
			lexico = new Lex("/home/pablo/eclipse-workspace/PDL/docs/test.txt");
			lexico.transito();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
