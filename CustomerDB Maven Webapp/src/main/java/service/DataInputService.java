package service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pojo.QuestionData;
import pojo.Tag;
import pojo.TagTree;
import util.ExceptionCustomerData;
import util.ExceptionQuestionData;
import util.ExceptionService;
import util.ExceptionTagStore;
import util.ExceptionTagTree;
import util.ExceptionTagValue; 
import util.ToolCreateCustomerId;
import util.ToolGlobalParams;

@Service
public class DataInputService {

	@Autowired
	TagTreeService tagTreeService;
	@Autowired
	TagValueService tagValueService;
	@Autowired
	QuestionDataService questionDataService;
	@Autowired
	CustomerDataService customerDataService;
	@Autowired
	TagStoreService tagStoreService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午9:26:14
	 *
	 * 方法名：deleteAllInfoByQid
	 * 方法描述：根据qid删除TagTree、TagValue、QuestionData、CustomerData所有相关的数据
	 */
	@Transactional
	public boolean deleteAllInfoByQid(int qid){
		if(tagTreeService.existQid(qid)){
			TagTree tagTree = tagTreeService.getTagTree(qid);
			if(tagTree.getRecordcount()!=0){
				if(deleteQuestionDataAndCustomerDataByQid(qid)){
					if(deleteTagValueAndTagTreeByQid(qid)){
						return true;
					}else{
						throw new ExceptionService("删除TagValueAndTagTree时失败");
					}
				}else{
					throw new ExceptionService("删除QuestionDataAndCustomerData时失败");
				}
			}else{
				if(deleteTagValueAndTagTreeByQid(qid)){
					return true;
				}else{
					throw new ExceptionService("删除TagValueAndTagTree时失败");
				}
			}
		}else{
			return true;
		}
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午9:29:18
	 *
	 * 方法名：deleteQuestionDataAndCustomerDataByQid
	 * 方法描述：根据qid删除该问卷的数据
	 * 
	 * 步骤：1.根据qid得到这个问卷对应的所有customerid,再得到时间戳,这样可以删除CustomerData表里的数据
	 * 	   2.根据qid删除QuestionData里面的数据
	 */
	@Transactional
	public boolean deleteQuestionDataAndCustomerDataByQid(int qid){
		//1.根据qid得到这个问卷对应的所有customerid,再得到时间戳,这样可以删除CustomerData表里的数据
		List<String> customerids = questionDataService.getCustomeridsByQid(qid);
		String recordtime = tagTreeService.getTagTree(qid).getRecordtime().toString();
		if(customerDataService.daleteValueByCustomeridsAndRecordtime(customerids, recordtime)){
			//2.根据qid删除QuestionData里面的数据
			if(questionDataService.deleteQuestionDatasByQid(qid)){
				return true;
			}else{
				throw new ExceptionQuestionData("删除QuestionData数据失败!");
			}
		}else{
			throw new ExceptionCustomerData("删除CustomerData数据失败"); 
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午10:54:01
	 *
	 * 方法名：deleteTagValueAndTagTreeByQid
	 * 方法描述：
	 * 
	 * 步骤：1.根据qid删除tagvalue里面的数据
	 * 	   2.同时对tagstore中使用的标签使用次数-1
	 * 	   3.根据qid删除tagtree里面的记录
	 */
	@Transactional
	public boolean deleteTagValueAndTagTreeByQid(int qid){
		//1.根据qid删除tagvalue里面的数据
		if(tagValueService.deleteTagValueByQid(qid)){
			//2.同时对使用的标签使用次数-1
			List<Tag> tagList = tagTreeService.getTagListByQid(qid);
			if(tagStoreService.batchUpdateBeused_timesSub(tagList)){
				//3.根据qid删除tagtree里面的记录
				if(tagTreeService.deleteTagTreeByQid(qid)){
					return true;
				}else{
					throw new ExceptionTagTree("删除标签树记录时失败！");
				}
			}else{
				throw new ExceptionTagStore("标签使用次数减一时失败！");
			}
		}else{
			throw new ExceptionTagValue("删除标签值时失败！");
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午7:08:57
	 *
	 * 方法名：InputQInfoAndRelations
	 * 方法描述：插入问卷id,问卷名,以及根据对应关系生成问卷的标签树(含标签值)
	 * 
	 * 步骤：1.删除所有信息消费者数据、标签树等信息
	 * 	   2.根据relations得到问卷的标签树，将qid、qname、标签树、时间戳 放入标签树表中
	 * 	   3.对tagstore中使用了的标签使用次数+1
	 *	   4.根据relations得到radio标签的值，将tagid、qid、标签值 放入标签值表中
	 *		
	 *	   relations：[{tagId：int,option:["",""]},{tagId:int,option:[]}]
	 */
	@Transactional
	public boolean InputQInfoAndRelations(int qid,String qname,JSONArray relations){
		if(deleteAllInfoByQid(qid)){
			//生成时间戳
			Date date = new Date(); 
			Timestamp recordtime = new Timestamp(date.getTime());

			//1.将根据relations生成好的标签树插入表中
			if(tagTreeService.insertTagTree(qid, qname, relations, recordtime)){
				//2.得到标签树中所有的标签，将它们的使用次数+1
				List<Tag> tagList = tagTreeService.getTagListByQid(qid);
				if(tagStoreService.batchUpdateBeused_timesAdd(tagList)){
					//3.根据relations,将标签值插入到表中
					if(tagValueService.batchInsertTagValue(qid, relations)){
						return true;
					}else{
						throw new ExceptionTagValue("插入标签值时失败！");
					}
				}else{
					throw new ExceptionTagStore("标签使用次数加一时失败！");
				}
			}else{
				throw new ExceptionTagTree("插入标签树时失败！");
			}
		}
		return false;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月27日上午10:59:11
	 *
	 * 方法名：InputQuertionDataAndCustomerData
	 * 方法描述： 将问卷数据导入quertiondata与customerdata
	 *
	 * 步骤：1.将data数据放入quertiondata
	 * 	   2.将data数据放入customerdata表中
	 *	   3.修改tagtree表中该问卷对应的RecordCount值
	 * 
	 * qid:int
	 * sampleData：[]
	 * 其中sampleData的格式 key：value  eg.[{"51": ["2万","5万"],"23": ["男"],"customerId":["xxxx"]},{"51": ["7万","9万"],"23": ["女"],"customerId":["xxxx"]}]
	 * 
	 */
	@Transactional
	public int InputQuertionDataAndCustomerData(int qid,JSONArray sampleData_arr){
		int insertRecordCount = 0;
		if(tagTreeService.existQid(qid)){
			TagTree tagtree = tagTreeService.getTagTree(qid);
			String recordtime = tagtree.getRecordtime().toString();//只要是同一个qid,使用的recordtime都相同

			if(sampleData_arr.size() != 0){
				//说明从问卷数据库中获取到了数据
				for(int i=0;i<sampleData_arr.size();i++){
					//检查问卷数据以及CustomerData表中是否有customerid
					if(existCustomerIdInSampleDataAndCustomerData(sampleData_arr.getJSONObject(i))){
						//如果有customerid
						QuestionData questionData = createQuestionDataPoJoHaveCustomerId(sampleData_arr.getJSONObject(i), qid);
						//1.将记录放入QuestionData表中
						if(questionDataService.insertQuestionData(questionData)){
							//2.将记录放入Customerdata表中
							if(customerDataService.updateQuestionDataToCustomer(questionData, recordtime)){
								insertRecordCount++;
							}else{
								throw new ExceptionCustomerData("向CustomerData插入数据失败"); 
							}
						}else{
							throw new ExceptionQuestionData("向QuestionData插入数据失败!");
						}
					}else{
						//如果没有customerid
						QuestionData questionData = createQuestionDataPoJoNoCustomerId(sampleData_arr.getJSONObject(i), qid);
						//1.将记录放入QuestionData表中
						if(questionDataService.insertQuestionData(questionData)){
							//2.将记录放入Customerdata表中
							if(customerDataService.insertQuestionDataToCustomer(questionData, recordtime)){
								insertRecordCount++;
							}else{
								throw new ExceptionCustomerData("向CustomerData插入数据失败"); 
							}
						}else{
							throw new ExceptionQuestionData("向QuestionData插入数据失败!");
						}
					}
				}
			}
			//3.修改tagtree表中该问卷对应的RecordCount值
			if(!tagTreeService.updateRecordCountByQid(qid,insertRecordCount)){
				throw new ExceptionTagTree("修改RecordCount时失败！ ");
			}
		}
		return insertRecordCount;
	}

	//	/**
	//	 * 弃用，但不删
	//	 * 
	//	 * 作者：杨潇
	//	 * 创建时间：2017年3月1日下午4:24:46
	//	 *
	//	 * 方法名：getQuestionDataByQequest
	//	 * 方法描述：向问卷系统发送请求，得到问卷数据
	//	 */
	//	public JSONObject getQuestionDataByQequest(String url,int qid,int inputSign){
	//		//根据qid与inputSign发送请求获取数据
	//		String param = "qnaireId="+ qid +"&lastAnsSheetId="+inputSign;
	//		String QuestionData= ToolDownloadQuestionDataToCustomerDB.sendGetQequest(url, param);
	//
	//		return JSONObject.fromObject(QuestionData);
	//	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午5:45:25
	 *
	 * 方法名：existCustomerIdInSampleDataAndCustomerData
	 * 方法描述：检测问卷数据中是否有customerid,有customerid时,是否在customerdata表中存在
	 */
	public boolean existCustomerIdInSampleDataAndCustomerData(JSONObject data){
		boolean result = false;
		JSONArray customeridArr = data.getJSONArray("customerId");//得到每条记录的customerId,从问卷过来的customerId是这样
		if(customeridArr.size()==1){
			if(customerDataService.existCustomerByCustomerid(customeridArr.getString(0).trim())){
				result = true;
			}
		}
		return result;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午5:50:16
	 *
	 * 方法名：createQuestionDataPoJo
	 * 方法描述：构造QuestionData对象
	 * 
	 * 1.从数据JSON中得到customerid
	 * 2.从数据JSON中删除customerid字段
	 * 3.构造QuestionData对象
	 */
	public QuestionData createQuestionDataPoJoHaveCustomerId(JSONObject data,int qid){
		String customerid = data.getJSONArray("customerId").getString(0).trim();//得到每条记录的customerid
		data.remove("customerId");//将每条记录的customerid键值对删除

		QuestionData questionData = new QuestionData();
		questionData.setData(data.toString());
		questionData.setCustomerid(customerid);
		questionData.setQid(qid);

		return questionData;
	}

	public QuestionData createQuestionDataPoJoNoCustomerId(JSONObject data,int qid){
		data.remove("customerId");//将每条记录的customerid键值对删除

		String customerid = ToolCreateCustomerId.createCustomerId(ToolGlobalParams.companyNumber_JinPai);

		QuestionData questionData = new QuestionData();

		if(data.containsKey(ToolGlobalParams.telnumberTagId)){
			if(!data.containsKey(ToolGlobalParams.nameTagId)){
				JSONArray name = new JSONArray();
				name.add(ToolGlobalParams.name);
				data.accumulate(ToolGlobalParams.nameTagId, name);
			}
		}else{
			questionData.setIntegrated(false);
		}
		
		questionData.setData(data.toString());
		questionData.setCustomerid(customerid);
		questionData.setQid(qid);

		return questionData;
	}


}
