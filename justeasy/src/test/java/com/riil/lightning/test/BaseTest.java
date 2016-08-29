package com.stone.lightning.test;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class BaseTest {

	protected static ApplicationContext ctx;

	@BeforeClass
	public static void init() {
		if (ctx == null) {
			ctx = new FileSystemXmlApplicationContext("classpath*:/spring-context*.xml");
		}
	}
}
