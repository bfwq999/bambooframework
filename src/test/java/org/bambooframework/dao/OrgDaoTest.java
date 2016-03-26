package org.bambooframework.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.entity.Org;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class OrgDaoTest extends TestBase {
	@Autowired
	IOrgDao orgDao;
	
	@Test
	public void test_crud(){
		Org org = new Org();
		org.setParentId("00001");
		org.setName("测试");
		org.setCode("123");
		orgDao.insert(org);
		
		assertEquals("000010001", org.getId());
		
		org = orgDao.get(org.getId());
		assertEquals("测试", org.getName());
		assertNotNull(org.getSort());
		
		org.setShortName("测试");
		orgDao.update(org);
	}
	
	@Test
	public void test_find(){
		List<Org> orgs = orgDao.find(null);
		assertEquals(3, orgs.size());
		System.out.println(orgs);
		
		Map<String,String> condition = new HashMap<String, String>();
		condition.put("name", "机构");
		orgs = orgDao.find(condition);
		assertEquals(2, orgs.size());
		System.out.println(orgs);
	}
	
	@Test
	public void test_insertOrgInThread() throws InterruptedException{
		//使用多线程测试
		List<Thread> threads = new ArrayList<Thread>();
		int count = 0;
		for(int i=0; i<30; i++){
			int c = 100+(int)Math.random()*100;
			count += c;
			Thread thread = new InsertOrgThread(c,"thread-"+i,"0");
			threads.add(thread);
			thread.start();
		}
		for(int i=0; i<30; i++){
			int c = 100+(int)Math.random()*100;
			count += c;
			Thread thread = new InsertOrgThread(c,"thread-"+i,"00001");
			threads.add(thread);
			thread.start();
		}
		for(int i=0; i<30; i++){
			int c = 100+(int)Math.random()*100;
			count += c;
			Thread thread = new InsertOrgThread(c,"thread-"+i,"00002");
			threads.add(thread);
			thread.start();
		}
		
		for(Thread thread:threads){
			thread.join();
		}
		System.out.println("主线程执行完成!");
	}
	
	private class InsertOrgThread extends Thread{

		private int count; 
		private String parentId;
		
		public InsertOrgThread(int count,String name,String parentId) {
			super();
			this.setName(name);
			this.count = count;
			this.parentId = parentId;
		}

		@Override
		public void run() {
			for(int i=0; i<count; i++){
				Org org = new Org();
				org.setParentId(parentId);
				org.setName("测试"+i);
				org.setCode(parentId+i);
				orgDao.insert(org);
				System.out.println(org.getId()+","+org.getParentId());
				try {
					this.sleep(100+(int)Math.random()*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.err.println(this.getName()+"结束!!");
		}
	}
	
	@Test
	@Transactional
	public void test_updateOrgSort(){
		Org org = new Org();
		org.setParentId("00001");
		org.setName("测试");
		org.setCode("123");
		orgDao.insert(org);
		
		orgDao.updateOrgSort(org.getId(), 1);
	}
}
