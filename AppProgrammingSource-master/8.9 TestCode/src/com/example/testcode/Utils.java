package com.example.testcode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static IdCard isIdCardNumberValid(String idNumber) {
		IdCard idCard = new IdCard();

		if(null == idNumber){
			idCard.setIdCardDesc(AppConstants.IDCARD_LENGTH_SHOULD_NOT_BE_NULL);
			return idCard;
		}

		String Ai = "";
		String idCardNumber = idNumber.trim();
		if ("".equals(idCardNumber)) {
			idCard.setIdCardDesc(AppConstants.IDCARD_LENGTH_SHOULD_NOT_BE_NULL);
			return idCard;
		}
		
		if ((idCardNumber.length() != 15 && idCardNumber.length() != 18)) {
			idCard.setIdCardDesc(AppConstants.IDCARD_LENGTH_SHOULD_BE_MORE_THAN_15_OR_18);
			return idCard;
		}

		if (idCardNumber.length() == 18) {
			Ai = idCardNumber.substring(0, 17);
			if (!isNumeric(Ai)) {
				idCard.setIdCardDesc(AppConstants.IDCARD_SHOULD_BE_17_DIGITS_EXCEPT_LASTONE);
				return idCard;
			}
		} else if (idCardNumber.length() == 15) {
			Ai = idCardNumber.substring(0, 6) + "19"
					+ idCardNumber.substring(6, 15);
			if (!isNumeric(Ai)) {
				idCard.setIdCardDesc(AppConstants.IDCARD_SHOULD_BE_15_DIGITS);
				return idCard;
			}
		}

		String strYear = Ai.substring(6, 10);// ���
		String strMonth = Ai.substring(10, 12);// �·�
		String strDay = Ai.substring(12, 14);// �·�
		String strBirthday = strYear + "-" + strMonth + "-" + strDay;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);// ����lenientΪfalse.
										// ����SimpleDateFormat��ȽϿ��ɵ���֤���ڣ�����2007/02/29�ᱻ���ܣ���ת����2007/03/01
		try {
			Date birthday = dateFormat.parse(strBirthday);
			Calendar calBirthday = Calendar.getInstance();
			calBirthday.setTime(birthday);
			if (calBirthday.after(Calendar.getInstance())) {// ������Ը��߾�������ֱ�ӽ��Ƿ�����������������ո�ɸѡ����
				idCard.setIdCardDesc(AppConstants.IDCARD_BIRTHDAY_SHOULD_NOT_LARGER_THAN_NOW);
				return idCard;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			idCard.setIdCardDesc(AppConstants.IDCARD_BIRTHDAY_IS_INVALID);
			return idCard;
		}

		int MAX_MAINLAND_AREACODE = 659004; // ��½��������������ֵ
		int MIN_MAINLAND_AREACODE = 110000; // ��½�������������Сֵ
		int HONGKONG_AREACODE = 810000; // ��۵������ֵ
		int TAIWAN_AREACODE = 710000; // ̨��������ֵ
		int MACAO_AREACODE = 820000; // ���ŵ������ֵ
		int areaCode = Integer.parseInt(Ai.substring(0, 6));

		if (areaCode != HONGKONG_AREACODE
				&& areaCode != TAIWAN_AREACODE
				&& areaCode != MACAO_AREACODE
				&& (areaCode > MAX_MAINLAND_AREACODE || areaCode < MIN_MAINLAND_AREACODE)) {
			idCard.setIdCardDesc(AppConstants.IDCARD_REGION_ENCODE_IS_INVALID);
			return idCard;
		}

		// �ж������18λ���֤���ж����һλ��ֵ�Ƿ�Ϸ�
		/*
		 * У��ļ��㷽ʽ�� ����1. ��ǰ17λ���ֱ������Ȩ��� ������ʽΪ��S = Sum(Ai * Wi), i = 0, ... , 16
		 * ��������Ai��ʾ��iλ���ϵ����֤��������ֵ��Wi��ʾ��iλ���ϵļ�Ȩ���ӣ����λ��Ӧ��ֵ����Ϊ�� 7 9 10 5 8 4 2 1 6
		 * 3 7 9 10 5 8 4 2 ����2. ��11�Լ�����ȡģ ����Y = mod(S, 11) ����3. ����ģ��ֵ�õ���Ӧ��У����
		 * ������Ӧ��ϵΪ�� ���� Yֵ�� 0 1 2 3 4 5 6 7 8 9 10 ����У���룺 1 0 X 9 8 7 6 5 4 3 2
		 */
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;
		if (idCardNumber.length() == 18) {
			if (!Ai.equals(idCardNumber)) {
				idCard.setIdCardDesc(AppConstants.IDCARD_IS_INVALID);
				return idCard;
			} else {
				idCard.setIdCardDesc(AppConstants.IDCARD_IS_VALID);
				return idCard;
			}
		} else if (idCardNumber.length() == 15) {
			idCard.setIdCardDesc(AppConstants.IDCARD_IS_VALID);
			return idCard;
		}

		idCard.setIdCardDesc(AppConstants.IDCARD_PARSER_ERROR);
		return idCard;
	}

	static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
}