package principal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
		texto=texto.replace("{", " { ");
		texto=texto.replace("}", " } ");
		texto=texto.replace("--", " -- ");
		texto=texto.replace("+", " + ");
		texto=texto.replace("\"", " \" ");
		texto=texto.replace("=", " = ");
		texto=texto.replace("!", " ! ");
		texto=texto.replace("<", " < ");
		texto=texto.replace(">", " > ");
		//Arraylist con todos los tokens leidos del archivo de entrada
		ArrayList<Token> listaTokens=new ArrayList<Token>();
		//Se preparan los regex para identificar con lo que se trabaja
		String identificador=new String("^[a-zA-ZÀ-ÿ]+[0-9|_|a-zA-ZÀ-ÿ]*$");
		String entero=new String("^-?\\d+$");
		String comentario=new String("^[/][*]+$");
		String comentario2=new String("^[*]+[/]$");

		String tokens[]=texto.split("\\s+");
		//booleanos para comentarios tipo java y para concatenar strings
		boolean encadenado=false;
		boolean noContar=false;
		//String para concatenado que hay que limpiar cuando se escriba un string entero
		String concatenado="";
		for (String token:tokens) {
			//System.out.println(token);
			if ((noContar) && !((token.matches(comentario2)))) {
				continue;
			}
			else if (token.matches(entero)) {
				Token aux=new Token(TiposToken.T_ENTERO, Integer.parseInt(token));
				listaTokens.add(aux);
			}
			else if ((encadenado) && !(token.equals("\""))) {
				concatenado+=" "+token;
				continue;
			}
			else if (token.matches(identificador)) {
				boolean reservada=false;
				//TODO mejorar esta parte para el ID si esta en tabla de simbolos
				for (String palabraReservada:Lex.palabrasReservadas) {
					if (palabraReservada.equals(token)) {
						reservada=true;
					}
				}
				if (reservada) {
					Token aux=new Token(Lex.decodificar(token));
					listaTokens.add(aux);
					continue;
				}
				else {
					Token aux=new Token(TiposToken.T_ID, token);
					listaTokens.add(aux);
				}
			}
			else if (token.matches(comentario)) {
				noContar=true;
			}
			else if (token.matches(comentario2)) {
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
			else if (token.equals("+")) {
				Token aux=new Token(TiposToken.T_SUMA);
				listaTokens.add(aux);
			}

			else if (token.equals("\"")) {
				if (encadenado) {//Hay que dejar de concatenar
					concatenado+=" "+token;
					concatenado=concatenado.replaceFirst(" ", "");
					concatenado=concatenado.replace(" \"", "\"");
					Token aux=new Token(TiposToken.T_CADENA,concatenado);
					listaTokens.add(aux);

					concatenado="";
					encadenado=false;
				}
				else {//Hay que empezar a concatenar
					concatenado+=token;
					encadenado=true;
				}
			}
		}
		//Testing

		try {
			PrintWriter writer = new PrintWriter("/home/pablo/eclipse-workspace/PDL/docs/tokens.txt", "UTF-8");
			for (Token token:listaTokens) {
				System.out.println(token.tokenizar());
				writer.println(token.tokenizar());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}


		
	}

}
