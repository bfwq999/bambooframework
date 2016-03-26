package org.bambooframework.utils;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class TestControllerBase extends TestBase {

	@Autowired(required=true)
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;
	
	protected MockHttpSession session;

	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		this.session = new MockHttpSession();  
	}
}
