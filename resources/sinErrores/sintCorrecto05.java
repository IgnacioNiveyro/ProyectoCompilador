///[SinErrores]
//prueba un if anidado


class Prueba1{


	static int prueba1(boolean notCleanCodeFlag, int valorTrueIf, int valorFalseIf)
	{
		
		if(notCleanCodeFlag){
			return valorTrueIf;
		}else
			return valorFalseIf;

		if(!notCleanCodeFlag){
			return valorFalseIf;
		}if(notCleanCodeFlag){
			return valorTrueIf;
		}if(!notCleanCodeFlag && !notCleanCodeFlag){
			return valorFalseIf;
		} else if(notCleanCodeFlag){
			return valorTrueIf;
		} else if(!notCleanCodeFlag){
			return valorFalseIf;
		}

		A.b().c = 1;
		a().m = 5;

		return -1;
		
	}


}