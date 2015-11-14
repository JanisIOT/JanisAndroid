// Copyright 2012 Square, Inc.
package com.meridian.hackbo.coap.events;

public class CoapFailEvent {
  public int reason;

  public CoapFailEvent(int reason) {
    this.reason = reason;

  }
}
