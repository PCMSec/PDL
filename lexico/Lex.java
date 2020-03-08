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
import java.nio.file.Files;
import java.nio.file.Paths;
import token.TiposToken;
import token.Token;


public class Lex {
	
	private int linea;
	public static boolean noContar;
	
	public Lex() {
		linea=0;
		noContar=false;
	}
	public static String leerArchivo(String filePath) {
		String resultado="";
		try {
			resultado= new String(Files.readAllBytes(Paths.get(filePath)));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
}
