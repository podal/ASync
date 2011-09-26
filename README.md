#ASync IO
ASync is asyncronius library to deal with asyncrone
communication: Socket, Console and Web.

##Get starting
add following in your pom.xml
	<project>
	...
		<dependencies>
			...
			<dependency>
				<groupId>com.github.podal.async</groupId>
				<artifactId>asyncdefault</artifactId>
				<version>1.0.0</version>
			</dependency>
			...
		</dependencies
	</project>

and

	ASync async = new ASync();
	Dispatcher disp = async.createDispatcher();
	async.console().start(disp.createCallback());
	async.socket().connectTo("127.0.0.1", 12345, disp.createCallback(new DoExit("Server did close!")));

and you have a connection to a socket that takes input and output from console.

See JavaDoc in net.async.ASync for full doc.