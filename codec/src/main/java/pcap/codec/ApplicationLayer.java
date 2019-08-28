/**
 * This code is licenced under the GPL version 2.
 */
package pcap.codec;

import pcap.common.memory.Memory;
import pcap.common.util.NamedNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 */
public final class ApplicationLayer extends NamedNumber<Short, ApplicationLayer> {

    private static final Map<ApplicationLayer, Short> registry =
            new HashMap<ApplicationLayer, Short>();

    private static final Map<Short, AbstractPacket.Builder> builder =
            new HashMap<Short, AbstractPacket.Builder>();

    public ApplicationLayer(Short value, String name) {
        super(value, name);
    }

    public Packet newInstance(Memory buffer) {
        AbstractPacket.Builder packetBuilder = builder.get(this.getValue());
        if (packetBuilder == null) {
            if (buffer == null || buffer.capacity() <= 0) {
                return null;
            }
            return new UnknownPacket.Builder().build(buffer);
        }
        return packetBuilder.build(buffer);
    }
    public static ApplicationLayer valueOf(short value) {
        for (Map.Entry<ApplicationLayer, Short> entry : registry.entrySet()) {
            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }
        return new ApplicationLayer((short) -1, "Unknown");
    }

    /**
     *
     * @param dataLinkLayer application type.
     */
    public static void register(ApplicationLayer dataLinkLayer) {
        synchronized (registry) {
            registry.put(dataLinkLayer, dataLinkLayer.getValue());
        }
    }

    /**
     *
     * @param dataLinkLayer application type.
     * @param packetBuilder packet builder.
     */
    public static void register(ApplicationLayer dataLinkLayer, AbstractPacket.Builder packetBuilder) {
        synchronized (builder) {
            builder.put(dataLinkLayer.getValue(), packetBuilder);
        }
    }

}