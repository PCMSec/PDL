package lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import token.TiposToken;
import token.Token;


public class Lex {
	Charset encoding = Charset.defaultCharset();
	private InputStream in;
	private Reader r;
	private String filename;


	public Lex(String filename) throws FileNotFoundException {
		this.filename=filename;
		this.in=new FileInputStream(filename);
		this.r=new InputStreamReader(in, encoding);

	}

	public void transito() throws IOException {
		int estado=0;
		String lexema=null;
		int valor=0;
		int i;

		while((i=r.read()) != -1) {
			
			char caracter=leer(i);
			System.out.print(caracter);
			switch(estado) {
			case 0:
				if (Character.isWhitespace(caracter)) {
					estado=0;
				}
				else if (Character.isLetter(caracter)) {
					lexema+=caracter;
					estado=1;
				}
				else if (Character.isDigit(caracter)) {
					valor=Character.getNumericValue(caracter);
					estado=3;
				}
				else if (caracter=='/') {
					lexema="/";
					estado=6;
				}
				else if(caracter=='-') {
					lexema="-";
					estado=10;
				}
				else if(caracter=='!') {
					lexema="!";
					estado=13;
				}
				else if(caracter=='=') {
					System.out.println("ojoooooooooooo");
					lexema="=";
					Token igual=new Token(TiposToken.T_IGUAL);
					System.out.println(igual.tokenizar());
					estado=0;
				}
				else if(caracter=='"') {
					lexema="\"";
					estado=17;
				}
				break;
				//fin de switch
			}
		}

	}

	public char leer(int i) {
		char ch=(char) i;
		return ch;
	}
}
