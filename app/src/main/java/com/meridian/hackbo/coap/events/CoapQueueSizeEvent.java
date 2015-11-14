// Copyright 2012 Square, Inc.
package com.meridian.hackbo.coap.events;

public class CoapQueueSizeEvent {
  public final int size;

  public CoapQueueSizeEvent(int size) {
    this.size = size;
  }
}
