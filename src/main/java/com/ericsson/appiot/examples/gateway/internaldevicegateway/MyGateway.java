package com.ericsson.appiot.examples.gateway.internaldevicegateway;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ericsson.appiot.examples.gateway.internaldevicegateway.smartobject.InternalTemperature;
import com.ericsson.appiot.gateway.AppIoTGateway;
import com.ericsson.appiot.gateway.AppIoTListener;
import com.ericsson.appiot.gateway.device.Device;
import com.ericsson.appiot.gateway.device.DeviceAppIoTListener;
import com.ericsson.appiot.gateway.device.DeviceManager;
import com.ericsson.appiot.gateway.device.smartobject.SmartObjectBase;

public class MyGateway {
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private AppIoTListener appIoTListener;
	private AppIoTGateway appIoTGateway;

	public static void main(String[] args) {
		MyGateway application = new MyGateway();
		application.start();
	}
	
	private void start() {
		logger.log(Level.INFO, "Internal Device Gateway starting up.");
		
		DeviceManager deviceManager = new DeviceManager();
		// Internal device for representing the gateway itself.
		Device internalDevice = new Device();
		SmartObjectBase internalTemperature = new InternalTemperature(internalDevice);
		internalDevice.addSmartObject(internalTemperature);
		deviceManager.setInternalDevice(internalDevice);
		
        appIoTListener = new DeviceAppIoTListener(deviceManager);
		appIoTGateway = new AppIoTGateway(appIoTListener);
		
		appIoTGateway.start();
		
		logger.log(Level.INFO, "Internal Device Gateway started. Type quit to shut down.");
		Scanner scanner = new Scanner(System.in);
		while(!scanner.nextLine().equalsIgnoreCase("quit")) {
		}
		scanner.close();
		logger.log(Level.INFO, "Internal Device Gateway shut down.");
	}
}
