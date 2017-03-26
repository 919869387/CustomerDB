package test;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInput {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void dalete() {

		String sql = "delete from data_all where source=?";
		jdbcTemplate.update(sql, "4");

	}

	public void update() {

		String sql = "update data_all set source=? where source=?";
		jdbcTemplate.update(sql, new Object[] { "1", "1a" });
		jdbcTemplate.update(sql, new Object[] { "2", "2a" });
		jdbcTemplate.update(sql, new Object[] { "3", "3a" });
		jdbcTemplate.update(sql, new Object[] { "5", "4a" });
		jdbcTemplate.update(sql, new Object[] { "6", "5a" });
		jdbcTemplate.update(sql, new Object[] { "7", "6a" });
		jdbcTemplate.update(sql, new Object[] { "8", "7a" });
		jdbcTemplate.update(sql, new Object[] { "9", "8a" });
		jdbcTemplate.update(sql, new Object[] { "10", "9a" });
		jdbcTemplate.update(sql, new Object[] { "11", "10a" });
		jdbcTemplate.update(sql, new Object[] { "12", "11a" });
		jdbcTemplate.update(sql, new Object[] { "13", "12a" });
		jdbcTemplate.update(sql, new Object[] { "14", "13a" });
		jdbcTemplate.update(sql, new Object[] { "15", "14a" });
		jdbcTemplate.update(sql, new Object[] { "16", "15a" });
		jdbcTemplate.update(sql, new Object[] { "17", "16a" });
		jdbcTemplate.update(sql, new Object[] { "18", "17a" });

	}

	public void Input1() {

		String sql = "select * from datastore";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (Map map : list) {
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, map.get("source_id").toString(),
					map.get("record_date"), map.get("content"));
		}

	}

	public void Input2() {

		String sql = "select * from b";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("send_date", map.get("date_send"));
			jsonObject.put("shop_name", map.get("shop_name"));
			jsonObject.put("external_platform_number",
					map.get("external_platform_number"));
			jsonObject.put("purchaser_id", map.get("purchaser_id"));
			jsonObject.put("barcode", map.get("barcode"));
			jsonObject.put("brand_name", map.get("brand_name"));
			jsonObject.put("purchase_amount", map.get("purchase_amount"));
			jsonObject.put("unit", map.get("unit"));
			jsonObject.put("distribution_cost", map.get("distribution_cost"));
			jsonObject.put("total_price", map.get("total_price"));
			jsonObject.put("total_sales_price", map.get("total_sales_price"));
			jsonObject.put("send_company", map.get("send_company"));
			jsonObject.put("tracking_number", map.get("tracking_number"));
			jsonObject.put("receive_name", map.get("receive_name"));
			jsonObject.put("receive_telnumber", map.get("receive_telnumber"));
			jsonObject.put("address_province", map.get("address_province"));
			jsonObject.put("address_city", map.get("address_city"));
			jsonObject.put("address_county", map.get("address_county"));
			jsonObject.put("store_name", map.get("store_name"));
			jsonObject.put("order_number", map.get("order_number"));
			jsonObject.put("order_count", map.get("order_count"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "2", map.get("date_send"),
					jsonObject.toString());
		}

	}

	public void Input3_1() {

		String sql = "select * from c1";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("participation_date", map.get("participation_date"));
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("address_city", map.get("address_city"));
			jsonObject.put("address_detail", map.get("address_detail"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "3", map.get("participation_date"),
					jsonObject.toString());
		}

	}

	public void Input3_2() {

		String sql = "select * from c";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("participation_date", map.get("participation_date"));
			jsonObject.put("qq_number", map.get("qq_number"));
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("age", map.get("age"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("address_province", map.get("address_province"));
			jsonObject.put("address_city", map.get("address_city"));
			jsonObject.put("address_detail", map.get("address_detail"));
			String sqlcontent = "insert into data_all(source,record_date,content) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "4", map.get("participation_date"),
					jsonObject.toString());
		}

	}

	public void Input3_3() {

		String sql = "select * from c";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("address_detail", map.get("address_detail"));
			jsonObject.put("data_record_date", map.get("data_record_date"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "5", map.get("data_record_date"),
					jsonObject.toString());
		}

	}

	public void Input3_4() {

		String sql = "select * from c";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("data_record_date", map.get("data_record_date"));
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("address_city", map.get("address_city"));
			jsonObject.put("address_detail", map.get("address_detail"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "6", map.get("data_record_date"),
					jsonObject.toString());
		}

	}

	public void Input4() {

		String sql = "select * from d";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", map.get("name"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("address_city", map.get("address_city"));
			jsonObject.put("address_detail", map.get("address_detail"));
			jsonObject.put("data_record_date", map.get("data_record_date"));
			jsonObject.put("data_source_type", map.get("data_source_type"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "7", map.get("data_record_date"),
					jsonObject.toString());
		}

	}

	public void e4() {

		String sql = "select * from e4";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("age", map.get("age"));
			jsonObject.put("work_place_name", map.get("work_place_name"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("wechat_id", map.get("wechat_id"));
			jsonObject.put("qq_number", map.get("qq_number"));
			jsonObject.put("buy_150mlbottle_number",
					map.get("buy_150mlbottle_number"));
			jsonObject.put("buy_350mlbottle_number",
					map.get("buy_350mlbottle_number"));
			jsonObject.put("give_150mlbottle_number",
					map.get("give_150mlbottle_number"));
			jsonObject.put("give_350mlbottle_number",
					map.get("give_350mlbottle_number"));
			jsonObject.put("data_record_date", map.get("data_record_date"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "11", map.get("data_record_date"),
					jsonObject.toString());
		}

	}

	public void f3() {

		String sql = "select * from f3";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", map.get("name"));
			jsonObject.put("sex", map.get("sex"));
			jsonObject.put("age", map.get("age"));
			jsonObject.put("telnumber", map.get("telnumber"));
			jsonObject.put("hobby", map.get("hobby"));
			jsonObject.put("drinking_frequency_week",
					map.get("drinking_frequency_week"));
			String sqlcontent = "insert into ansdata(source,date_time,record) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, "14", "2015-05-05",
					jsonObject.toString());
		}

	}

	public void copy() {

		String sql = "select * from data_all";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String sqlcontent = "insert into datastore(record_date,content,source_id) values(?,?,?)";
			jdbcTemplate.update(sqlcontent, map.get("record_date"),
					map.get("content"), map.get("source_id"));
		}

	}
}
