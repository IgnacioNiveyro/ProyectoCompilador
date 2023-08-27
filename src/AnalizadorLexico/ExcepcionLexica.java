package AnalizadorLexico;

public class ExcepcionLexica extends Exception{
    private int numeroLinea;
    private int numeroColumna;
    private String lexemaConError, lineaDelErrorLexico, tipoError;

    public ExcepcionLexica(int numeroLinea, int numeroColumna, String lexemaConError, String lineaDelErrorLexico, String tipoError){
        this.numeroLinea = numeroLinea;
        this.numeroColumna = numeroColumna;
        this.lexemaConError = lexemaConError;
        this.lineaDelErrorLexico = lineaDelErrorLexico;
        this.tipoError = tipoError;
    }

    public String getMessage(){
        return this.generarStringError();
    }

    public String generarStringError(){
        return "Error lexico en la linea "+this.numeroLinea+", columna "+this.numeroColumna+": "+this.tipoError+ "\n"
                + this.generarDetalleError() + "\n[Error:"+this.lexemaConError+"|"+this.numeroLinea+"]\n\n";
    }

    public String generarDetalleError(){
        String detalleError = "Detalle: ";
        int longitudInicialDetalleError = detalleError.length();
        detalleError += this.lineaDelErrorLexico;
        String punteroError = "";
        for ( int corrimiento = 1; corrimiento < (this.numeroColumna +  longitudInicialDetalleError); corrimiento++){
            punteroError += " ";
        }
        punteroError += "^";
        return detalleError + "\n" + punteroError;
    }
}
