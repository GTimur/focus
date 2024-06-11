package club.focus.focus.utils;

import java.nio.charset.StandardCharsets;

public class MessageGenerator {
	
    public static String composeNotEnoughMoneyForDocsInProgressMessage(String text) {
    	String BAL_MIN_VIOLATION = "Недостаточно средств. Измените сумму операции, учитывая неснижаемый остаток по вкладу %.2f %s.";
    	//EURO = e2 82 ac
    	//RUBLE = e2 82 bd
    	
    	//byte[] c = new byte[] { (byte) 0xE2, (byte) 0x82, (byte) 0xBD };
    	int c = 0xE282BD;
    	String s = "\u20bd";//Integer.toString(c);
    	
    	
		//String RUBLESIGN = new String(new byte[] { (byte) 0xE2, (byte) 0x82, (byte) 0xBD }, StandardCharsets.UTF_8);
		final String RUBLESIGN = "\u20BD";
		
		String formatedString = String.format(BAL_MIN_VIOLATION,1000.10, RUBLESIGN, s);
		return formatedString;
	}

}

