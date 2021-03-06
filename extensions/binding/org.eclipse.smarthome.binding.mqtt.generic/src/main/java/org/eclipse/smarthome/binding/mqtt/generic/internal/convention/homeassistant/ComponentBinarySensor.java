/**
 * Copyright (c) 2014,2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.binding.mqtt.generic.internal.convention.homeassistant;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.binding.mqtt.generic.internal.generic.ChannelStateUpdateListener;
import org.eclipse.smarthome.binding.mqtt.generic.internal.values.OnOffValue;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.gson.Gson;

/**
 * A MQTT BinarySensor, following the https://www.home-assistant.io/components/binary_sensor.mqtt/ specification.
 *
 * @author David Graeff - Initial contribution
 */
@NonNullByDefault
public class ComponentBinarySensor extends AbstractComponent {
    public static final String sensorChannelID = "sensor"; // Randomly chosen channel "ID"

    /**
     * Configuration class for MQTT component
     */
    static class Config {
        protected String name = "MQTT Sensor";
        protected String icon = "";
        protected int qos = 1;
        protected boolean retain = true;
        protected @Nullable String value_template;
        protected @Nullable String unique_id;

        protected String unit_of_measurement = "";
        protected @Nullable String device_class;
        protected boolean force_update = false;
        protected int expire_after = 0;

        protected String state_topic = "";
        protected String payload_on = "ON";
        protected String payload_off = "OFF";

        protected @Nullable String availability_topic;
        protected String payload_available = "online";
        protected String payload_not_available = "offline";
    };

    protected Config config = new Config();

    public ComponentBinarySensor(ThingUID thing, HaID haID, String configJSON,
            @Nullable ChannelStateUpdateListener channelStateUpdateListener, Gson gson) {
        super(thing, haID, configJSON, gson);
        config = gson.fromJson(configJSON, Config.class);

        if (config.force_update) {
            throw new UnsupportedOperationException("Component:Sensor does not support forced updates");
        }

        channels.put(sensorChannelID,
                new CChannel(this, sensorChannelID, new OnOffValue(config.payload_on, config.payload_off),
                        config.state_topic, null, config.name, config.unit_of_measurement, channelStateUpdateListener));
    }

    @Override
    public String name() {
        return config.name;
    }
}
