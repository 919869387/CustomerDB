package controller;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.DataOutputService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/dataoutput")
public class DataOutputController {
	@Autowired
	DataOutputService dataOutputService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月30日下午5:46:57
	 * 
	 * 方法名：quertionDataOutputToExcel
	 * 方法描述：将问卷数据导出到excel中,不同的问卷写在不同的sheet中
	 */
	@RequestMapping(value = "/quertionDataOutputToExcel", method = RequestMethod.POST)
	@ResponseBody
	public String quertionDataOutputToExcel(@RequestBody Map<String, Object> map,HttpSession session,HttpServletResponse response) {

		JSONArray qids = JSONArray.fromObject(map.get("qids"));
		if(qids.size()==0){
			return null;
		}

		String filePath = session.getServletContext().getRealPath("/")+"tempFile\\";

		//生成文件
		File file = null;
		try {
			file = dataOutputService.QuertionDataOutputToExcel(filePath, qids);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//将文件下载
		if (file.exists()) {
			//将文件写到前端
			dataOutputService.outputWriteFile(file,response);
			//将文件从服务器中删除
			file.delete();
		}
		return null;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月30日下午5:46:40
	 * 
	 * 方法名：customerDataOutputToExcel
	 * 方法描述：将消费者全表数据导出到excel中
	 */
	@RequestMapping(value = "/customerDataOutputToExcel", method = RequestMethod.GET)
	@ResponseBody
	public String customerDataOutputToExcel(HttpSession session,HttpServletResponse response) {

		String filePath = session.getServletContext().getRealPath("/")+"tempFile\\";

		//生成文件
		File file = null;
		try {
			file = dataOutputService.CustomerDataOutputToExcel(filePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//将文件下载
		if (file.exists()) {
			//将文件写到前端
			dataOutputService.outputWriteFile(file,response);
			//将文件从服务器中删除
			file.delete();
		}
		return null;
	}

}
