/**
 *
 * Copyright 2004 The Apache Software Foundation
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
package org.apache.geronimo.j2ee.j2eeobjectnames;

import java.util.Properties;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @version $Rev: $ $Date: $
 */
public class NameFactory {

    //name components
    public static String J2EE_SERVER = "J2EEServer";
    public static String J2EE_APPLICATION = "J2EEApplication";
    public static String J2EE_MODULE = "J2EEModule";
    public static String J2EE_TYPE = "j2eeType";
    public static String J2EE_NAME = "name";

    //types
    public static String J2EE_DOMAIN = "J2EEDomain";
    public static String JVM = "JVM";
    public static String APP_CLIENT_MODULE = "AppClientModule";

    public static String EJB = "EJB";
    public static String EJB_MODULE = "EJBModule";
    public static String MESSAGE_DRIVEN_BEAN = "MessageDrivenBean";
    public static String ENTITY_BEAN = "EntityBean";
    public static String STATEFUL_SESSION_BEAN = "StatefulSessionBean";
    public static String STATELESS_SESSION_BEAN = "StatelessSessionBean";

    public static String WEB_MODULE = "WebModule";
    public static String SERVLET = "Servlet";

    public static String RESOURCE_ADAPTER_MODULE = "ResourceAdapterModule";
    public static String RESOURCE_ADAPTER = "ResourceAdapter";
    public static String JAVA_MAIL_RESOURCE = "JavaMailResource";
    public static String JCA_RESOURCE = "JCAResource";
    public static String JCA_CONNECTION_FACTORY = "JCAConnectionFactory";
    public static String JCA_MANAGED_CONNECTION_FACTORY = "JCAManagedConnectionFactory";
    public static String JDBC_RESOURCE = "JDBCResource";
    public static String JDBC_DATASOURCE = "JDBCDataSource";
    public static String JDBC_DRIVER = "JDBCDriver";
    public static String JMS_RESOURCE = "JMSResource";

    public static String JNDI_RESOURCE = "JNDIResource";
    public static String JTA_RESOURCE = "JTAResource";

    public static String RMI_IIOP_RESOURCE = "RMI_IIOPResource";
    public static String URL_RESOURCE = "URLResource";

    //used for J2EEApplication= when component is not deployed in an ear.
    public static String NULL = "null";

    //geronimo extensions
    public static final String JCA_ADMIN_OBJECT = "JCAAdminObject";
    public static final String JCA_ACTIVATION_SPEC = "JCAActivationSpec";
    //TODO shouldn't we use the RESOURCE_ADAPTER string?
    public static final String JCA_RESOURCE_ADAPTER = "JCAResourceAdapter";
    public static final String JCA_WORK_MANAGER = "JCAWorkManager";
    public static final String JCA_CONNECTION_MANAGER = "JCAConnectionManager";
    public static final String WEB_FILTER = "WebFilter";
    public static final String WEB_FILTER_MAPPING = "WebFilterMapping";
    public static final String URL_PATTERN = "URLPattern";
    public static final String GERONIMO_SERVICE = "GBean"; //copied in GBeanInfoBuilder to avoid dependencies in the wrong direction.

    public static String JAXR_CONNECTION_FACTORY = "JAXRConnectionFactory";

    public static final String CONFIG_BUILDER = "ConfigBuilder";
    public static final String MODULE_BUILDER = "ModuleBuilder";
    public static final String SECURITY_REALM = "SecurityRealm";
    public static final String LOGIN_MODULE = "LoginModule";
    public static final String APP_CLIENT = "AppClient";
    //jsr 88 configurer
    public static final String DEPLOYMENT_CONFIGURER = "DeploymentConfigurer";
    public static final String CONFIGURATION_STORE = "ConfigurationStore";
    public static final String DEPLOYER = "Deployer"; //duplicated in Deployer
    public static final String REALM_BRIDGE = "RealmBridge";
    public static final String CONFIGURATION_ENTRY = "ConfigurationEntry";
    public static final String PERSISTENT_CONFIGURATION_LIST = "PersistentConfigurationList"; //duplicated in FileConfigurationList
//    public static final String URL_PATTERN = "URLPattern";
    public static String DEFAULT_SERVLET = "DefaultServlet";
    public static final String SERVLET_WEB_SERVICE_TEMPLATE = "ServletWebServiceTemplate";

