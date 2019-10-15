package pcap.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class PcapsTest {

  @Test
  public void versionTest() {
    Assertions.assertNotNull(Pcaps.version());
  }
}