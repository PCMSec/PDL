package principal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList; 
import lexico.Lex;
import token.*;
public class Principal {

	public static void main(String[] args) {
		String texto=lexico.Lex.leerArchivo("/home/pablo/eclipse-workspace/PDL/docs/test.txt");
		//Se deja preparado el texto con el que se va a trabajar
		texto=texto.replace(";", " ; ");
		texto=texto.replace(",", " , ");
		texto=texto.replace(":", " : ");
		texto=texto.replace("(", " ( ");
		texto=texto.replace(")", " ) ");
		//texto=texto.replace("[", " [ ");
		//texto=texto.replace("]", " ] ");
		texto=texto.replace("{", " { ");
		texto=texto.replace("}", " } ");
		texto=texto.replace("--", " -- ");
		//texto=texto.replace("`", " ` ");
		texto=texto.replace("\"", " \" ");
		texto=texto.replace("=", " = ");
		texto=texto.replace("!", " ! ");
		texto=texto.replace("<", " < ");
		texto=texto.replace(">", " > ");
		//Arraylist con todos los tokens leidos del archivo de entrada
		ArrayList<Token> listaTokens=new ArrayList<Token>();
		//Se preparan los regex para identificar con lo que se trabaja
		String identificador=new String("^[a-zA-ZÀ-ÿ]+[0-9|_|a-zA-ZÀ-ÿ]*$");
		String entero=new String("^\\d+$");
		String comentario=new String("^[/][*]+$");
		String comentario2=new String("^[*]+[/]$");

		String tokens[]=texto.split("\\s+");
		boolean encadenado=false;
		boolean noContar=false;
		for (String token:tokens) {
			System.out.println(token);
			if ((noContar) && !((token.matches(comentario2)))) {
				continue;
			}
			else if (token.matches(entero)) {
				Token aux=new Token(TiposToken.T_ENTERO, Integer.parseInt(token));
				listaTokens.add(aux);
			}
			else if ((encadenado) && !(token.equals("\""))) {
				Token aux=new Token(TiposToken.T_CADENA, token);
				listaTokens.add(aux);
				continue;
			}
			else if (token.matches(identificador)) {
				//TODO mejorar esta parte para el ID
				Token aux=new Token(TiposToken.T_ID, token);
				listaTokens.add(aux);
			}
			else if (token.matches(comentario)) {
				//TODO mejorar esta parte para el ID
				noContar=true;
			}
			else if (token.matches(comentario2)) {
				//TODO mejorar esta parte para el ID
				noContar=false;
			}
			else if (token.equals(";")) {
				Token aux=new Token(TiposToken.T_PUNTOCOMA);
				listaTokens.add(aux);
			}
			else if (token.equals(",")) {
				Token aux=new Token(TiposToken.T_COMA);
				listaTokens.add(aux);
			}
			else if (token.equals(":")) {
				Token aux=new Token(TiposToken.T_DOSPUNTOS);
				listaTokens.add(aux);
			}
			else if (token.equals("(")) {
				Token aux=new Token(TiposToken.T_PARENTESISABRE);
				listaTokens.add(aux);
			}
			else if (token.equals(")")) {
				Token aux=new Token(TiposToken.T_PARENTESISCIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("{")) {
				Token aux=new Token(TiposToken.T_LLAVEABRE);
				listaTokens.add(aux);
			}
			else if (token.equals("}")) {
				Token aux=new Token(TiposToken.T_LLAVECIERRA);
				listaTokens.add(aux);
			}
			else if (token.equals("--")) {
				Token aux=new Token(TiposToken.T_POSTDECREMENTO);
				listaTokens.add(aux);
			}
			else if (token.equals("=")) {
				Token aux=new Token(TiposToken.T_IGUAL);
				listaTokens.add(aux);
			}
			else if (token.equals("!")) {
				Token aux=new Token(TiposToken.T_DISTINTO);
				listaTokens.add(aux);
			}
			else if (token.equals("<")) {
				Token aux=new Token(TiposToken.T_MENOR);
				listaTokens.add(aux);
			}
			else if (token.equals(">")) {
				Token aux=new Token(TiposToken.T_MAYOR);
				listaTokens.add(aux);
			}
			
			else if (token.equals("\"")) {
				if (encadenado) {
					encadenado=false;
				}
				else {
					encadenado=true;
				}
				Token aux=new Token(TiposToken.T_CADENA);
				listaTokens.add(aux);
			}
		}
		//Testing
		for (Token token:listaTokens) {
			System.out.println(token.tokenizar());
		}
	}

}
