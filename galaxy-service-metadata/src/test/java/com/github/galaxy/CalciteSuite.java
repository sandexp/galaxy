package com.github.galaxy;

import com.github.galaxy.annotation.Available;
import com.github.galaxy.query.SqlNodeBuilder;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.Test;

public class CalciteSuite {

	@Available
	@Test
	public void testSimpleSelectList() throws SqlParseException {
		String sql4=
			"select\n" +
			"    d.`Name` as Department,\n" +
			"    (select count(1) from employee),\n" +
			"    sum(if(t.Salary is null,0,t.Salary)) as Salary\n" +
			"from\n" +
			"    (\n" +
			"        select\n" +
			"            DepartmentId as Id,\n" +
			"            Salary as Salary,\n" +
			"            `Name` as Employee,\n" +
			"            dense_rank() over(partition by DepartmentId order by Salary desc) as ranknum\n" +
			"        from\n" +
			"            Employee\n" +
			"    ) as t,\n" +
			"    Department as d\n" +
			"where\n" +
			"    t.Id=d.Id\n" +
			"and \n" +
			"    t.ranknum in (1,2,3)\n"
		;
		String sql5="select if(d.`name` is not null) as a from employee";
		SqlNodeBuilder builder = new SqlNodeBuilder(sql5);
		final SqlNodeList build = (SqlNodeList) builder.selectList().build();
		System.out.println(build.get(0).getKind());
		final SqlCall call = (SqlCall) build.get(0);
		System.out.println(call.getOperandList().get(0));
		SqlBasicCall call1= (SqlBasicCall) call.getOperandList().get(0);
		System.out.println(call1.getOperandList().get(0).getKind());
	}


	@Available
	@Test
	public void testWindowFunc() throws Exception {
		String sql="select dense_rank() over(partition by d.`Name` order by d.`Age` desc) as a from employee";
		SqlNodeBuilder builder = new SqlNodeBuilder(sql);
		SqlNodeList node = (SqlNodeList) builder.selectList().build();
		SqlCall call= (SqlCall) node.get(0);
		SqlCall call1 = (SqlCall) call.getOperandList().get(0);
		System.out.println("Call1 --> "+call1.getKind());

		System.out.println("---------------------");
		for (int i = 0; i < call1.getOperandList().size(); i++) {
			System.out.println(call1.getOperandList().get(i) + "\t -> \t"+call1.getOperandList().get(i).getKind());
		}
		SqlCall call2 = (SqlCall) call1.getOperandList().get(1);
		System.out.println(call1.getKind());

		for (int i = 0; i < call2.getOperandList().size(); i++) {
			System.out.println(call2.getOperandList().get(i));
		}
	}

	@Test
	public void testInsertSelect() throws Exception {
		String sql="insert into lowers select name,age from students ";
		SqlNodeBuilder builder=new SqlNodeBuilder(sql);
		SqlNode node = builder.insertSource().build();
		System.out.println(node);
	}
}
