package principal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList; 
import lexico.Lex;
import tabla.TablaSimbolos;
import token.*;
import tabla.*;
public class Principal {

	public static void main(String[] args) {

		String texto=lexico.Lex.leerArchivo("/home/pablo/eclipse-workspace/PDL/docs/sencillo.txt");
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
				//System.out.println(token.tokenizar());
				writer.println(token.tokenizar());
			}
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		//INICIO TABLA SIMBOLOS
		//Var o no var indica cambio entre definicion y uso.
		//Declaracion, tabla global y tabla actual con la que se esta trabajando

		//declaracion para obtener var int s etc...
		boolean declaracion=false;
		//Tenemos ya el tipo del var?
		boolean tipo=false;
		//Estamos o no en una function
		boolean funcion=false;
		//boolean para comprobar si tiene o no parametros la function
		boolean parametros=false;
		//boolean antesFunc porque quiero empezar a leer los parametro de entrada en el parentesis de la func
		boolean antesFunc=false;
		//cuerpo de func
		boolean cuerpo=false;
		//leyendo elementos denro del parentesis
		boolean dentroParentesis=false;
		boolean decGlobal=false;
		boolean tipoid=false;
		int nCorchetes=0;
		//Lista de parametros de func y de su lexema
		ArrayList<String> lexemaParametros=new ArrayList<String>();
		ArrayList<TiposToken> tiposParametros=new ArrayList<TiposToken>();
		TablaSimbolos global=new TablaSimbolos();
		TablaSimbolos actual=global;
		TiposToken tipoActual=null;
		TiposToken tipoFuncion=null;
		String lexema="";
		//Vamos leyendo todos los tokens uno a uno
		for (Token token:listaTokens) {
			//System.out.println(token.getToken());
			//System.out.println(token.getLexema());
			if (token.getToken().equals(TiposToken.T_VAR)) {
				//Se activa la declaracion porque ha leido var
				declaracion=true;
			}
			else if(!(antesFunc)&&!(dentroParentesis)&&!(declaracion) && ((token.getToken().equals(TiposToken.T_INT))||(token.getToken().equals(TiposToken.T_BOOLEAN))||(token.getToken().equals(TiposToken.T_STRING)))) {
				tipoActual=token.getToken();
				decGlobal=true;
			}
			else if ((decGlobal) && (token.getToken().equals(TiposToken.T_ID))) {
				decGlobal=false;
				global.meterLexema(token.getLexema());
				global.meterTipo(tipoActual);
			}
			else if((declaracion) && ((token.getToken().equals(TiposToken.T_INT))||(token.getToken().equals(TiposToken.T_BOOLEAN))||(token.getToken().equals(TiposToken.T_STRING)))) {
				//var puesto previamente y se lee int string o boolean
				tipoActual=token.getToken();
				declaracion=false;
				tipo=true;
			}
			else if ((tipo) && (token.getToken().equals(TiposToken.T_ID))) {
				//var + tipo + id que estamos leyendo ahora y se limpia lo que se ha usado
				//TODO meter en la tabla en la que se define
				if (actual.existeEnTabla(token.getLexema())) {
					System.out.println("Error, ya existe una variable con el mismo nombre");
				}
				else {
					actual.meterLexema(token.getLexema());
					actual.meterTipo(tipoActual);
					tipoActual=null;
				}
				lexema=token.getLexema();
				//System.out.println(lexema +" "+tipoActual);
				lexema="";
				tipoActual=null;
				tipo=false;
			}
			else if (token.getToken().equals(TiposToken.T_FUNC)) {
				//Tenemos una funcion en el token, acabamos de leer function
				//Hasta que no se salga de la func estamos con funcion a true
				//TODO:cambiar la actual a la tabla que crea la nueva func
				actual=new TablaSimbolos();
				//System.out.println("ENTRANDO EN FUNCION");
				antesFunc=true;
				funcion=true;

			}
			else if((antesFunc) && (funcion) && (token.getToken().equals(TiposToken.T_ID))) {
				//el tipo de funcion es void
				tipoFuncion=TiposToken.T_VACIO;
				global.lexemaParametros.add(token.getLexema());
				global.tiposParametros.add(tipoFuncion);
				//System.out.println(tipoFuncion);
				tipoFuncion=null;
				antesFunc=false;
				parametros=true;

			}
			else if((antesFunc) && (funcion) && ((token.getToken().equals(TiposToken.T_INT))||(token.getToken().equals(TiposToken.T_BOOLEAN))||(token.getToken().equals(TiposToken.T_STRING)))) {
				//tipo de func es alguno de los tres de arriba
				if (token.getToken().equals(TiposToken.T_INT)){
					tipoFuncion=TiposToken.T_FUNCINT;
				}
				if (token.getToken().equals(TiposToken.T_STRING)){
					tipoFuncion=TiposToken.T_FUNCSTRING;
				}
				if (token.getToken().equals(TiposToken.T_BOOLEAN)){
					tipoFuncion=TiposToken.T_FUNCBOOLEAN;
				}
				//System.out.println(tipoFuncion);
				antesFunc=false;
				tipoid=true;
				//parametros=true;
			}
			else if(tipoid && funcion && token.getToken().equals(TiposToken.T_ID)) {
				global.lexemaParametros.add(token.getLexema());
				global.tiposParametros.add(tipoFuncion);
				tipoFuncion=token.getToken();
				tipoid=false;
				parametros=true;

			}
			else if (funcion && parametros) {
				if (token.getToken().equals(TiposToken.T_PARENTESISABRE)) {
					dentroParentesis=true;
					continue;
				}
				if (dentroParentesis) {
					if (token.getToken().equals(TiposToken.T_ID)) {
						//actual.meterLexema(token.getLexema());
						actual.lexemaParametrosFuncion.add(token.getLexema());
					}
					else if (token.getToken().equals(TiposToken.T_INT)||token.getToken().equals(TiposToken.T_BOOLEAN)||token.getToken().equals(TiposToken.T_STRING)){
						//actual.meterTipo(token.getToken());
						actual.tiposParametrosFuncion.add(token.getToken());
					}
					else if (token.getToken().equals(TiposToken.T_PARENTESISCIERRA)) {
						//Ya podemos devolver lo que tengamos en la lista y limpiar la lista para usarla con otras
						//funciones
						//return a la Tabla de simbolos de lo que nos hayan metido
						//return del numero de elementos...
						//System.out.println(token.getToken());
						//System.out.println(actual.lexemaParametros);
						//System.out.println(actual.tiposParametros);
						lexemaParametros.clear();
						tiposParametros.clear();
						parametros=false;
						dentroParentesis=false;
						cuerpo=true;
					}
				}

			}
			else if ((cuerpo) && (token.getToken().equals(TiposToken.T_LLAVEABRE))) {
				nCorchetes++;
			}
			else if ((cuerpo) && (token.getToken().equals(TiposToken.T_LLAVECIERRA))) {
				nCorchetes--;
				if (nCorchetes==0) {
					//System.out.println("FIN DE LA FUNCION");
					actual=global;
					funcion=false;
					cuerpo=false;
				}
			}
			/*else {
				System.out.println("REVISAR");
				System.out.println(token.getToken());
				System.out.println(token.getLexema());
			}*/
		}
		//Añadir a cada TS de funcion los parametro que tiene la global
		if (TablaSimbolos.listaTablas.size()>1) {
			for (TablaSimbolos tabla:TablaSimbolos.listaTablas) {
				if (!tabla.lexemaParametrosFuncion.isEmpty()) {
					tabla.lexemaParametros.addAll(0,tabla.lexemaParametrosFuncion);
					tabla.tiposParametros.addAll(0,tabla.tiposParametrosFuncion);
				}
			}
		}

		for (TablaSimbolos tabla:TablaSimbolos.listaTablas) {
			//System.out.println(tabla.getIdTabla()); System.out.println(tabla.lexemaParametros); System.out.println(tabla.tiposParametros); System.out.println(tabla.lexemaParametrosFuncion); System.out.println(tabla.tiposParametrosFuncion);
			System.out.println(tabla.toString());
		}
	}

}
