/*
 * Copyright 2015 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hivemq.authorization.example.callbacks;

import com.hivemq.spi.aop.cache.Cached;
import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.security.OnAuthorizationCallback;
import com.hivemq.spi.callback.security.authorization.AuthorizationBehaviour;
import com.hivemq.spi.security.ClientData;
import com.hivemq.spi.topic.MqttTopicPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Florian Limpöck
 */
public class AuthorizationCallback implements OnAuthorizationCallback {

    /**
     * This is an example authorization method, which shows how to allow a client only
     * to access topics, which have its client id up front.
     * This can be easily changed to implement your authorization logic of choice: read from a database
     * or call your authorization webservice.
     *
     * @param clientData All information from the MQTT client requesting authorization.
     * @return a list of valid topic permissions
     */
    @Override
    @Cached(timeToLive = 5, timeUnit = TimeUnit.MINUTES)
    public List<MqttTopicPermission> getPermissionsForClient(ClientData clientData) {
        ArrayList<MqttTopicPermission> mqttTopicPermissions = new ArrayList<>();
        mqttTopicPermissions.add(
                new MqttTopicPermission(
                        clientData.getClientId() + "/#",            // Topic
                        MqttTopicPermission.TYPE.ALLOW,        // Type
                        MqttTopicPermission.QOS.ALL,        // QoS
                        MqttTopicPermission.ACTIVITY.ALL)); // Publish, Subscribe, All

        return mqttTopicPermissions;
    }

    @Override
    public AuthorizationBehaviour getDefaultBehaviour() {
        return AuthorizationBehaviour.NEXT;
    }

    @Override
    public int priority() {
        return CallbackPriority.CRITICAL;
    }
}
