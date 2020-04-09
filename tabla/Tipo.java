package tabla;

import token.TiposToken;

public class Tipo {
	private TiposToken tipoToken;
	
	public Tipo(TiposToken tipoToken) {
		this.tipoToken=tipoToken;
	}
	
	public TiposToken getTipoToken() {
		return tipoToken;
	}
	public void setTipoToken(TiposToken tipoToken) {
		this.tipoToken = tipoToken;
	}

}
