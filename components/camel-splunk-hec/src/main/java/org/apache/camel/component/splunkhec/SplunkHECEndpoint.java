/*
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
package org.apache.camel.component.splunkhec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.camel.support.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The splunk component allows to publish events in Splunk using the HTTP Event Collector.
 */
@UriEndpoint(firstVersion = "3.3.0", scheme = "splunk-hec", title = "Splunk HEC", syntax = "splunk-hec:endpoint/token", label = "log,monitoring")
public class SplunkHECEndpoint extends DefaultEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SplunkHECEndpoint.class);
    private static final Pattern URI_PARSER = Pattern.compile("splunk-hec\\:\\/?\\/?(\\w+):(\\d+)/(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12})\\??.*");

    @UriPath
    private String endpointUri;

    private String splunkURL;

    private String token;

    @UriParam
    private SplunkHECConfiguration configuration;

    public SplunkHECEndpoint() {
    }

    public SplunkHECEndpoint(String uri, SplunkHECComponent component, SplunkHECConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
        Matcher match = URI_PARSER.matcher(uri);
        if (!match.matches()) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        int port = Integer.valueOf(match.group(2));
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        splunkURL = match.group(1) + ":" + port;
        token = match.group(3);
    }

    @Override
    public Producer createProducer() {
        return new SplunkHECProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) {
        throw new UnsupportedOperationException();
    }

    public SplunkHECConfiguration getConfiguration() {
        return configuration;
    }

    public String getSplunkURL() {
        return splunkURL;
    }

    public String getToken() {
        return token;
    }

    /**
     * Splunk host URI
     */
    @Override
    public void setEndpointUri(String endpointUri) {
        this.endpointUri = endpointUri;
    }

    @Override
    public String getEndpointUri() {
        return endpointUri;
    }
}
