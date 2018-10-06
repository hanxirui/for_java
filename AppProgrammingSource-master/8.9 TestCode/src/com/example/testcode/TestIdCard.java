package com.example.testcode;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestIdCard extends TestCase {

	public void testIdCard() throws Exception {		
		// ���Գ���Ϊ0��������Ϊ�յ����
		Assert.assertEquals(AppConstants.IDCARD_LENGTH_SHOULD_NOT_BE_NULL,
				Utils.isIdCardNumberValid("").getIdCardDesc());
		Assert.assertEquals(AppConstants.IDCARD_LENGTH_SHOULD_NOT_BE_NULL,
				Utils.isIdCardNumberValid(null).getIdCardDesc());

		// ���Գ��Ȳ�Ϊ15����18�����
		StringBuilder idCard = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			idCard.append("1");

			if (idCard.length() == 15 || idCard.length() == 18)
				continue;

			Assert.assertEquals(
					AppConstants.IDCARD_LENGTH_SHOULD_BE_MORE_THAN_15_OR_18,
					Utils.isIdCardNumberValid(idCard.toString()).getIdCardDesc());
		}

		// ����18λ���֤��ǰ17λ����Ϊ���ֵ����
		String idCardNO = "1234567890123456XX";
		Assert.assertEquals(
				AppConstants.IDCARD_SHOULD_BE_17_DIGITS_EXCEPT_LASTONE,
				Utils.isIdCardNumberValid(idCardNO).getIdCardDesc());

		// ����15λ���֤������ȫ��Ϊ���ֵ����
		String idCardNO2 = "12345678901234X";
		Assert.assertEquals(AppConstants.IDCARD_SHOULD_BE_15_DIGITS,
				Utils.isIdCardNumberValid(idCardNO2).getIdCardDesc());

		// ����18λ���֤�����ղ��ܴ��ڵ�ǰ����
		String realCardNO = "120107201501142413";
		Assert.assertEquals(
				AppConstants.IDCARD_BIRTHDAY_SHOULD_NOT_LARGER_THAN_NOW,
				Utils.isIdCardNumberValid(realCardNO).getIdCardDesc());

		// ����18λ���֤�����ձ�������Ч��
		String realCardNO2 = "120107198201322413";
		Assert.assertEquals(AppConstants.IDCARD_BIRTHDAY_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO2).getIdCardDesc());

		// ����18λ���֤��ǰ6λ���ܴ���659004������С��110000
		String realCardNO5 = "659005198201142413";
		Assert.assertEquals(AppConstants.IDCARD_REGION_ENCODE_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO5).getIdCardDesc());
		String realCardNO6 = "100000198201142413";
		Assert.assertEquals(AppConstants.IDCARD_REGION_ENCODE_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO6).getIdCardDesc());

		// ����15λ���֤�����ձ�������Ч��
		String realCardNO4 = "422823051232022";
		Assert.assertEquals(AppConstants.IDCARD_BIRTHDAY_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO4).getIdCardDesc());

		// ����15λ���֤��ǰ6λ���ܴ���659004������С��110000
		String realCardNO7 = "659005051230022";
		Assert.assertEquals(AppConstants.IDCARD_REGION_ENCODE_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO7).getIdCardDesc());
		String realCardNO8 = "100000051230022";
		Assert.assertEquals(AppConstants.IDCARD_REGION_ENCODE_IS_INVALID,
				Utils.isIdCardNumberValid(realCardNO8).getIdCardDesc());

		// 15λ���֤�����տ϶�С�ڵ�ǰ���ڣ�����19xx��ģ�
		String realCardNO3 = "422823151202022";
		Assert.assertEquals(AppConstants.IDCARD_IS_VALID,
				Utils.isIdCardNumberValid(realCardNO3).getIdCardDesc());

		
		//����18λ���֤���һλ�Ƿ�Ϸ�
		Assert.assertEquals(AppConstants.IDCARD_IS_INVALID,
				Utils.isIdCardNumberValid("110000191501010000").getIdCardDesc());
		Assert.assertEquals(AppConstants.IDCARD_IS_VALID,
				Utils.isIdCardNumberValid("120107198201142413").getIdCardDesc());

	}

}
