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

package org.apache.geronimo.gbean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.apache.geronimo.kernel.management.NotificationType;

/**
 * Describes a GBean.
 *
 * @version $Rev$ $Date$
 */
public final class GBeanInfo implements Serializable {
    private static final Set DEFAULT_NOTIFICATIONS = Collections.unmodifiableSet(new HashSet(Arrays.asList(NotificationType.TYPES)));

    /**
     * Static helper to try to get the GBeanInfo from the class supplied.
     *
     * @param className name of the class to get the GBeanInfo from
     * @param classLoader the class loader use to load the specifiec class
     * @return GBeanInfo generated by supplied class
     * @throws org.apache.geronimo.gbean.InvalidConfigurationException if there is a problem getting the GBeanInfo from the class
     */
    public static GBeanInfo getGBeanInfo(String className, ClassLoader classLoader) throws InvalidConfigurationException {
        Class clazz;
        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new InvalidConfigurationException("Could not load class " + className, e);
        }
        Method method;
        try {
            method = clazz.getDeclaredMethod("getGBeanInfo", new Class[]{});
        } catch (NoSuchMethodException e) {
            throw new InvalidConfigurationException("Class does not have a getGBeanInfo() method: " + className);
        }
        try {
            return (GBeanInfo) method.invoke(clazz, new Object[]{});
        } catch (Exception e) {
            throw new InvalidConfigurationException("Could not get GBeanInfo from class: " + className, e);
        }
    }

    private final String name;
    private final String className;
    private final Set attributes;
    private final Map attributesByName;
    private final GConstructorInfo constructor;
    private final Set operations;
    private final Set notifications;
    private final Set references;

    public GBeanInfo(String className, Collection attributes, GConstructorInfo constructor, Collection operations, Set references) {
        this(className, className, attributes, constructor, operations, references, DEFAULT_NOTIFICATIONS);
    }

    public GBeanInfo(String name, String className, Collection attributes, GConstructorInfo constructor, Collection operations, Set references) {
        this(name, className, attributes, constructor, operations, references, DEFAULT_NOTIFICATIONS);
    }

    /**
     * @deprecated
     */
    public GBeanInfo(String className, Collection attributes, GConstructorInfo constructor, Collection operations, Set references, Set notifications) {
        this(className, className, attributes, constructor, operations, references, notifications);
    }

    /**
     * @deprecated
     */
    public GBeanInfo(String name, String className, Collection attributes, GConstructorInfo constructor, Collection operations, Set references, Set notifications) {
        this.name = name;
        this.className = className;
        if (attributes == null) {
            this.attributes = Collections.EMPTY_SET;
            this.attributesByName = Collections.EMPTY_MAP;
        } else {
            Map map = new HashMap();
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
                GAttributeInfo attribute = (GAttributeInfo) iterator.next();
                map.put(attribute.getName(), attribute);
            }
            this.attributesByName = Collections.unmodifiableMap(map);
            this.attributes = Collections.unmodifiableSet(new HashSet(map.values()));
        }
        if (constructor == null) {
            this.constructor = new GConstructorInfo(Collections.EMPTY_LIST);
        } else {
            this.constructor = constructor;
        }
        if (operations == null) {
            this.operations = Collections.EMPTY_SET;
        } else {
            this.operations = Collections.unmodifiableSet(new HashSet(operations));
        }
        if (references == null) {
            this.references = Collections.EMPTY_SET;
        } else {
            this.references = Collections.unmodifiableSet(new HashSet(references));
        }
        if (notifications == null) {
            this.notifications = Collections.EMPTY_SET;
        } else {
            this.notifications = Collections.unmodifiableSet(new HashSet(notifications));
        }
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public GAttributeInfo getAttribute(String name) {
        return (GAttributeInfo) attributesByName.get(name);
    }
    
    public Set getAttributes() {
        return attributes;
    }

    public List getPersistentAttributes() {
        List attrs = new ArrayList();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            GAttributeInfo info = (GAttributeInfo) i.next();
            if (info.isPersistent()) {
                attrs.add(info);
            }
        }
        return attrs;
    }

    public GConstructorInfo getConstructor() {
        return constructor;
    }

    public Set getOperations() {
        return operations;
    }

    public Set getNotifications() {
        return notifications;
    }

    public Set getReferences() {
        return references;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("[GBeanInfo: id=").append(super.toString()).append(" name=").append(name);
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            GAttributeInfo geronimoAttributeInfo = (GAttributeInfo) iterator.next();
            result.append("\n    attribute: ").append(geronimoAttributeInfo);
        }
        for (Iterator iterator = operations.iterator(); iterator.hasNext();) {
            GOperationInfo geronimoOperationInfo = (GOperationInfo) iterator.next();
            result.append("\n    operation: ").append(geronimoOperationInfo);
        }
        for (Iterator iterator = references.iterator(); iterator.hasNext();) {
            GReferenceInfo referenceInfo = (GReferenceInfo) iterator.next();
            result.append("\n    reference: ").append(referenceInfo);
        }
        result.append("]");
        return result.toString();
    }
}