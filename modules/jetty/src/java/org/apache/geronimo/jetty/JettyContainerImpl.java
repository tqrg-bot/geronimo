/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.jetty;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpListener;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.UserRealm;
import org.mortbay.jetty.Server;

import org.apache.geronimo.gbean.GBean;
import org.apache.geronimo.gbean.GBeanContext;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoFactory;
import org.apache.geronimo.gbean.GOperationInfo;
import org.apache.geronimo.gbean.WaitingException;


/**
 * @version $Revision: 1.5 $ $Date: 2004/05/30 19:09:57 $
 */
public class JettyContainerImpl implements JettyContainer, GBean {

    private final Server server;

    public JettyContainerImpl() {
        server = new JettyServer();
    }

    public void addListener(HttpListener listener) {
        server.addListener(listener);
    }

    public void removeListener(HttpListener listener) {
        server.removeListener(listener);
    }

    public void addContext(HttpContext context) {
        server.addContext(context);
    }

    public void removeContext(HttpContext context) {
        server.removeContext(context);
    }

    public void addRealm(UserRealm realm) {
        server.addRealm(realm);
    }

    public void removeRealm(UserRealm realm) {
        server.removeRealm(realm.getName());
    }

    public void setGBeanContext(GBeanContext context) {
    }

    public void doStart() throws WaitingException, Exception {
        server.start();
    }

    public void doStop() throws WaitingException {
        try {
            server.stop(true);
        } catch (InterruptedException e) {
        }
    }

    public void doFail() {
        try {
            server.stop(false);
        } catch (InterruptedException e) {
            // continue
        }
    }

    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoFactory infoFactory = new GBeanInfoFactory("Jetty Web Container", JettyContainerImpl.class.getName());
        infoFactory.addOperation(new GOperationInfo("addListener", new String[]{HttpListener.class.getName()}));
        infoFactory.addOperation(new GOperationInfo("removeListener", new String[]{HttpListener.class.getName()}));
        infoFactory.addOperation(new GOperationInfo("addContext", new String[]{HttpContext.class.getName()}));
        infoFactory.addOperation(new GOperationInfo("removeContext", new String[]{HttpContext.class.getName()}));
        infoFactory.addOperation(new GOperationInfo("addRealm", new String[]{UserRealm.class.getName()}));
        infoFactory.addOperation(new GOperationInfo("removeRealm", new String[]{UserRealm.class.getName()}));
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
