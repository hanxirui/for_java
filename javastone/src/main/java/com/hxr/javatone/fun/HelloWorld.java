package com.hxr.javatone.fun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HelloWorld {
	public static String randomString(int i) {
		Random ran = new Random(i);
		StringBuilder sb = new StringBuilder();
		while (true) {
			int k = ran.nextInt(27);
			if (k == 0)
				break;

			sb.append((char) ('`' + k));
		}

		return sb.toString();
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(randomString(-229985452) + " " + randomString(-147909649));

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str3 = "1997-12-31 23:54:07";
		String str4 = "1997-12-31 23:54:08";
		Date sDt3 = sf.parse(str3);
		Date sDt4 = sf.parse(str4);
		long ld3 = sDt3.getTime() / 1000;
		long ld4 = sDt4.getTime() / 1000;
		System.out.println(ld4 - ld3);
	}
}
