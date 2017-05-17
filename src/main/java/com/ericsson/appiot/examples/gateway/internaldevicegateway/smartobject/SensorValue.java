package com.ericsson.appiot.examples.gateway.internaldevicegateway.smartobject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ericsson.appiot.gateway.device.InitException;
import com.ericsson.appiot.gateway.device.smartobject.SmartObjectBase;
import com.ericsson.appiot.gateway.device.smartobject.resource.ObserveRequestException;
import com.ericsson.appiot.gateway.device.smartobject.resource.type.FloatResource;
import com.ericsson.appiot.gateway.model.ResourceModel;

public class SensorValue extends FloatResource implements Runnable {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private int counter = 0;

    public SensorValue(SmartObjectBase smartObject, ResourceModel resourceModel) {
        super(smartObject, resourceModel);            
        this.counter = ((int)(Math.random() * 100.0D));
    }

    @Override
    public void init() throws InitException {
    	// This is the place to setup db connections etc. if needed.
    }

    @Override
	protected void onObserveRequest() throws ObserveRequestException {
		super.onObserveRequest();
		if(scheduler.isTerminated()) {
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
		}
	}
    
	@Override
	protected void onCancelObserveRequest() throws ObserveRequestException {
		super.onCancelObserveRequest();
		scheduler.shutdown();
	}

	@Override
    public void run() {
        int offset = 50;
        float measurement = (float) (offset - offset * Math.cos(this.counter / 100.0 * 3.141592653589793D));
        setValue(measurement);
        this.counter += 10;
    }

}
