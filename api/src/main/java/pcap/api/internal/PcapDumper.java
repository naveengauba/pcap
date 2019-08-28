/**
 * This code is licenced under the GPL version 2.
 */
package pcap.api.internal;

import pcap.api.Pcap;
import pcap.api.internal.foreign.pcap_mapping;
import pcap.spi.Dumper;
import pcap.spi.PacketBuffer;
import pcap.spi.PacketHeader;

import java.foreign.NativeTypes;
import java.foreign.memory.Pointer;

/**
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 */
public class PcapDumper implements Dumper {

    final Pointer<pcap_mapping.pcap_dumper> pcap_dumper;
    final Pointer<Byte> reference;

    public PcapDumper(Pointer<pcap_mapping.pcap_dumper> pcap_dumper) {
        this.pcap_dumper = pcap_dumper;
        this.reference = pcap_dumper.cast(NativeTypes.VOID).cast(NativeTypes.UINT8);
    }

    @Override
    public void dump(PacketHeader header, PacketBuffer buffer) {
        synchronized (Pcap.LOCK) {
            Pcap.MAPPING.pcap_dump(reference, ((PcapPktHdr.Impl) header).pointer(), ((PcapBuffer) buffer).pointer());
        }
    }

    @Override
    public long position() {
        synchronized (Pcap.LOCK) {
            return Pcap.MAPPING.pcap_dump_ftell(pcap_dumper);
        }
    }

    @Override
    public void flush() {
        synchronized (Pcap.LOCK) {
            Pcap.MAPPING.pcap_dump_flush(pcap_dumper);
        }
    }

    @Override
    public void close() {
        synchronized (Pcap.LOCK) {
            if (!pcap_dumper.isNull()) {
                Pcap.MAPPING.pcap_dump_close(pcap_dumper);
            }
        }
    }

}