package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RunDateInput {

	@Test
	public void RunInput() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring.xml");
		DataInput dataInput = (DataInput) context.getBean("dataInput");
		dataInput.Input1();
	}

}
