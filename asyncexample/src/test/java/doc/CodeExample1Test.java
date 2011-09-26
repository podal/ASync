package doc;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class CodeExample1Test {
	@Test
	public void testExample() throws IOException, InterruptedException {
		CodeExample1 codeExample1 = new CodeExample1();
		codeExample1.example();
		byte[] inB = new byte[codeExample1.inLen];
		System.arraycopy(codeExample1.inBytes, 0, inB, 0, codeExample1.inLen);
		Assert.assertEquals("cnySA", new String(inB));

	}
	public static void main(String[] args) {
		new CodeExample1().startReverseConsole();
	}
}
