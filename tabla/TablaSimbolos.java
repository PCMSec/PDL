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

	//Metodo para imprimir la tabla
	@Override
	public String toString() {
		String cabecera="CONTENIDO DE LA TABLA # " + idTabla+"\n"+"\n";
		String cuerpo="";
		//System.out.println(listaTablas.get(idTabla).lexemaParametrosFuncion);
		//Iteramos en cada 
		Collections.reverse(lexemaParametros);
		for (String lexema:lexemaParametros) {
			cuerpo+="* LEXEMA : "+"'"+lexema+"'"+"\n";
			cuerpo+="\t "+"ATRIBUTOS :"+"\n";
			cuerpo+="\t "+"+ tipo : "+tipoLexema(lexema)+"\n";
			if (tipoLexema(lexema).equals(TiposToken.T_STRING)) {
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=4;
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_INT)) {
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=2;
			}
			else if (tipoLexema(lexema).equals(TiposToken.T_BOOLEAN)) {
				cuerpo+="\t "+"+ despl : "+desplazamiento+"\n";
				desplazamiento+=1;
				
			}
			cuerpo+="--------- ----------"+"\n";
		}
		return cabecera+cuerpo;
	}
}
