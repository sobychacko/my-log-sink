package foo.bar;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class MyLogSinkApplicationTests {

	@Autowired
	private Sink sink;

	@Autowired
	@Qualifier("myLogSinkHandler")
	private LoggingHandler loggingHandler;

	@Test
	public void testMessagesLogged() {
		assertNotNull(this.sink.input());
		assertEquals(LoggingHandler.Level.INFO, this.loggingHandler.getLevel());

		DirectFieldAccessor accessor = new DirectFieldAccessor(loggingHandler);
		Log log = (Log) accessor.getPropertyValue("messageLogger");
		log = spy(log);
		accessor.setPropertyValue("messageLogger", log);

		GenericMessage<String> message = new GenericMessage<>("foo");
		this.sink.input().send(message);

		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
		verify(log).info(captor.capture());
		assertEquals("foo", captor.getValue());
	}
}
