package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

//	@Test
//	public void QuertionDataOutputToExcel() throws Exception{
//		String filePath="";
//		JSONArray qids = new JSONArray();
//		qids.add(1653);
//		qids.add(1654);
//		getService().QuertionDataOutputToExcel(filePath, qids);
//	}
	
//	@Test
//	public void CustomerDataOutput() throws Exception{
//		
//		getService().CustomerDataOutputToExcel();
//	}

}
