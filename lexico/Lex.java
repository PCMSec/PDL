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
import java.util.ArrayList;
import java.util.Arrays;

import token.TiposToken;
import token.Token;

//{"function","return","switch","case","default","var","int","boolean","true","false","break","print","input"};
public class Lex {
	public static final ArrayList<String> palabrasReservadas=new ArrayList<String>(Arrays.asList("function","return","switch","case","default","var","int","boolean","true","false","break","print","input","string","if","prompt"));
	public static boolean noContar;

	public Lex() {
		noContar=false;
	}
	public static TiposToken decodificar(String palabra) {
		switch(palabrasReservadas.indexOf(palabra)) {
		case 0:
			return TiposToken.T_FUNC;
		case 1:
			return TiposToken.T_RETURN;
		case 2:
			return TiposToken.T_SWITCH;
		case 3:
			return TiposToken.T_CASE;
		case 4:
			return TiposToken.T_DEFAULT;
		case 5:
			return TiposToken.T_VAR;
		case 6:
			return TiposToken.T_INT;
		case 7:
			return TiposToken.T_BOOLEAN;
		case 8:
			return TiposToken.T_TRUE;
		case 9:
			return TiposToken.T_FALSE;
		case 10:
			return TiposToken.T_BREAK;
		case 11:
			return TiposToken.T_PRINT;
		case 12:
			return TiposToken.T_INPUT;
		case 13:
			return TiposToken.T_STRING;
		case 14:
			return TiposToken.T_IF;

		}
		

		return TiposToken.T_ERROR;
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
