package pcap.api.internal.foreign.struct;

import java.foreign.annotations.NativeGetter;
import java.foreign.annotations.NativeStruct;
import java.foreign.memory.Array;
import java.foreign.memory.Struct;

public interface darwin_structs {

  @NativeStruct("[u8(sa_len)u8(sa_family)[14u8](sa_data)](sockaddr)")
  interface sockaddr extends Struct<sockaddr> {

    @NativeGetter("sa_len")
    byte sa_len$get();

    @NativeGetter("sa_family")
    byte sa_family$get();

    @NativeGetter("sa_data")
    Array<Byte> sa_data$get();
  }

  @NativeStruct("[u32(s_addr)](in_addr)")
  interface in_addr extends Struct<in_addr> {

    @NativeGetter("s_addr")
    int s_addr$get();
  }

  @NativeStruct(
      value =
          "[u8(sin_len)u8(sin_family)u16(sin_port)${in_addr}(sin_addr)[8u8](sin_zero)](sockaddr_in)",
      resolutionContext = in_addr.class)
  interface sockaddr_in extends Struct<sockaddr_in> {

    @NativeGetter("sin_len")
    byte sin_len$get();

    @NativeGetter("sin_family")
    byte sin_family$get();

    @NativeGetter("sin_port")
    short sin_port$get();

    @NativeGetter("sin_addr")
    in_addr sin_addr$get();

    @NativeGetter("sin_zero")
    Array<Byte> sin_zero$get();
  }

  @NativeStruct(
      value = "[${anon$__u6_addr}(__u6_addr)](in6_addr)",
      resolutionContext = in6_addr.anon$__u6_addr.class)
  interface in6_addr extends Struct<in6_addr> {

    @NativeGetter("__u6_addr")
    anon$__u6_addr __u6_addr$get();

    @NativeStruct("[[16u8](__u6_addr8)[8u16](__u6_addr16)[4u32](__u6_addr32)](anon$__u6_addr)")
    interface anon$__u6_addr extends Struct<anon$__u6_addr> {

      @NativeGetter("__u6_addr8")
      Array<Byte> __u6_addr8$get();

      @NativeGetter("__u6_addr16")
      Array<Short> __u6_addr16$get();

      @NativeGetter("__u6_addr32")
      Array<Integer> __u6_addr32$get();
    }
  }

  @NativeStruct(
      value =
          "[u8(sin6_len)u8(sin6_family)u16(sin6_port)u32(sin6_flowinfo)${in6_addr}(sin6_addr)u32(sin6_scope_id)](sockaddr_in6)",
      resolutionContext = in6_addr.class)
  interface sockaddr_in6 extends Struct<sockaddr_in6> {

    @NativeGetter("sin6_len")
    byte sin6_len$get();

    @NativeGetter("sin6_family")
    byte sin6_family$get();

    @NativeGetter("sin6_port")
    short sin6_port$get();

    @NativeGetter("sin6_flowinfo")
    int sin6_flowinfo$get();

    @NativeGetter("sin6_addr")
    in6_addr sin6_addr$get();

    @NativeGetter("sin6_scope_id")
    int sin6_scope_id$get();
  }
}