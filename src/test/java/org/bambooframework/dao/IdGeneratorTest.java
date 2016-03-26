package org.bambooframework.dao;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.common.IdGenerator;
import org.bambooframework.entity.Property;
import org.bambooframework.utils.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IdGeneratorTest extends TestBase {
	@Autowired
	IPropertyDao propertyDao;
	
	@Test
	public void test_getNextId(){
		for(int i=0; i<100; i++){
			int val = IdGenerator.getNextId();
			assertEquals(i+1, val);
		}
		
		Property property = propertyDao.get("nextid");
		assertEquals(101, Integer.parseInt(property.getValue()));
	}
	
	@Test
	public void test_getNextIdInThread() throws InterruptedException{
		//使用多线程测试
		List<Thread> threads = new ArrayList<Thread>();
		int count = 0;
		for(int i=0; i<100; i++){
			int c = 100+(int)Math.random()*100;
			count += c;
			Thread thread = new GetNextIdThread(c,"thread-"+i);
			threads.add(thread);
			thread.start();
		}
		
		for(Thread thread:threads){
			thread.join();
		}
		assertEquals(count+1, IdGenerator.getNextId().intValue());
		System.out.println("主线程执行完成!");
	}
	
	private class GetNextIdThread extends Thread{

		private int count; 
		
		public GetNextIdThread(int count,String name) {
			super();
			this.setName(name);
			this.count = count;
		}

		@Override
		public void run() {
			for(int i=0; i<count; i++){
				System.out.println(this.getName()+">>>>>>>>"+IdGenerator.getNextId());
				try {
					this.sleep(100+(int)Math.random()*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.err.println(this.getName()+"结束!!");
		}
	}
	
	
}
