package tabla;

import java.util.ArrayList;

import token.TiposToken;

public class TablaSimbolos {
	//Lista con todas las clases que se crean 
	public static ArrayList<TablaSimbolos> listaTablas=new ArrayList<TablaSimbolos>();
	//Id de la tabla que incrementa cada vez que se instancia una diferente
	private static int inicial=0;
	public int idTabla;
	//ArrayList de entradas de la tabla
	private ArrayList<EntradaTS> entradas=new ArrayList<EntradaTS>();
	//lexemas y tipos de los tokens de la TS
	public ArrayList<String> lexemaParametros;
	public ArrayList<TiposToken> tiposParametros;

	public TablaSimbolos() {
		lexemaParametros=new ArrayList<String>();
		tiposParametros=new ArrayList<TiposToken>();
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
		for (EntradaTS entrada:entradas) {

		}
		return "";
	}
}
