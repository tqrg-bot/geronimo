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
package org.apache.geronimo.kernel.config;

import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamConstants;
import java.io.PushbackInputStream;
import java.util.Collection;

/**
 * @version $Rev$ $Date$
 */
public class SerializedConfigurationMarshaler implements ConfigurationMarshaler {
    private static byte[] SERIALIZED_MAGIC = new byte[] {
            (byte) ((ObjectStreamConstants.STREAM_MAGIC >>> 8) & 0xFF),
            (byte) ((ObjectStreamConstants.STREAM_MAGIC >>> 0) & 0xFF)
    };

    public ConfigurationData readConfigurationData(InputStream in) throws IOException, ClassNotFoundException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(in, 2);
        byte[] streamHeader = new byte[2];
        if (pushbackInputStream.read(streamHeader) != 2) throw new AssertionError("Cound not read stream header");
        pushbackInputStream.unread(streamHeader);

        // if this is a serialized config, fallback to the serialization marshaler
        if (SERIALIZED_MAGIC[0] != streamHeader[0] || SERIALIZED_MAGIC[1] != streamHeader[1]) {
            ConfigurationMarshaler marshaler;
            try {
                marshaler = ConfigurationUtil.createConfigurationMarshaler("org.apache.geronimo.kernel.config.xstream.XStreamConfigurationMarshaler");
            } catch (Throwable ignored) {
                throw new IOException("Input does not contain a Java Object Serialization stream");
            }
            return marshaler.readConfigurationData(pushbackInputStream);

        }

        ObjectInputStream oin = new ObjectInputStream(pushbackInputStream);
        try {
            return (ConfigurationData) oin.readObject();
        } finally {
            oin.close();
        }
    }

    public void writeConfigurationData(ConfigurationData configurationData, OutputStream out) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(out);
        try {
            oout.writeObject(configurationData);
        } finally {
            if (oout != null) {
                try {
                    oout.flush();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public GBeanState newGBeanState(Collection gbeans) {
        return new SerializedGBeanState(gbeans);
    }
}
