package errores;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import principal.Principal;

public class Error {
	public static PrintWriter writer;
	
	public Error() {
		try {
			writer = new PrintWriter(Principal.directorioADevolver+"/ResultadoErrores.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void escribirError(String error) {
		writer.println(error);
		
	}
	
	public void cerrarArchivo() {
		writer.close();
	}
}
