package org.bambooframework.dictionary;

import java.util.List;
import java.util.Map;

import org.bambooframework.utils.TestBase;
import org.junit.Test;

public class SelModelTest extends TestBase {

	@Test
	public void test_getCodeList(){
		List<Map<String,String>> list = SelModelUtils.getCodeList("FUNC_STATUS", null);
		assertEquals(2, list.size());
		assertEquals("是", SelModelUtils.val2Text("tb:YESNO", "1"));
	}
}
