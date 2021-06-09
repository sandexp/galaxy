package com.github.galaxy;


import com.github.galaxy.common.jdbc.SQLExecutor;
import com.github.galaxy.query.QueryClosure;
import com.github.galaxy.utils.TransformUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LineageAnalysisSuite {


	@Autowired
	private SQLExecutor executor;

	@Test
	public void testTableSimpleJoin() throws Exception {

		String sql="select " +
				"t.id,t.name " +
				"from student as t " +
				"inner join users as u " +
				"on t.id=u.id " +
				"where id<10";
		String sql2="select " +
				"t.id,t.name " +
				"from student as t " +
				",users as u " +
				"where id<10";

		String sql3="select " +
				"id,name " +
				"from student " +
				"where id<10";

		String sql5="SELECT `DepartmentId` AS `Id`, `Salary` AS `Salary`, `Name` AS `Employee`, DENSE_RANK() OVER (PARTITION BY `DepartmentId` ORDER BY `Salary` DESC) AS `ranknum`\n" +
				"FROM `Employee`";

		// get table info
	}

	@Test
	public void testTableSingleSubQuery() throws Exception{
		String sql4=
				"select\n" +
				"    d.`Name` as Department,\n" +
				"    t.Employee as Employee,\n" +
				"    t.Salary as Salary\n" +
				"from\n" +
				"    (\n" +
				"        select\n" +
				"            DepartmentId as Id,\n" +
				"            Salary as Salary,\n" +
				"            `Name` as Employee,\n" +
				"            dense_rank() over(partition by DepartmentId order by Salary desc) as ranknum\n" +
				"        from\n" +
				"            Employee\n" +
				"    ) as t"
		;
	}

	@Test
	public void testTableSubJoinQuery() throws Exception {
		String sql4=
			"select\n" +
			"    d.`Name` as Department,\n" +
			"    t.Employee as Employee,\n" +
			"    t.Salary as Salary\n" +
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

		QueryClosure closure = new QueryClosure(sql4, TransformUtil.md5(sql4));

		closure.parseTableInfo(executor);
	}

	@Test
	public void testFieldSubJoinQuery() throws Exception {
		String sql4=
			"select\n" +
			"    d.`Name` as Department,\n" +
			"    t.Employee as Employee,\n" +
			"    dense_rank() over(partition by d.`Name` order by d.`Age` desc) as Salary\n" +
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
		QueryClosure closure = new QueryClosure(sql4, TransformUtil.md5(sql4));
		closure.parseTableInfo(executor);
		closure.parseFields(executor);

	}




}

