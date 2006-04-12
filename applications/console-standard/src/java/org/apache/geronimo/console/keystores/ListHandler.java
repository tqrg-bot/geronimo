/**
 *
 * Copyright 2005 The Apache Software Foundation
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
package org.apache.geronimo.console.keystores;

import java.io.IOException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.geronimo.console.MultiPageModel;
import org.apache.geronimo.console.util.PortletManager;
import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.j2ee.j2eeobjectnames.NameFactory;
import org.apache.geronimo.management.geronimo.KeystoreInstance;
import org.apache.geronimo.management.geronimo.KeystoreManager;

/**
 * Handler for the keystore list screen.
 *
 * @version $Rev: 46019 $ $Date: 2004-09-14 05:56:06 -0400 (Tue, 14 Sep 2004) $
 */
public class ListHandler extends BaseKeystoreHandler {
    public ListHandler() {
        super(LIST_MODE, "/WEB-INF/view/keystore/index.jsp");
    }

    public String actionBeforeView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
        return getMode();
    }

    public void renderView(RenderRequest request, RenderResponse response, MultiPageModel model) throws PortletException, IOException {
        KeystoreManager manager = PortletManager.getCurrentServer(request).getKeystoreManager();
        KeystoreInstance[] keystores = manager.getKeystores();
        PortletSession session = request.getPortletSession(true);
        KeystoreData[] datas = new KeystoreData[keystores.length];
        for (int i = 0; i < datas.length; i++) {
            AbstractName aName = PortletManager.getNameFor(request, keystores[i]);
            String name = (String) aName.getName().get(NameFactory.J2EE_NAME);
            KeystoreData data = (KeystoreData) session.getAttribute(KEYSTORE_DATA_PREFIX+name);
            if(data == null) {
                data = new KeystoreData();
                data.setInstance(keystores[i]);
                session.setAttribute(KEYSTORE_DATA_PREFIX+name, data);
            }
            datas[i] = data;
        }
        request.setAttribute("keystores", keystores);
    }

    public String actionAfterView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
        return getMode()+BEFORE_ACTION;
    }

}
