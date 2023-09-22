package AnalizadorSemantico;

import java.util.ArrayList;

public class ExcepcionSemantica extends Exception{

    private ArrayList<ErrorSemantico> errores;

    public ExcepcionSemantica(ArrayList<ErrorSemantico> errores){
        this.errores = errores;
    }

    public String getMessage(){
        String mensajeRetornar = "";
        for(ErrorSemantico error : errores)
            mensajeRetornar += "Error semantico en la linea "
                            + error.obtenerTokenConError().getNro_linea()
                            + ": "
                            + error.obtenerMensajeError()
                            + generarMensajeError(error);

        return mensajeRetornar;
    }

    private String generarMensajeError(ErrorSemantico errorSemantico){
        return "\n\n[Error:" +
                errorSemantico.obtenerTokenConError().getLexema()
                + "|"
                + errorSemantico.obtenerTokenConError().getNro_linea()
                + "]\n\n";
    }

}
