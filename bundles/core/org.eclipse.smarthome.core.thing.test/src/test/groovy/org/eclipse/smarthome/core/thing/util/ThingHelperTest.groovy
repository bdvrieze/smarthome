/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
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
package org.eclipse.smarthome.core.thing.util

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.config.core.Configuration
import org.eclipse.smarthome.core.thing.ChannelUID
import org.eclipse.smarthome.core.thing.Thing
import org.eclipse.smarthome.core.thing.ThingTypeUID
import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.core.thing.binding.builder.ChannelBuilder
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder
import org.junit.Test

class ThingHelperTest {

    @Test
    void 'Two technical equal Thing instances are detected as "equal"'() {
        Thing thingA = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withChannels(
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel1"), "itemType").build(),
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel2"), "itemType").build()
                )
                .withConfiguration(new Configuration())
                .build()
        thingA.getConfiguration().put("prop1", "value1")
        thingA.getConfiguration().put("prop2", "value2")

        assertTrue ThingHelper.equals(thingA, thingA)

        Thing thingB = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withChannels(
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel2"), "itemType").build(),
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel1"), "itemType").build()
                )
                .withConfiguration(new Configuration())
                .build()
        thingB.getConfiguration().put("prop2", "value2")
        thingB.getConfiguration().put("prop1", "value1")

        assertTrue ThingHelper.equals(thingA, thingB)
    }

    @Test
    void 'Two things are different after properties were modified'() {
        Thing thingA = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withChannels(
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel1"), "itemType").build(),
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel2"), "itemType").build()
                )
                .withConfiguration(new Configuration())
                .build()
        thingA.getConfiguration().put("prop1", "value1")

        Thing thingB = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withChannels(
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel2"), "itemType").build(),
                ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel1"), "itemType").build()
                )
                .withConfiguration(new Configuration())
                .build()
        thingB.getConfiguration().put("prop1", "value1")

        assertTrue ThingHelper.equals(thingA, thingB)

        thingB.getConfiguration().put("prop3", "value3")

        assertFalse ThingHelper.equals(thingA, thingB)
    }

    @Test
    void 'Two things are different after channels were modified'() {
        Thing thingA = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).build()

        Thing thingB = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).build()

        assertTrue ThingHelper.equals(thingA, thingB)

        thingB.setChannels([ChannelBuilder.create(new ChannelUID("binding:type:thingId:channel3"), "itemType3").build()])

        assertFalse ThingHelper.equals(thingA, thingB)
    }


    @Test
    void 'Two things are different after label was modified'() {
        Thing thingA = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).withLabel("foo").build()

        Thing thingB = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).withLabel("foo").build()

        assertTrue ThingHelper.equals(thingA, thingB)

        thingB.setLabel("bar")

        assertFalse ThingHelper.equals(thingA, thingB)
    }

    @Test
    void 'Two things are different after location was modified'() {
        Thing thingA = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).withLocation("foo").build()

        Thing thingB = ThingBuilder.create(new ThingUID(new ThingTypeUID("binding:type"), "thingId"))
                .withConfiguration(new Configuration()).withLocation("foo").build()

        assertTrue ThingHelper.equals(thingA, thingB)

        thingB.setLocation("bar")

        assertFalse ThingHelper.equals(thingA, thingB)
    }

    @Test(expected=IllegalArgumentException)
    void 'assert that no duplicate channels can be added'() {
        ThingTypeUID THING_TYPE_UID = new ThingTypeUID("test", "test")
        ThingUID THING_UID = new ThingUID(THING_TYPE_UID, "test")

        Thing thing = ThingBuilder.create(THING_TYPE_UID, THING_UID).withChannels(
                ChannelBuilder.create(new ChannelUID(THING_UID, "channel1"), "").build(),
                ChannelBuilder.create(new ChannelUID(THING_UID, "channel2"), "").build()
                ).build();

        ThingHelper.addChannelsToThing(thing, [
            ChannelBuilder.create(new ChannelUID(THING_UID, "channel2"), "").build(),
            ChannelBuilder.create(new ChannelUID(THING_UID, "channel3"), "").build()
        ])
    }
}