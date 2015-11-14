// Copyright 2012 Square, Inc.
package com.meridian.hackbo.coap.events;

public class CoapSuccessEvent {
  public  int event;
  public String url;
  public byte[] chunk;

  public CoapSuccessEvent(int event) {
    this.event = event;
  }
  public CoapSuccessEvent(byte [] chunk) {
    this.chunk = chunk;
  }
  public CoapSuccessEvent(String url) {
    this.url = url;
  }
}
