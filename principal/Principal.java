package principal;

import java.io.FileNotFoundException;
import java.io.IOException;

import lexico.Lex;

public class Principal {

	public static void main(String[] args) {
		String texto=lexico.Lex.leerArchivo("/home/pablo/eclipse-workspace/PDL/docs/test.txt");
		//Se deja preparado el texto con el que se va a trabajar
		texto=texto.replace(";", " ; ");
		texto=texto.replace(",", " , ");
		texto=texto.replace(":", " : ");
		texto=texto.replace("(", " ( ");
		texto=texto.replace(")", " ) ");
		texto=texto.replace("[", " [ ");
		texto=texto.replace("]", " ] ");
		texto=texto.replace("{", " { ");
		texto=texto.replace("}", " } ");
		texto=texto.replace("--", " -- ");
		texto=texto.replace("`", " ` ");
		texto=texto.replace("\"", " \" ");
		texto=texto.replace("=", " = ");
		texto=texto.replace("!", " ! ");
		texto=texto.replace("<", " < ");
		texto=texto.replace(">", " > ");
		
		//Se preparan los regex para identificar con lo que se trabaja
		String identificador=new String("^[a-zA-ZÀ-ÿ]+[0-9|_|a-zA-ZÀ-ÿ]*$");
		String entero=new String("^\\d+$");

		String tokens[]=texto.split("\\s+");
		for (String token:tokens) {
			if (token.matches(entero)) {
				System.out.println(token + " TRUE");
			}
			else {
				System.out.println(token + " FALSO");
			}
		}
		
	}

}
