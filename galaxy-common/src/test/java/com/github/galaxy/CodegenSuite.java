package com.github.galaxy;

import com.github.galaxy.common.core.codegen.PojoGenerator;
import com.github.galaxy.common.core.config.GeneratedTypeSuffix;
import com.github.galaxy.common.core.util.CodegenUtil;
import org.junit.Test;

public class CodegenSuite {


	@Test
	public void testCodegen() throws Exception {
		PojoGenerator generator = new PojoGenerator();
		generator.setTableName("users");
		generator.addField(new PojoGenerator.Field(String.class.getName(),"name"),"sandee");
		generator.addField(new PojoGenerator.Field(Integer.class.getName(),"age"),18);
		CodegenUtil.autoGenerateCode(GeneratedTypeSuffix.JAVA,"pojo.ftl",generator);
	}

}
