package test;

import util.ExceptionTagStore;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

public class TestException {

	public int testExcp() {
		System.out.println("11111");
		// return 1;
		throw new ExceptionTagStore("异常");

	}

}
