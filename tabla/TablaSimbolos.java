package tabla;

import java.util.ArrayList;
import java.util.Collections;

import token.TiposToken;

public class TablaSimbolos {
	//Lista con todas las clases que se crean 
	public static ArrayList<TablaSimbolos> listaTablas=new ArrayList<TablaSimbolos>();
	//Id de la tabla que incrementa cada vez que se instancia una diferente
	private static int inicial=0;
	public int idTabla;
	private int desplazamiento=0;
	//ArrayList de entradas de la tabla
	//private ArrayList<EntradaTS> entradas=new ArrayList<EntradaTS>();
	//lexemas y tipos de los tokens de la TS
	public ArrayList<String> lexemaParametrosFuncion;
	public ArrayList<TiposToken> tiposParametrosFuncion;
	public ArrayList<String> lexemaParametros;
	public ArrayList<TiposToken> tiposParametros;
	public static int tablaActual=0;
	public TablaSimbolos() {
		lexemaParametros=new ArrayList<String>();
		tiposParametros=new ArrayList<TiposToken>();
		lexemaParametrosFuncion=new ArrayList<String>();
		tiposParametrosFuncion=new ArrayList<TiposToken>();
		this.idTabla=inicial;
		inicial++;
		listaTablas.add(this);
	}

	public int getIdTabla() {
		return this.idTabla;
	}

	//Existe lexema en tabla?
	public boolean existeEnTabla(String lexema) {
		if (lexemaParametros.contains(lexema)) {
			return true;
		}
		else return false;
	}
	//TiposToken del lexema de la tabla
	public TiposToken tipoLexema(String lexema) {
		int i=lexemaParametros.indexOf(lexema);
		return tiposParametros.get(i);
	}

	public void meterLexema(String lexema) {
		lexemaParametros.add(lexema);
	}

	public void meterTipo(TiposToken tipoToken) {
		tiposParametros.add(tipoToken);
	}

	private ArrayList<String> getFunciones(TablaSimbolos global){
		ArrayList<String> aux=new ArrayList<String>();
		for (int i=0;i<global.tiposParametros.size();i++) {
			if (global.tiposParametros.get(i).equals(TiposToken.T_VACIO)||global.tiposParametros.get(i).equals(TiposToken.T_FUNCBOOLEAN)||global.tiposParametros.get(i).equals(TiposToken.T_FUNCINT)||global.tiposParametros.get(i).equals(TiposToken.T_FUNCSTRING)) {
				aux.add(lexemaParametros.get(i));
			}	
		}
		return aux;
	}

	//private ArrayList<String> auxFunciones=getFunciones(listaTablas.get(0));

	//Metodo para imprimir la tabla
	@Override
	public String toString() {
		String cabecera="CONTENIDO DE LA TABLA # " + idTabla;

		String cuerpo="";
		//System.out.println(listaTablas.get(idTabla).lexemaParametrosFuncion);
		//Iteramos en cada

		cuerpo+="\n\n";
		for (String lexema:lexemaParametros) {
			//if (this.)

			cuerpo+="* LEXEMA : "+"'"+lexema+"'"+"\n";
			cuerpo+="\t "+"ATRIBUTOS :"+"\n";


			if (tipoLexema(lexema).equals(TiposToken.T_VACIO)) {
				tablaActual++;
				//cabecera+="(funcion "+lexema+" )";
				cuerpo+="\t "+"+ tipo : "+ "funcion" +"\n";
				cuerpo+="\t "+"+ numparam : "+listaTablas.get(tablaActual).lexemaParametrosFuncion.size()+"\n";
				//System.out.println(tiposParametrosFuncion);
				for (int i=0;i<listaTablas.get(tablaActual).lexemaParametrosFuncion.size();i++) {

					cuerpo+="\t "+"+ tipoParam" + i + " : "+listaTablas.get(tablaActual).tiposParametrosFuncion.get(i)+"\n";
				}
				cuerpo+="\t "+"+ tipoRetorno : "+TiposToken.T_VACIO+"\n";
				cuerpo+="\t "+"+ etiqFuncion : "+ "ET" +lexema +"\n";
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_FUNCBOOLEAN)) {
				tablaActual++;
				//cabecera+="(funcion "+lexema+" )";
				cuerpo+="\t "+"+ tipo : "+ "funcion" +"\n";
				cuerpo+="\t "+"+ numparam : "+listaTablas.get(tablaActual).lexemaParametrosFuncion.size()+"\n";
				//System.out.println(tiposParametrosFuncion);
				for (int i=0;i<listaTablas.get(tablaActual).lexemaParametrosFuncion.size();i++) {

					cuerpo+="\t "+"+ tipoParam" + i + " : "+listaTablas.get(tablaActual).tiposParametrosFuncion.get(i)+"\n";
				}
				cuerpo+="\t "+"+ tipoRetorno : "+TiposToken.T_FUNCBOOLEAN+"\n";
				cuerpo+="\t "+"+ etiqFuncion : "+ "ET" +lexema +"\n";
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_FUNCINT)) {
				tablaActual++;
				//cabecera+="(funcion "+lexema+" )";
				cuerpo+="\t "+"+ tipo : "+ "funcion" +"\n";
				cuerpo+="\t "+"+ numparam : "+listaTablas.get(tablaActual).lexemaParametrosFuncion.size()+"\n";
				//System.out.println(tiposParametrosFuncion);
				for (int i=0;i<listaTablas.get(tablaActual).lexemaParametrosFuncion.size();i++) {

					cuerpo+="\t "+"+ tipoParam" + i + " : "+listaTablas.get(tablaActual).tiposParametrosFuncion.get(i)+"\n";
				}
				cuerpo+="\t "+"+ tipoRetorno : "+TiposToken.T_FUNCINT+"\n";
				cuerpo+="\t "+"+ etiqFuncion : "+ "ET" +lexema +"\n";
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_FUNCSTRING)) {
				tablaActual++;
				//cabecera+="(funcion "+lexema+" )";
				cuerpo+="\t "+"+ tipo : "+ "funcion" +"\n";
				cuerpo+="\t "+"+ numparam : "+listaTablas.get(tablaActual).lexemaParametrosFuncion.size()+"\n";
				//System.out.println(tiposParametrosFuncion);
				for (int i=0;i<listaTablas.get(tablaActual).lexemaParametrosFuncion.size();i++) {	
					cuerpo+="\t "+"+ tipoParam" + i + " : "+listaTablas.get(tablaActual).tiposParametrosFuncion.get(i)+"\n";
				}
				cuerpo+="\t "+"+ tipoRetorno : "+TiposToken.T_FUNCSTRING+"\n";
				cuerpo+="\t "+"+ etiqFuncion : "+ "ET" +lexema +"\n";
			}


			else if (tipoLexema(lexema).equals(TiposToken.T_STRING)) {
				cuerpo+="\t "+"+ tipo : "+TiposToken.T_STRING+"\n";
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=4;
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_INT)) {
				cuerpo+="\t "+"+ tipo : "+TiposToken.T_INT+"\n";
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=2;
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_BOOLEAN)) {
				cuerpo+="\t "+"+ tipo : "+TiposToken.T_BOOLEAN+"\n";
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=1;

			}
			cuerpo+="--------- ----------"+"\n";
		}
		return cabecera+cuerpo;
	}
}
