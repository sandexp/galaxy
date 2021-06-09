package com.github.galaxy;

import com.github.galaxy.common.jdbc.SQLBuilder;
import org.junit.Test;

/**
 * DSL语句测试
 */
public class DSLSuite {


	@Test
	public void testInsert(){
		String sql = new SQLBuilder()
			.INSERT_INTO("students")
			.INSERT_COLUMNS("id,name,age")
			.INSERT_VALUE("23,\"sandee\",18")
			.build();
		System.out.println(sql);
	}

	@Test
	public void testDelete(){

	}

	@Test
	public void testUpdate(){
		String sql = new SQLBuilder()
			.UPDATE("students")
			.SET("id", "17")
			.WHERE()
			.appendAttribute("id")
			.EQUAL()
			.appendAttribute("23")
			.build();
		System.out.println(sql);
	}

	@Test
	public void testQuery(){
		String sql = new SQLBuilder()
			.SELECT("*")
			.FROM("students")
			.WHERE()
			.appendAttribute("id")
			.LESS()
			.appendAttribute("20")
			.build();
		System.out.println(sql);
	}


	@Test
	public void testInnerJoin(){
		String sql = new SQLBuilder()
			.SELECT("s.*")
			.FROM("students")
			.AS("s")
			.INNER_JOIN("user")
			.AS("u")
			.ON()
			.appendAttribute("s.id")
			.EQUAL()
			.appendAttribute("u.id")
			.build();
		System.out.println(sql);
	}

	// 年龄排序
	@Test
	public void testSort(){
		String sql = new SQLBuilder()
			.SELECT("id,name,age")
			.FROM("students")
			.ORDER_BY("age")
			.DESC()
			.build();
		System.out.println(sql);
	}

	@Test
	public void testGroup(){
		String sql = new SQLBuilder()
			.SELECT("age, count(id) as cnt")
			.FROM("students")
			.GROUP_BY("age")
			.build();
		System.out.println(sql);
	}

}
