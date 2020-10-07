package pcap.api.jdk7;

import com.sun.jna.NativeLong;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class DefaultTimestampTest {

  @Test
  public void newInstance() {
    DefaultTimestamp timestamp = new DefaultTimestamp();
    timestamp.tv_sec = new NativeLong(1);
    timestamp.tv_usec = new NativeLong(1);
    timestamp.write();

    Assertions.assertEquals(1, timestamp.second());
    Assertions.assertEquals(1, timestamp.microSecond());
  }
}
