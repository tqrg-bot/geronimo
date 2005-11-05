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
package org.apache.geronimo.console.infomanager;

import java.util.TreeMap;
import java.util.Map;

/**
 * A Map that can remove items as they are accessed, which makes it
 * easier to show a list of the remaining items.
 *
 * @version $Rev: 46019 $ $Date: 2004-09-14 05:56:06 -0400 (Tue, 14 Sep 2004) $
 */
public class ShrinkingMap extends TreeMap {
    private boolean shrinking = false;

    public ShrinkingMap() {
    }

    public ShrinkingMap(Map defaults) {
        super(defaults);
    }

    public boolean isShrinking() {
        return shrinking;
    }

    public void setShrinking(boolean shrinking) {
        this.shrinking = shrinking;
    }

    public synchronized Object get(Object key) {
        if(shrinking) {
            return super.remove(key);
        } else {
            return super.get(key);
        }
    }

    public Map getRemainingItems() {
        shrinking = false;
        return this;
    }
}
