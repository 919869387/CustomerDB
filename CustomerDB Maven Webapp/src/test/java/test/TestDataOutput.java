package test;

import java.io.IOException;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.DataOutputService;

public class TestDataOutput {
	
	public DataOutputService getService() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring.xml");
		DataOutputService service = (DataOutputService) context
				.getBean("dataOutputService");
		return service;
	}
	
	@Test
	public void CustomerDataOutput() throws IOException, RowsExceededException, WriteException{

		getService().CustomerDataOutputToExcel();
	}
}
