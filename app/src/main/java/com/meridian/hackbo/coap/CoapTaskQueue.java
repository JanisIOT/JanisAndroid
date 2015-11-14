// Copyright 2012 Square, Inc.
package com.meridian.hackbo.coap;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.meridian.hackbo.coap.events.CoapQueueSizeEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.FileObjectQueue.Converter;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;


public class CoapTaskQueue extends TaskQueue<CoapTask> {
  private static final String FILENAME = "image_upload_task_queue";

  private final Context context;
  private final Bus bus;

  private CoapTaskQueue(ObjectQueue<CoapTask> delegate, Context context, Bus bus) {
    super(delegate);
    this.context = context;
    this.bus = bus;
    bus.register(this);

    if (size() > 0) {
      startService();
    }
  }

  private void startService() {
    context.startService(new Intent(context, CoapTaskService.class));
  }

  @Override
  public void add(CoapTask entry) {
    super.add(entry);
    bus.post(produceSizeChanged());
    startService();
  }

  @Override
  public void remove() {
    super.remove();
    bus.post(produceSizeChanged());
  }

  @SuppressWarnings("UnusedDeclaration") // Used by event bus.
  @Produce public CoapQueueSizeEvent produceSizeChanged() {
    return new CoapQueueSizeEvent(size());
  }

  public static CoapTaskQueue create(Context context, Gson gson, Bus bus) {
    Converter<CoapTask> converter = new GsonConverter<CoapTask>(gson, CoapTask.class);
    InMemoryObjectQueue<CoapTask> delegate;
      delegate = new InMemoryObjectQueue<CoapTask>();
    return new CoapTaskQueue(delegate, context, bus);
  }
}
