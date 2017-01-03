package gash.project.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class SmallSizeWriteTest {

	@Test
	public void test() {
		new OneWriteRequest().test();
	}

}
