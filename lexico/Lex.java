package lexico;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import token.TiposToken;


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
			return TiposToken.TFUNC;
		case 1:
			return TiposToken.TRETURN;
		case 2:
			return TiposToken.TSWITCH;
		case 3:
			return TiposToken.TCASE;
		case 4:
			return TiposToken.TDEFAULT;
		case 5:
			return TiposToken.TVAR;
		case 6:
			return TiposToken.TINT;
		case 7:
			return TiposToken.TBOOLEAN;
		case 8:
			return TiposToken.TTRUE;
		case 9:
			return TiposToken.TFALSE;
		case 10:
			return TiposToken.TBREAK;
		case 11:
			return TiposToken.TPRINT;
		case 12:
			return TiposToken.TINPUT;
		case 13:
			return TiposToken.TSTRING;
		case 14:
			return TiposToken.TIF;

		}
		

		return TiposToken.TERROR;
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
