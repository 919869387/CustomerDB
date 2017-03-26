package service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pojo.QuestionnaireSendStatistics;
import util.ExceptionQuestionnaireSendStatistics;
import dao.QuestionnaireSendStatisticsDao;

@Service
public class QuestionnaireSendStatisticsService {

	@Autowired
	QuestionnaireSendStatisticsDao questionnaireSendStatisticsDao;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月19日下午4:20:05
	 *
	 * 方法名：getCustomeridsTotalsendnumAndSuccesssendnumAndAnswernum
	 * 方法描述：得到一组消费者的totalsendnum、successsendnum、answernum相加和
	 */
	public JSONObject getCustomeridsTotalsendnumAndSuccesssendnumAndAnswernum(JSONArray customerids){
		JSONObject result = new JSONObject();
		int totalsendnum = 0;
		int successsendnum = 0;
		int answernum = 0;
		for(int i=0;i<customerids.size();i++){
			QuestionnaireSendStatistics questionnaireSendStatistics = getQuestionnaireSendStatisticsByCustomerId(customerids.getString(i));
			totalsendnum += questionnaireSendStatistics.getTotalsendnum();
			successsendnum += questionnaireSendStatistics.getSuccesssendnum();
			answernum += questionnaireSendStatistics.getAnswernum();
		}
		result.put("totalsendnum", totalsendnum);
		result.put("successsendnum", successsendnum);
		result.put("answernum", answernum);
		return result;
	}
	
	public boolean exist_insertQuestionnaireSendStatisticsByCustomerId(String customerid){

		if(!existQuestionnaireSendStatisticsByCustomerId(customerid)){
			//说明customerid不存在需要添加
			QuestionnaireSendStatistics questionnaireSendStatistics = new QuestionnaireSendStatistics();
			questionnaireSendStatistics.setCustomerid(customerid);
			if(!questionnaireSendStatisticsDao.insertQuestionnaireSendStatistics(questionnaireSendStatistics)){
				throw new ExceptionQuestionnaireSendStatistics("添加问卷发送情况customerid时发生异常");
			}
		}
		return true;
	}

	@Transactional
	public boolean updateQuestionnaireSendStatistics_ADDtotalsendnum(String customerid) {
		if(exist_insertQuestionnaireSendStatisticsByCustomerId(customerid)){  
			if(!questionnaireSendStatisticsDao.updateQuestionnaireSendStatistics_ADDtotalsendnum(customerid)){
				throw new ExceptionQuestionnaireSendStatistics("更新totalsendnum时发生异常");
			}
		}
		return true;
	}
	
	@Transactional
	public boolean updateQuestionnaireSendStatistics_ADDsuccesssendnum(String customerid) {
		if(exist_insertQuestionnaireSendStatisticsByCustomerId(customerid)){  
			if(!questionnaireSendStatisticsDao.updateQuestionnaireSendStatistics_ADDsuccesssendnum(customerid)){
				throw new ExceptionQuestionnaireSendStatistics("更新successsendnum时发生异常");
			}
		}
		return true;
	}
	
	@Transactional
	public boolean updateQuestionnaireSendStatistics_ADDanswernum(String customerid) {
		if(exist_insertQuestionnaireSendStatisticsByCustomerId(customerid)){  
			if(!questionnaireSendStatisticsDao.updateQuestionnaireSendStatistics_ADDanswernum(customerid)){
				throw new ExceptionQuestionnaireSendStatistics("更新answernum时发生异常");
			}
		}
		return true;
	}
	
	/*
	 * 根据customerid,找到QuestionnaireSendStatistics
	 */
	public QuestionnaireSendStatistics getQuestionnaireSendStatisticsByCustomerId(String customerid) {
		QuestionnaireSendStatistics questionnaireSendStatistics = new QuestionnaireSendStatistics();
		if(questionnaireSendStatisticsDao.existQuestionnaireSendStatisticsByCustomerId(customerid)){
			questionnaireSendStatistics = questionnaireSendStatisticsDao.getQuestionnaireSendStatisticsByCustomerId(customerid);
		}
		return questionnaireSendStatistics;
	}
	
	/*
	 * 判断customerid是否存在
	 */
	public boolean existQuestionnaireSendStatisticsByCustomerId(String customerid) {
		return questionnaireSendStatisticsDao.existQuestionnaireSendStatisticsByCustomerId(customerid);
	}

}
