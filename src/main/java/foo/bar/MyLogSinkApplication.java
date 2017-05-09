package foo.bar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.LoggingHandler;

@SpringBootApplication
public class MyLogSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyLogSinkApplication.class, args);
	}

	@EnableBinding(Sink.class)
	static class MyLogSinkConfiguration {

		@Bean
		@ServiceActivator(inputChannel = Sink.INPUT)
		public LoggingHandler myLogSinkHandler() {
			LoggingHandler loggingHandler = new LoggingHandler(LoggingHandler.Level.INFO);
			loggingHandler.setLogExpressionString("payload");
			loggingHandler.setLoggerName("my-log-sink");
			return loggingHandler;
		}

	}

}
