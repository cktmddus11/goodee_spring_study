package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;
import logic.User;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
@Repository // @Component + dao  기능 
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Item> mapper = 
			new BeanPropertyRowMapper<Item>(Item.class);
	// mapper : item형태로 리턴값을 보낼수있음, 자동으로 빈 클래스를 채워줌
	private Map<String, Object> param = new HashMap<String, Object>();
	
	@Autowired // 내 컨테이너 안에서 자료형이 DataSource 인 객체를 주입해
	public void setDataSource(DataSource dataSource) { // spring-db.xml에 생성된 dataSource객체 주입
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Item> list() {
		return template.query("select *From item order by id", mapper); // Item 행태의 리스트로 리턴
		
	}

	public void insert(Item item) {
		param.clear();
		// id ; 등록된 id의 최대값
		int id=template.queryForObject("select ifnull(max(id), 0) from item", param, Integer.class);
		item.setId(++id+"");
		String sql = "insert into item (id, name, price, description, pictureUrl) values "
				+"(:id, :name, :price, :description, :pictureUrl)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql,  proparam);
		
	}

	public Item detailView(String id) {
		param.clear();
		param.put("id",  id);
		return template.queryForObject("select * from item where id=:id", param, mapper);
		
	}

	public void update(Item item) {
		String sql = "update item set name = :name, price = :price, "
				+ "description = :description, pictureUrl = :pictureUrl"
				+ " where id = :id";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql, proparam);
		
	}

	public void delete(int id) {
		String sql = "delete from item where id =:id";
		param.clear();
		param.put("id",  id);
		template.update(sql,  param);
		
	}

	
}
