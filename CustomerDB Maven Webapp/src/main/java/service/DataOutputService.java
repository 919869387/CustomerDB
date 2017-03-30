package service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.Customer;


@Service
public class DataOutputService {
	@Autowired
	TagTreeService tagTreeService;
	@Autowired
	CustomerDataService customerDataService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月29日下午4:26:49
	 * 
	 * 方法名：CustomerDataOutputToExcel
	 * 方法描述：将消费者全表数据导出到excel中
	 */
	public File CustomerDataOutputToExcel(String filePath) throws IOException, RowsExceededException, WriteException{
		//导出时间
		Date date = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateTime = dateFormat.format(date);

		String xlsName= dateTime;

		File xlsFile = new File(filePath+xlsName+".xls");
		// 创建一个工作簿
		WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
		// 创建一个工作表
		WritableSheet sheet = workbook.createSheet("全库消费者数据", 0);
		//写表头
		int stRow = 0;//行
		int stColumn = 0;//列
		Map<String,Integer> tagToColumn = new HashMap<String,Integer>();//这个Map用来记录标签与列号的对应关系，eg:{22:3}
		JSONArray customerDataTagTrees = tagTreeService.getCustomerDataTagTrees();
		writeTableHead(sheet, stColumn, stRow, customerDataTagTrees,tagToColumn);
		//写消费者数据
		int dataStRow = sheet.getRows();
		List<Customer> customers = customerDataService.getAllCustomerData();
		writeTableCustomerData(dataStRow, tagToColumn, customers, sheet);

		workbook.write();
		workbook.close();
		
		return xlsFile;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月24日下午12:35:28
	 *
	 * 方法名：writeTableCustomerData
	 * 方法描述：把消费者信息写入表中
	 */
	public void writeTableCustomerData(int dataStRow,Map<String,Integer> tagToColumn,List<Customer> customers,WritableSheet sheet) throws WriteException{
		for(Customer customer : customers){
			JSONObject customerInfo = JSONObject.fromObject(customer.getContent());
			Iterator it = customerInfo.keys();
			while (it.hasNext()) {
				String key = (String) it.next();  
				String value = customerInfo.getString(key);
				int column = tagToColumn.get(key);

				Label label = new Label(column, dataStRow, value);
				WritableCellFormat cellFormat = new WritableCellFormat();  
				cellFormat.setAlignment(jxl.format.Alignment.CENTRE);//设置居中
				sheet.addCell(label);
			}
			dataStRow++;
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月23日下午10:38:50
	 *
	 * 方法名：writeTableHead
	 * 方法描述：写Excel表头
	 */
	public void writeTableHead(WritableSheet sheet,int column,int row,JSONArray customerDataTagTrees,Map<String,Integer> tagToColumn) throws RowsExceededException, WriteException{

		for(int i=0;i<customerDataTagTrees.size();i++){
			JSONObject node = customerDataTagTrees.getJSONObject(i);
			int nodeWidth = writeNode(sheet, node, column, row,tagToColumn);
			if(node.containsKey("children")){
				int tempCol = column;
				int tempRow = row+1;
				writeTableHead(sheet, tempCol, tempRow, JSONArray.fromObject(node.getJSONArray("children")),tagToColumn);
			}
			column+=nodeWidth;
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月23日下午10:38:21
	 *
	 * 方法名：writeNode
	 * 方法描述：判断每个节点宽度,写入文件中
	 */
	public int writeNode(WritableSheet sheet,JSONObject node,int stColumn,int stRow,Map<String,Integer> tagToColumn) throws RowsExceededException, WriteException{
		int width = getTreeWidth(node);
		int high = 1;
		//合并宽度
		if(width>1){
			sheet.mergeCells(stColumn, stRow, stColumn+width-1, stRow+high-1);
		}

		Label label = new Label(stColumn, stRow, node.getString("name"));
		WritableCellFormat cellFormat = new WritableCellFormat();  
		cellFormat.setAlignment(jxl.format.Alignment.CENTRE);//设置居中
		//将叶节点的单元格设置颜色,并添加标签与列号的对应关系
		if(!node.containsKey("children")){
			cellFormat.setBackground(Colour.YELLOW);
			tagToColumn.put(node.getString("id"), stColumn);
		}
		label.setCellFormat(cellFormat); 

		sheet.addCell(label);
		return width;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月23日下午5:47:21
	 *
	 * 方法名：getTreeWidth
	 * 方法描述：得到树中父节点下叶子节点的个数(可以说是宽度)
	 */
	public int getTreeWidth(JSONObject tree){
		int[] width = {0};
		getTreeWidth(tree,width);
		return width[0];
	}
	public void getTreeWidth(JSONObject tree,int[] width){
		if(tree.containsKey("children")){
			JSONArray children = tree.getJSONArray("children");
			for(int i=0;i<children.size();i++){
				JSONObject node = children.getJSONObject(i);
				getTreeWidth(node,width);
			}
		}else{
			width[0]++;
		}
	}

}
