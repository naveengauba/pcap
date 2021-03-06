/*
 * Copyright (c) 2020 Pcap <contact@pcap.ardikars.com>
 * SPDX-License-Identifier: MIT
 */
package pcap.spi.exception.error;

/**
 * Interface isn't up ({@code -9}).
 *
 * @author <a href="mailto:contact@ardikars.com">Ardika Rommy Sanjaya</a>
 * @since 1.0.0
 */
public class InterfaceNotUpException extends Exception {

  public InterfaceNotUpException(String message) {
    super(message);
  }
}
