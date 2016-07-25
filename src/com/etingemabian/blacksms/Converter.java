package com.etingemabian.blacksms;

import java.math.BigInteger;

public class Converter {
	
	public static final BigInteger coded(String  encoded){
		return new BigInteger(encoded.getBytes());
	}
	
	public static final String fromCoded(String bar){
		
		BigInteger bigstr = new BigInteger(bar.getBytes());
		
		
		return new String(bigstr.toByteArray().toString());
		
	}

	
}
