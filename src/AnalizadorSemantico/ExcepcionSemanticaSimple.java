package AnalizadorSemantico;
import AnalizadorLexico.Token;
public class ExcepcionSemanticaSimple extends Exception{

    private Token tokenError;
    private String mensajeError;

    public ExcepcionSemanticaSimple(Token tokenError, String mensajeError){
        this.tokenError = tokenError;
        this.mensajeError = mensajeError;
    }
    public String getMensajeError(){
        return "Error Semantico en la linea "
                +this.tokenError.getNro_linea()
                +": "
                +this.mensajeError
                +this.generarStringError();
    }
    private String generarStringError(){
        return "\n\n[Error:" +
                this.tokenError.getLexema()
                + "|"
                + this.tokenError.getNro_linea()
                +"]\n\n";
    }
}
