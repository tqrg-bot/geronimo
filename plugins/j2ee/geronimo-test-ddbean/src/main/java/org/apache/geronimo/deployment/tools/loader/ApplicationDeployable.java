/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.deployment.tools.loader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.J2eeApplicationObject;
import javax.enterprise.deploy.model.XpathListener;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
import org.osgi.framework.Bundle;

/**
 * 
 * 
 * @version $Rev$ $Date$
 */
public abstract class ApplicationDeployable extends AbstractDeployable implements J2eeApplicationObject {
    private final Map uriMap;
    public ApplicationDeployable(Bundle bundle) throws DDBeanCreateException {
        super(ModuleType.EAR, bundle, "META-INF/application.xml");
        DDBean[] moduleBeans = getChildBean("/application/module");
        uriMap = new HashMap(moduleBeans.length);
        for (int i = 0; i < moduleBeans.length; i++) {
            DDBean moduleBean = moduleBeans[i];
            String uri;

        }
    }

    public void addXpathListener(ModuleType type, String xpath, XpathListener xpl) {
    }

    public void removeXpathListener(ModuleType type, String xpath, XpathListener xpl) {
    }
}
