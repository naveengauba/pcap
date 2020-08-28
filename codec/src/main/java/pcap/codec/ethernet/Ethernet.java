/** This code is licenced under the GPL version 2. */
package pcap.codec.ethernet;

import java.util.Objects;
import pcap.codec.AbstractPacket;
import pcap.codec.NetworkLayer;
import pcap.codec.Packet;
import pcap.common.annotation.Inclubating;
import pcap.common.memory.Memory;
import pcap.common.net.MacAddress;
import pcap.common.util.Strings;
import pcap.common.util.Validate;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Ethernet_frame">Wikipedia</a>
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 */
@Inclubating
public class Ethernet extends AbstractPacket {

  private final Header header;
  private final Packet payload;
  private final Builder builder;

  private Ethernet(final Builder builder) {
    this.header = new Header(builder);
    this.payloadBuffer = builder.payloadBuffer;
    if (this.payloadBuffer != null
        && this.payloadBuffer.readerIndex() < this.payloadBuffer.writerIndex()) {
      this.payload =
          NetworkLayer.valueOf(this.header.payloadType().value()).newInstance(this.payloadBuffer);
    } else {
      this.payload = null;
    }
    this.builder = builder;
  }

  public static final Ethernet newPacket(final Memory buffer) {
    return new Builder().build(buffer);
  }

  @Override
  public Header header() {
    return header;
  }

  @Override
  public Packet payload() {
    return payload;
  }

  @Override
  public Builder builder() {
    return builder;
  }

  @Override
  public Memory buffer() {
    return header().buffer();
  }

  @Override
  public String toString() {
    return Strings.toStringBuilder(this)
        .add("header", header)
        .add("payload", payload != null ? payload.getClass().getSimpleName() : "(None)")
        .toString();
  }

  public static class Header extends AbstractPacket.Header {

    public static final int ETHERNET_HEADER_LENGTH = 14;

    private final MacAddress destinationMacAddress;
    private final MacAddress sourceMacAddress;
    private final NetworkLayer ethernetType;

    private final Builder builder;

    private Header(final Builder builder) {
      this.destinationMacAddress = builder.destinationMacAddress;
      this.sourceMacAddress = builder.sourceMacAddress;
      this.ethernetType = builder.ethernetType;
      this.buffer = resetIndex(builder.buffer, length());
      this.builder = builder;
    }

    /**
     * Destination MAC address.
     *
     * @return returns destination MAC address.
     */
    public MacAddress destinationMacAddress() {
      return destinationMacAddress;
    }

    /**
     * Source MAC address.
     *
     * @return returns source MAC address.
     */
    public MacAddress sourceMacAddress() {
      return sourceMacAddress;
    }

    /**
     * Ethernet type.
     *
     * @return returns {@link NetworkLayer}.
     */
    public NetworkLayer ethernetType() {
      return ethernetType;
    }

    @Override
    public NetworkLayer payloadType() {
      return ethernetType;
    }

    @Override
    public int length() {
      return Header.ETHERNET_HEADER_LENGTH;
    }

    @Override
    public Memory buffer() {
      if (buffer == null) {
        buffer = ALLOCATOR.allocate(length());
        buffer.writeBytes(destinationMacAddress.address());
        buffer.writeBytes(sourceMacAddress.address());
        buffer.writeShort(ethernetType.value());
      }
      return buffer;
    }

    @Override
    public Builder builder() {
      return builder;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Header header = (Header) o;
      return destinationMacAddress.equals(header.destinationMacAddress)
          && sourceMacAddress.equals(header.sourceMacAddress)
          && ethernetType.equals(header.ethernetType);
    }

    @Override
    public int hashCode() {
      return Objects.hash(destinationMacAddress, sourceMacAddress, ethernetType);
    }

    @Override
    public String toString() {
      return Strings.toStringBuilder(this)
          .add("destinationMacAddress", destinationMacAddress)
          .add("sourceMacAddress", sourceMacAddress)
          .add("ethernetType", ethernetType)
          .toString();
    }
  }

  public static class Builder extends AbstractPacket.Builder {

    private MacAddress destinationMacAddress = MacAddress.BROADCAST;
    private MacAddress sourceMacAddress = MacAddress.ZERO;
    private NetworkLayer ethernetType = NetworkLayer.ARP;

    private Memory buffer;
    private Memory payloadBuffer;

    /**
     * Destination MAC address.
     *
     * @param destinationMacAddress destination MAC address.
     * @return returns this {@link Builder}.
     */
    public Builder destinationMacAddress(final MacAddress destinationMacAddress) {
      this.destinationMacAddress = destinationMacAddress;
      return this;
    }

    /**
     * Source MAC address.
     *
     * @param sourceMacAddress source MAC address.
     * @return returns this {@link Builder}.
     */
    public Builder sourceMacAddress(final MacAddress sourceMacAddress) {
      this.sourceMacAddress = sourceMacAddress;
      return this;
    }

    /**
     * Ethernet type.
     *
     * @param ethernetType ethernet type.
     * @return returns this {@link Builder}.
     */
    public Builder ethernetType(final NetworkLayer ethernetType) {
      this.ethernetType = ethernetType;
      return this;
    }

    public Builder payload(AbstractPacket packet) {
      this.payloadBuffer = packet.buffer();
      return this;
    }

    @Override
    public Ethernet build() {
      return new Ethernet(this);
    }

    @Override
    public Ethernet build(final Memory buffer) {
      resetIndex(buffer);
      byte[] hardwareAddressBuffer;
      hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
      buffer.readBytes(hardwareAddressBuffer);
      this.destinationMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
      hardwareAddressBuffer = new byte[MacAddress.MAC_ADDRESS_LENGTH];
      buffer.readBytes(hardwareAddressBuffer);
      this.sourceMacAddress = MacAddress.valueOf(hardwareAddressBuffer);
      this.ethernetType = NetworkLayer.valueOf(buffer.readShort());
      this.buffer = buffer;
      this.payloadBuffer = buffer.slice();
      return new Ethernet(this);
    }

    @Override
    public Builder reset() {
      return reset(readerIndex, Header.ETHERNET_HEADER_LENGTH);
    }

    @Override
    public Builder reset(long offset, long length) {
      if (buffer != null) {
        resetIndex(buffer);
        Validate.notIllegalArgument(offset + length <= buffer.capacity());
        Validate.notIllegalArgument(destinationMacAddress != null, ILLEGAL_HEADER_EXCEPTION);
        Validate.notIllegalArgument(sourceMacAddress != null, ILLEGAL_HEADER_EXCEPTION);
        Validate.notIllegalArgument(ethernetType != null, ILLEGAL_HEADER_EXCEPTION);
        long index = offset;
        buffer.setBytes(index, destinationMacAddress.address());
        index += MacAddress.MAC_ADDRESS_LENGTH;
        buffer.setBytes(index, sourceMacAddress.address());
        index += MacAddress.MAC_ADDRESS_LENGTH;
        buffer.setShort(index, ethernetType.value());
      }
      return this;
    }
  }
}
