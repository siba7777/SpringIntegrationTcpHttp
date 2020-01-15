
package org.springframework.integration.samples.tcphttp;

public class EchoService {

	public String test(String input) {
		return "echo:" + input;
	}

}
