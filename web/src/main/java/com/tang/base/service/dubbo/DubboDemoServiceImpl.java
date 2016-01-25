package com.tang.base.service.dubbo;

import com.tang.base.api.service.DubboDemoService;

public class DubboDemoServiceImpl implements DubboDemoService{

	public String sayHello(String userName) {
		String s = "hello "+userName+"!";
		System.out.println(s);
		return s;
	}

}
