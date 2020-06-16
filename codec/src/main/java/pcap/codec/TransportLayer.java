/** This code is licenced under the GPL version 2. */
package pcap.codec;

import pcap.common.annotation.Inclubating;
import pcap.common.memory.Memory;
import pcap.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

/** @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a> */
@Inclubating
public final class TransportLayer extends NamedNumber<Byte, TransportLayer> {

  public static final TransportLayer ICMP =
      new TransportLayer((byte) 1, "Internet Control Message Protocol Version 4");

  public static final TransportLayer IPV6 = new TransportLayer((byte) 41, "IPv6 Header.");

  public static final TransportLayer IPV6_ICMP =
      new TransportLayer((byte) 58, "Internet Control Message Protocol Version 6");

  public static final TransportLayer IGMP =
      new TransportLayer((byte) 2, "Internet Group Management Protocol");

  public static final TransportLayer TCP =
      new TransportLayer((byte) 6, "Transmission Control Protocol");

  public static final TransportLayer UDP = new TransportLayer((byte) 17, "User Datagram Protocol");

  public static final TransportLayer UNKNOWN = new TransportLayer((byte) -1, "Unknown");

  private static final Map<Byte, TransportLayer> REGISTRY = new HashMap<>();

  private static final Map<Byte, AbstractPacket.Builder> BUILDER = new HashMap<>();

  public TransportLayer(int value, String name) {
    super((byte) value, name);
  }

  public static TransportLayer valueOf(final Byte value) {
    TransportLayer transportLayer = REGISTRY.get(value);
    if (transportLayer == null) {
      return UNKNOWN;
    } else {
      return transportLayer;
    }
  }

  /**
   * @param type type.
   * @param packetBuilder packet builder.
   */
  public static synchronized void register(
      TransportLayer type, AbstractPacket.Builder packetBuilder) {
    BUILDER.put(type.value(), packetBuilder);
    REGISTRY.put(type.value(), type);
  }

  public Packet newInstance(Memory buffer) {
    AbstractPacket.Builder packetBuilder = BUILDER.get(this.value());
    if (packetBuilder == null) {
      if (buffer == null || buffer.capacity() <= 0) {
        return null;
      }
      return new UnknownPacket.Builder().build(buffer);
    }
    return packetBuilder.build(buffer);
  }
}
