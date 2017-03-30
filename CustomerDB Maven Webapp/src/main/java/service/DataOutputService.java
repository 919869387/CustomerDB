package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import pojo.QuestionData;
import pojo.TagTree;


@Service
public class DataOutputService {
	@Autowired
	TagTreeService tagTreeService;
	@Autowired
	CustomerDataService customerDataService;
	@Autowired
	QuestionDataService questionDataService;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月30日下午5:29:44
	 * 
	 * 方法名：QuertionDataOutputToExcel
	 * 方法描述：将问卷数据导出到excel中,不同的问卷写在不同的sheet中
	 */
	public File QuertionDataOutputToExcel(String filePath,JSONArray qids) throws IOException, RowsExceededException, WriteException{
		//导出时间
		Date date = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateTime = dateFormat.format(date);

		File xlsFile = new File(filePath+dateTime+".xls");
		// 创建一个工作簿
		WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
		for(int i=0;i<qids.size();i++){
			int qid = (int) qids.get(i);
			TagTree tagtree = tagTreeService.getTagTree(qid);
			// 创建一个工作表
			WritableSheet sheet = workbook.createSheet(tagtree.getQname(), i);
			//写表头
			int stRow = 0;//行
			int stColumn = 0;//列
			Map<String,Integer> tagToColumn = new HashMap<String,Integer>();//这个Map用来记录标签与列号的对应关系，eg:{22:3}
			JSONArray questionTagTree = JSONArray.fromObject(tagtree.getTree());
			writeTableHead(sheet, stColumn, stRow, questionTagTree,tagToColumn);
			//写消费者数据
			int dataStRow = sheet.getRows();
			List<QuestionData> questionDatas = questionDataService.getQuestionDatasByQid(qid);
			writeTableQuestionData(dataStRow, tagToColumn, questionDatas, sheet);
		}
		
		workbook.write();
		workbook.close();
		return xlsFile;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月30日下午5:30:28
	 * 
	 * 方法名：writeTableQuestionData
	 * 方法描述：把问卷数据写入表中
	 */
	public void writeTableQuestionData(int dataStRow,Map<String,Integer> tagToColumn,List<QuestionData> questionDatas,WritableSheet sheet) throws WriteException{
		for(QuestionData questionData : questionDatas){
			JSONObject dataInfo = JSONObject.fromObject(questionData.getData());
			Iterator it = dataInfo.keys();
			while (it.hasNext()) {
				String key = (String) it.next();  
				String value = "";
				JSONArray valueArr = JSONArray.fromObject(dataInfo.getString(key));
				for(int i=0;i<valueArr.size();i++){
					if(value.length()==0){
						value += valueArr.get(i);
					}else{
						value += ":"+valueArr.get(i);
					}
				}
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

		File xlsFile = new File(filePath+dateTime+".xls");
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

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月30日下午5:44:13
	 * 
	 * 方法名：outputWriteFile
	 * 方法描述：将文件输出到前端
	 */
	public void outputWriteFile(File file,HttpServletResponse response){
		response.setContentType("application/force-download");//设置强制下载不打开
		response.addHeader("Content-Disposition","attachment;fileName=" + file.getName());// 设置文件名
		byte[] buffer = new byte[1024];
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			OutputStream os = response.getOutputStream();
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