    public static ObjectName getDomainName(String j2eeDomainName, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, J2EE_DOMAIN);
        props.put(J2EE_NAME, context.getJ2eeDomainName(j2eeDomainName));
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    public static ObjectName getServerName(String j2eeDomainName, String j2eeServerName, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, J2EE_SERVER);
        props.put(J2EE_NAME, context.getJ2eeServerName(j2eeServerName));
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    public static ObjectName getApplicationName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, J2EE_APPLICATION);
        props.put(J2EE_SERVER, context.getJ2eeServerName(j2eeServerName));
        props.put(J2EE_NAME, context.getJ2eeApplicationName(j2eeApplicationName));
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    public static ObjectName getModuleName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleType, String j2eeModuleName, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        //N.B.! module context will have the module's j2eeType as its module type attribute.
        props.put(J2EE_TYPE, context.getJ2eeModuleType(j2eeModuleType));
        props.put(J2EE_SERVER, context.getJ2eeServerName(j2eeServerName));
        props.put(J2EE_APPLICATION, context.getJ2eeApplicationName(j2eeApplicationName));
        props.put(J2EE_NAME, context.getJ2eeModuleName(j2eeModuleName));
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    public static ObjectName getComponentName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleType, String j2eeModuleName, String j2eeName, String j2eeType, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, context.getJ2eeType(j2eeType));
        props.put(J2EE_SERVER, context.getJ2eeServerName(j2eeServerName));
        props.put(J2EE_APPLICATION, context.getJ2eeApplicationName(j2eeApplicationName));
        props.put(context.getJ2eeModuleType(j2eeModuleType), context.getJ2eeModuleName(j2eeModuleName));
        props.put(J2EE_NAME, context.getJ2eeName(j2eeName));
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    public static ObjectName getEjbComponentName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleName, String j2eeName, String j2eeType, J2eeContext context) throws MalformedObjectNameException {
        return getComponentName(j2eeDomainName, j2eeServerName, j2eeApplicationName, EJB_MODULE, j2eeModuleName, j2eeName, j2eeType, context);
    }

    public static String getEjbComponentNameString(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleName, String j2eeName, String j2eeType, J2eeContext context) throws MalformedObjectNameException {
        return getEjbComponentName(j2eeDomainName, j2eeServerName, j2eeApplicationName, j2eeModuleName, j2eeName, j2eeType, context).getCanonicalName();
    }


    public static ObjectName getComponentNameQuery(String domainName, String serverName, String applicationName, String moduleType, String moduleName, String name, String type, J2eeContext context) throws MalformedObjectNameException {
        StringBuffer buffer = new StringBuffer();
        if ("*".equals(domainName)) {
            buffer.append("*");
        } else {
            buffer.append(context.getJ2eeDomainName(domainName));
        }
        boolean query = false;
        String sep = ":";
        //implicit attributes, if they are missing it means "use value from j2eecontext": if they are * it means skip and make a query
        if ("*".equals(serverName)) {
            query = true;
        } else {
            buffer.append(sep).append(J2EE_SERVER + "=").append(context.getJ2eeServerName(serverName));
            sep = ",";
        }
        if ("*".equals(applicationName)) {
            query = true;
        } else {
            buffer.append(sep).append(J2EE_APPLICATION + "=").append(context.getJ2eeApplicationName(applicationName));
            sep = ",";
        }
        if ("*".equals(moduleName)) {
            query = true;
        } else {
            buffer.append(sep).append(context.getJ2eeModuleType(moduleType)).append("=").append(context.getJ2eeModuleName(moduleName));
            sep = ",";
        }
        if ("*".equals(type)) {
            query = true;
        } else {
            buffer.append(sep).append(J2EE_TYPE + "=").append(context.getJ2eeType(type));
            sep = ",";
        }
        //explicit attributes, must be included: if * then make a query
        if ("*".equals(name)) {
            query = true;
        } else {
            buffer.append(sep).append(J2EE_NAME + "=").append(context.getJ2eeName(name));
            sep = ",";
        }
        //make a query, possibly
        if (query) {
            buffer.append(sep).append("*");
        }
        return ObjectName.getInstance(buffer.toString());
    }

    public static ObjectName getComponentNameQuery(String domainName, String serverName, String applicationName, String name, String type, J2eeContext context) throws MalformedObjectNameException {
        return getComponentNameQuery(domainName, serverName, applicationName, null, "*", name, type, context);
    }

    public static ObjectName getComponentInModuleQuery(String domainName, String serverName, String applicationName, String moduleType, String moduleName, String type, J2eeContext context) throws MalformedObjectNameException {
        return getComponentNameQuery(domainName, serverName, applicationName, moduleType, moduleName, "*", type, context);
    }

    /**
     * Creates a query for components that are in no application with given name.
     *
     * @param domainName
     * @param serverName
     * @param name
     * @param type
     * @param context
     * @return
     * @throws MalformedObjectNameException
     */

    public static ObjectName getComponentRestrictedQueryName(String domainName, String serverName, String name, String type, J2eeContext context) throws MalformedObjectNameException {
        return getComponentNameQuery(domainName, serverName, NULL, null, "*", name, type, context);
    }

    public static ObjectName getWebComponentName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleName, String j2eeName, String j2eeType, J2eeContext context) throws MalformedObjectNameException {
        return getComponentName(j2eeDomainName, j2eeServerName, j2eeApplicationName, WEB_MODULE, j2eeModuleName, j2eeName, j2eeType, context);
    }

    //TODO THIS IS KIND OF WEIRD. Is there a better way???
    public static ObjectName getWebFilterMappingName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleName, String filterName, String servletName, String urlPattern, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, WEB_FILTER_MAPPING);
        props.put(J2EE_SERVER, context.getJ2eeServerName(j2eeServerName));
        props.put(J2EE_APPLICATION, context.getJ2eeApplicationName(j2eeApplicationName));
        props.put(WEB_MODULE, context.getJ2eeModuleName(j2eeModuleName));
        props.put(WEB_FILTER, filterName);
        if (servletName != null) {
            props.put(SERVLET, servletName);
        } else {
            props.put(URL_PATTERN, ObjectName.quote(urlPattern));
        }
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    //for non-j2ee-deployable resources such as the transaction manager
    public static ObjectName getComponentName(String j2eeDomainName, String j2eeServerName, String j2eeApplicationName, String j2eeModuleName, String j2eeName, String j2eeType, J2eeContext context) throws MalformedObjectNameException {
        Properties props = new Properties();
        props.put(J2EE_TYPE, context.getJ2eeType(j2eeType));
        props.put(J2EE_SERVER, context.getJ2eeServerName(j2eeServerName));
        props.put(J2EE_NAME, context.getJ2eeName(j2eeName));
        props.put(J2EE_APPLICATION, context.getJ2eeApplicationName(j2eeApplicationName));
        //TODO add module type
        if (context.getJ2eeModuleName(j2eeModuleName) != null) {
            props.put(J2EE_MODULE, context.getJ2eeModuleName(j2eeModuleName));
        }
        return ObjectName.getInstance(context.getJ2eeDomainName(j2eeDomainName), props);
    }

    //TODO parameterize this
    public static ObjectName getSecurityRealmName(String realmName) throws MalformedObjectNameException {
        return ObjectName.getInstance("geronimo.security:type=SecurityRealm,name=" + realmName);
    }
}
