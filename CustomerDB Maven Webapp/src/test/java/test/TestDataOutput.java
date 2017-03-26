package test;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.DataOutputService;
import service.ManageCustomerService;

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
//		File xlsFile = new File("jxl.xls");
//	      // 创建一个工作簿
//	      WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
//	      // 创建一个工作表
//	      WritableSheet sheet = workbook.createSheet("sheet1", 0);
//	      for (int row = 0; row < 10; row++)
//	      {
//	         for (int col = 0; col < 10; col++)
//	         {
//	            // 向工作表中添加数据
//	            sheet.addCell(new Label(col, row, "data" + row + col));
//	         }
//	      }
//	      workbook.write();
//	      workbook.close();
		JSONObject test = new JSONObject();
		test.put("[aa,dd]", 1);
		//getService().CustomerDataOutputToExcel();
		//getService().CustomerDataOutputToExcel();
	}
}
