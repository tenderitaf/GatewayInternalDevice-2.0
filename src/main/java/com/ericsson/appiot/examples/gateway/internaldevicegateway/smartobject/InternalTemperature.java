package com.ericsson.appiot.examples.gateway.internaldevicegateway.smartobject;

import com.ericsson.appiot.gateway.device.Device;
import com.ericsson.appiot.gateway.device.InitException;
import com.ericsson.appiot.gateway.device.smartobject.SmartObjectBase;
import com.ericsson.appiot.gateway.device.smartobject.resource.Resource;
import com.ericsson.appiot.gateway.device.smartobject.resource.ResourceValueChangedListener;
import com.ericsson.appiot.gateway.device.smartobject.resource.type.FloatResource;
import com.ericsson.appiot.gateway.device.smartobject.resource.type.ResetResource;
import com.ericsson.appiot.gateway.device.smartobject.resource.type.StringResource;
import com.ericsson.appiot.gateway.model.ObjectModel;

public class InternalTemperature extends SmartObjectBase implements ResourceValueChangedListener {

    private static final String UNIT                                    = "Cel";

    public static final int OBJECT_ID_TEMPERATURE                       = 3303;

    public static final int RESOURCE_ID_SENSOR_VALUE                    = 5700;
    public static final int RESOURCE_ID_UNITS                           = 5701;
    public static final int RESOURCE_ID_MIN_MEASURED_VALUE              = 5601;
    public static final int RESOURCE_ID_MAX_MEASURED_VALUE              = 5602;
    public static final int RESOURCE_ID_RESET_MIN_MAX_MEASURED_VALUES   = 5605;

    /**
     * Creates a Temperature Smart Object.
     * 
     */
    public InternalTemperature(Device device, ObjectModel objectModel, Integer objectId, Integer instanceId) {
        super(device, objectModel, objectId, instanceId);

        addResource(new SensorValue(this, objectModel.getResourceModel(RESOURCE_ID_SENSOR_VALUE)));
        addResource(new FloatResource(this, objectModel.getResourceModel(RESOURCE_ID_MIN_MEASURED_VALUE), 0, 0f));
        addResource(new FloatResource(this, objectModel.getResourceModel(RESOURCE_ID_MAX_MEASURED_VALUE), 0, 0f));
        addResource(new StringResource(this, objectModel.getResourceModel(RESOURCE_ID_UNITS), UNIT));

        ResetResource resetResource = new ResetResource(this, objectModel.getResourceModel(RESOURCE_ID_RESET_MIN_MAX_MEASURED_VALUES));
        resetResource.addResource(getResource(RESOURCE_ID_MIN_MEASURED_VALUE));
        resetResource.addResource(getResource(RESOURCE_ID_MAX_MEASURED_VALUE));
        addResource(resetResource);
        
        getResource(RESOURCE_ID_SENSOR_VALUE).addListener(this);
    }

    @Override
    public void init() throws InitException {
    	// This is the place to setup db connections etc. if needed.
    }
    
    @Override
    public void onResourceValueChanged(Resource resource, Object value) {
        if(resource.getResourceId() == RESOURCE_ID_SENSOR_VALUE) {
            float measurement = (float) value;
            FloatResource min = (FloatResource) getResource(RESOURCE_ID_MIN_MEASURED_VALUE);
            FloatResource max = (FloatResource) getResource(RESOURCE_ID_MAX_MEASURED_VALUE);
            min.setValue(min.getValue() > measurement ? measurement : min.getValue());  
            max.setValue(max.getValue() < measurement ? measurement : max.getValue());
        }
    }
}
