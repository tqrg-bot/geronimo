/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Geronimo" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Geronimo", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * ====================================================================
 */
package org.apache.geronimo.kernel.service;

import javax.management.MBeanParameterInfo;

import org.apache.geronimo.kernel.service.ParserUtil;
import org.apache.geronimo.kernel.service.GeronimoOperationInfo;

/**
 * Describes a parameter of an operation.  This extension allows the properties to be mutable during setup,
 * and once the MBean is deployed an imutable copy of will be made.
 *
 * @version $Revision: 1.3 $ $Date: 2003/11/14 16:17:33 $
 */
public final class GeronimoParameterInfo extends MBeanParameterInfo {
    private final int hashCode = System.identityHashCode(this);
    private final boolean immutable;
    private String name;
    private String type;
    private String description;
    private final Class typeClass;

    public GeronimoParameterInfo() {
        this(null, null, null);
    }

    public GeronimoParameterInfo(String name, String type, String description) {
        super(null, null, null);
        immutable = false;
        typeClass = null;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    GeronimoParameterInfo(GeronimoParameterInfo source, GeronimoOperationInfo parent) {
        super("Ignore", "Ignore", null);
        this.immutable = true;

        //
        // Required
        //
        type = source.type;
        if (type == null) {
            throw new IllegalArgumentException("Parameter type is null: " + name);
        }

        //
        // Optional
        //
        name = source.name;
        description = source.description;

        //
        // Derived
        //
        try {
            typeClass = ParserUtil.loadClass(getType());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not load parameter type class: name=" + name + " type=" + type);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (immutable) {
            throw new IllegalStateException("Data is no longer mutable");
        }
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (immutable) {
            throw new IllegalStateException("Data is no longer mutable");
        }
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (immutable) {
            throw new IllegalStateException("Data is no longer mutable");
        }
        this.description = description;
    }

    Class getTypeClass() {
        return typeClass;
    }

    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object object) {
        return object == this;
    }

    public String toString() {
        return "[GeronimoParameterInfo: name=" + name + " type=" + type + " description=" + description + "]";
    }
}
