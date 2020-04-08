/** This code is licenced under the GPL version 2. */
package pcap.spring.boot.autoconfigure.experimental.annotation;

import java.lang.annotation.*;
import pcap.common.annotation.Inclubating;

@Documented
@Inclubating
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Source {}