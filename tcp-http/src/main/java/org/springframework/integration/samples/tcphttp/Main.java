package org.springframework.integration.samples.tcphttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.util.SocketUtils;

/**
 * 
 * TCPソケット通信でデータを受信して、 受信したデータをCustomOrderへデシリアライズし、
 * デシリアライズしたCustomOrderをJSONへ変換し、 JSONへ変換したデータをHTTPサーバへPOSTする。
 *
 * HTTPサーバからの応答をEchoServiceへ通した後に、TCPソケット通信で応答する。
 *
 * TCP要求 - HTTP要求応答 - TCP応答
 * 
 * 下記 Spring-Integration のデモの内、
 * https://github.com/spring-projects/spring-integration-samples
 * basic/tcp-client-server をベースに作成した。
 * 
 */
public final class Main {

	private static final String AVAILABLE_SERVER_SOCKET = "availableServerSocket";

	private Main() {
	}

	/**
	 * メイン処理
	 *
	 * @param args 使用しない
	 */
	public static void main(final String... args) {

		final Scanner scanner = new Scanner(System.in);

		final GenericXmlApplicationContext context = Main.setupContext();
		final SimpleGateway gateway = context.getBean(SimpleGateway.class);
		final AbstractServerConnectionFactory crLfServer = context.getBean(AbstractServerConnectionFactory.class);

		System.out.print("Waiting for server to accept connections...");
		TestingUtilities.waitListening(crLfServer, 10000L);
		System.out.println("running.\n\n");

		System.out.println("Please enter some text and press <enter>: ");
		System.out.println("\tNote:");
		System.out.println("\t- Entering FAIL will create an exception");
		System.out.println("\t- Entering q will quit the application");
		System.out.print("\n");
		System.out.println("\t--> Please also check out the other samples, " + "that are provided as JUnit tests.");
		System.out.println(
				"\t--> You can also connect to the server on port '" + crLfServer.getPort() + "' using Telnet.\n\n");

		while (true) {

			final String input = scanner.nextLine();

			if ("q".equals(input.trim())) {
				break;
			} else {
				final String result = gateway.send(input);
				System.out.println(result);
			}
		}
		
		scanner.close();

		System.out.println("Exiting application...bye.");
		System.exit(0);

	}

	public static GenericXmlApplicationContext setupContext() {
		final GenericXmlApplicationContext context = new GenericXmlApplicationContext();

		if (System.getProperty(AVAILABLE_SERVER_SOCKET) == null) {
			System.out.print("Detect open server socket...");
			int availableServerSocket = SocketUtils.findAvailableTcpPort(5678);

			final Map<String, Object> sockets = new HashMap<>();
			sockets.put(AVAILABLE_SERVER_SOCKET, availableServerSocket);

			final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

			context.getEnvironment().getPropertySources().addLast(propertySource);
		}

		System.out.println("using port " + context.getEnvironment().getProperty(AVAILABLE_SERVER_SOCKET));

		context.load("classpath:META-INF/spring/integration/tcp-http-context.xml");
		context.registerShutdownHook();
		context.refresh();

		return context;
	}

}
