/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.geronimo.tomcat;

import java.io.StringReader;

import org.apache.catalina.deploy.LoginConfig;
import org.xml.sax.InputSource;

/**
 * @version $Rev$ $Date$
 */
public class WebContextConfig extends BaseGeronimoContextConfig {
    
    private String deploymentDescriptor;
    
    public WebContextConfig(String deploymentDescriptor) {
        this.deploymentDescriptor = deploymentDescriptor;
    }
    
    @Override
    protected InputSource getContextWebXmlSource() {
        if (deploymentDescriptor == null) {
            return super.getContextWebXmlSource();
        } else {
            return new InputSource(new StringReader(deploymentDescriptor));
        }
    }
    
    @Override
    protected void authenticatorConfig() {
        if (!(context instanceof GeronimoStandardContext)) {
            throw new IllegalStateException("Unexpected context type");
        }
        GeronimoStandardContext geronimoContext = (GeronimoStandardContext) context;
        if (geronimoContext.isAuthenticatorInstalled()) {
            return;
        }
        if (geronimoContext.getDefaultSubject() == null) {
            return;
        }
        LoginConfig loginConfig = context.getLoginConfig();
        if (loginConfig == null) {
            loginConfig = new LoginConfig();
        }
        String authMethod = loginConfig.getAuthMethod();
        String realmName = loginConfig.getRealmName();
        String loginPage = loginConfig.getLoginPage();
        String errorPage = loginConfig.getErrorPage();

        configureSecurity(geronimoContext,
                geronimoContext.getPolicyContextId(),
                geronimoContext.getConfigurationFactory(),
                geronimoContext.getDefaultSubject(),
                authMethod,
                realmName,
                loginPage,
                errorPage);
    }
    
}
