/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.osgi;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test the example.
 *
 * @version 
 */
public class RmiTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camelContext.xml");
    }

    @Test
    public void testRmi() throws Exception {
        // Create a new camel context to send the request so we can test the service which is deployed into a container
        CamelContext camelContext = new DefaultCamelContext();
        ProducerTemplate myTemplate = camelContext.createProducerTemplate();
        myTemplate.start();
        try {
            String out = myTemplate.requestBody("rmi://localhost:37541/helloServiceBean", "Camel", String.class);
            assertEquals("Hello Camel", out);
        } finally {
            if (myTemplate != null) {
                template.stop();
            }
            camelContext.stop();
        }
        
    }

}
