package cybersignclient.testsignature;

import java.util.ArrayList;

public class TestJavaCore {

	class AB{
		public AB() {
			
		}
		String s;
		
	}
	
	public static void test(AB a, AB ba) {
		TestJavaCore te = new TestJavaCore();
		TestJavaCore.AB b = te.new AB();
		b.s = "2";
		a.s = "1";
		a = b;
		ba = a;
		
	}
	
	public static void main(String[] args) {
		TestJavaCore te = new TestJavaCore();
		TestJavaCore.AB a = te.new AB();
		AB b = null;
		String s = null;
		//a.s = "1";
		test(a,b);
		System.out.println(a.s + "  " + b);
		
	}
}
