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

package org.apache.geronimo.buildsupport

import org.codehaus.mojo.groovy.GroovyMojoSupport

import org.apache.maven.project.MavenProject

/**
 * Helper to copy XmlBeans schemas.
 *
 * @goal copy-xmlbeans-schemas
 *
 * @version $Rev$ $Date$
 */
class CopyXmlBeansSchemas
    extends GroovyMojoSupport
{
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project
    
    void execute() {
        //
        // FIXME: Change this to reflect its a hack for xmlbeans not clover
        //
        def dir = new File(project.basedir, 'target/clover/classes')
        ant.mkdir(dir: dir)
        
        ant.copy(todir: dir) {
            fileset(dir: project.build.outputDirectory) {
                include(name: 'schemaorg_apache_xmlbeans/**')
            }
        }
    }
}

