package AnalizadorSintactico;

import AnalizadorLexico.Token;
public class ExcepcionSintactica extends Exception {
    private Token tokenActual;
    private String tokenId;

    public ExcepcionSintactica(Token tokenActual, String tokenId){
        this.tokenActual = tokenActual;
        this.tokenId = tokenId;
    }

    public String getMessage() {
        return "Error sintactico en la linea "
                + this.tokenActual.getNro_linea()
                + ": se esperaba "
                + this.tokenId
                + " y se encontro "
                + this.tokenActual.getToken_id()
                + generarMensajeError();
    }

    public String generarMensajeError(){
        return "\n\n[Error:" +
                this.tokenActual.getLexema()
                + "|"
                + this.tokenActual.getNro_linea()
                + "]\n\n";

    }
}
