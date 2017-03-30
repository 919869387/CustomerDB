package controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.DataOutputService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/dataouput")
public class DataOuputController {
	@Autowired
	DataOutputService dataOutputService;

	@RequestMapping(value = "/customerDataOutputToExcel", method = RequestMethod.GET)
	@ResponseBody
	public String customerDataOutputToExcel(HttpSession session,HttpServletRequest request, HttpServletResponse response) {

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
				//将文件从服务器中删除
				file.delete();
			}
		}
		return null;
	}

}
