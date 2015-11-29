package com.tang.base.common.utils;

public class PrimitiveType {
//	boolean、byte、short、int、long、char、float 和 double
	private static boolean booleanDefault;
	private static byte byteDefault;
	private static short shortDefault;
	private static int intDefault;
	private static long longDefault;
	private static char charDefault;
	private static float floatDefault;
	private static double doubleDefault;
	
	public static Object getPrimitiveDefaultValue(Class<?> c){
		if(c.getName().equals("boolean")){
			return booleanDefault;
		}else if(c.getName().equals("byte")){
			return byteDefault;
		}else if(c.getName().equals("short")){
			return shortDefault;
		}else if(c.getName().equals("int")){
			return intDefault;
		}else if(c.getName().equals("long")){
			return longDefault;
		}else if(c.getName().equals("char")){
			return charDefault;
		}else if(c.getName().equals("float")){
			return floatDefault;
		}else if(c.getName().equals("double")){
			return doubleDefault;
		}
		return null;
	}

}
