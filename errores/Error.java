package errores;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Error {
	String filename="/home/pablo/eclipse-workspace/PDL/docs/errores.txt";
	public static PrintWriter writer;
	
	public Error() {
		try {
			writer = new PrintWriter(filename, "UTF-8");
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
