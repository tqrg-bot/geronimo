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

package org.apache.geronimo.commands

import org.apache.geronimo.gshell.command.Command
import org.apache.geronimo.gshell.command.CommandSupport
import org.apache.geronimo.gshell.command.CommandException

import org.apache.geronimo.gshell.clp.Option

import org.apache.tools.ant.ExitStatusException

// Make sure we use our custom builder
import org.apache.geronimo.commands.AntBuilder

/**
 * Starts a new Geronimo server instance.
 *
 * @version $Id$
 */
class StartServerCommand
    extends CommandSupport
{
    private AntBuilder ant
    
    @Option(name='-H', aliases=['--home'], description='Use a specific Geronimo home directory')
    File geronimoHome
    
    @Option(name='-j', aliases=['--jvm'], description='Use a specific Java Virtual Machine for server process')
    File javaVirtualMachine
    
    @Option(name='-A', aliases=['--javaagent'], description='Use a specific Java Agent, set to \'none\' to disable')
    String javaAgent
    
    @Option(name='-l', aliases=['--logfile'], description='Capture console output to file')
    File logFile
    
    @Option(name='-b', aliases=['--background'], description='Run the server process in the background')
    boolean background = false
    
    @Option(name='-q', aliases=['--quiet'], description='Suppress informative and warning messages')
    boolean quiet = false
    
    @Option(name='-v', aliases=['--verbose'], description='Enable verbose output; specify multipule times to increase verbosity')
    int verbose = 0
    
    @Option(name='-t', aliases=['--timeout'], description='Specify the timeout for the server process in seconds')
    int timeout = -1
    
    final Map properties = [:]
    
    final List javaFlags = []
    
    final List startModules = []
    
    String hostname = 'localhost'
    
    int port = 1099
    
    String username = 'system'
    
    String password = 'manager'
    
    StartServerCommand() {
        super('start-server')
    }
    
    /*
    protected Options getOptions() {
        options.addOption(OptionBuilder.withLongOpt('property')
            .withDescription(messages.getMessage('cli.option.property'))
            .hasArg()
            .withArgName('name=value')
            .create('D'))
        
        options.addOption(OptionBuilder.withLongOpt('gproperty')
            .withDescription(messages.getMessage('cli.option.gproperty'))
            .hasArg()
            .withArgName('name=value')
            .create('G'))

        options.addOption(OptionBuilder.withLongOpt('javaopt')
            .withDescription(messages.getMessage('cli.option.javaopt'))
            .hasArg()
            .withArgName('option')
            .create('J'))
        
        options.addOption(OptionBuilder.withLongOpt('module')
            .withDescription(messages.getMessage('cli.option.module'))
            .hasArg()
            .withArgName('name')
            .create('m'))
        
        //
        // TODO: Expose URL, username/password for validation auth
        //
        
        return options
    }
    */
    
    /*
    protected boolean processCommandLine(final CommandLine line) throws CommandException {
        if (line.hasOption('v')) {
            line.options.each {
                if (it.opt == 'v') {
                    verbose++
                }
            }
        }
        
        def addProperty = { namevalue, prefix ->
            def name, value
            int i = namevalue.indexOf('=')
            
            if (i == -1) {
                name = namevalue
                value = true
            }
            else {
                name = namevalue.substring(0, i)
                value = namevalue.substring(i + 1, namevalue.size())
            }
            name = name.trim()
            
            if (prefix) {
                name = "${prefix}.$name"
            }
            
            properties[name] = value
        }
        
        if (line.hasOption('D')) {
            def values = line.getOptionValues('D')
            
            values.each {
                addProperty(it)
            }
        }
        
        if (line.hasOption('G')) {
            def values = line.getOptionValues('G')
            
            values.each {
                addProperty(it, 'org.apache.geronimo')
            }
        }
        
        if (line.hasOption('J')) {
            def values = line.getOptionValues('J')
            
            values.each {
                javaFlags << it
            }
        }
        
        if (line.hasOption('m')) {
            def values = line.getOptionValues('m')
            
            values.each {
                startModules << it
            }
        }
        
        return false
    }
    */
    
    private File getJavaAgentJar() {
        def file = new File(geronimoHome, 'bin/jpa.jar')
        
        if (javaAgent) {
            if (javaAgent.toLowerCase() == 'none') {
                file = null
            }
            else {
                file = new File(javaAgent)
                
                if (!file.exists()) {
                    log.warn("Missing Java Agent jar: $file")
                }
            }
        }
        
        return file
    }
    
    protected Object doExecute() throws Exception {
        def io = getIO()
        ant = new AntBuilder(log, io)
        
        if (!geronimoHome) {
            geronimoHome = new File(System.properties['gshell.home'])
        }
        log.debug("Geronimo home: $geronimoHome")
        
        // Setup default java flags
        if (javaAgentJar) {
            javaFlags << "-javaagent:${javaAgentJar.canonicalPath}"
        }
        
        // Setup the default properties required to boot the server
        properties['org.apache.geronimo.base.dir'] = geronimoHome
        properties['java.io.tmpdir'] = 'var/temp' // Use relative path
        properties['java.endorsed.dirs'] = prefixSystemPath('java.endorsed.dirs', new File(geronimoHome, 'lib/endorsed'))
        properties['java.ext.dirs'] = prefixSystemPath('java.ext.dirs', new File(geronimoHome, 'lib/ext'))
        
        processScripts()
        
        // If we are not backgrounding, then add a nice message for the user when ctrl-c gets hit
        if (!background) {
            addShutdownHook({
                io.out.println('Shutting down...')
                io.flush()
            })
        }
        
        def launcher = new ProcessLauncher(log: log, io: io, name: 'Geronimo Server', background: background)
        
        //
        // TODO: Add spawn support?
        //
        
        launcher.process = {
            try {
                ant.java(jar: "$geronimoHome/bin/server.jar", dir: geronimoHome, failonerror: true, fork: true) {
                    def node = current.wrapper
                    
                    if (timeout > 0) {
                        log.info("Timeout after: ${timeout} seconds")
                        node.setAttribute('timeout', "${timeout * 1000}")
                    }
                    
                    if (logFile) {
                        log.info("Redirecting output to: $logFile")
                        logFile.parentFile.mkdirs()
                        redirector(output: logFile)
                    }
                    
                    if (javaVirtualMachine) {
                        if (!javaVirtualMachine.exists()) {
                            fail("Java virtual machine is not valid: $javaVirtualMachine")
                        }
                        
                        log.info("Using Java virtual machine: $javaVirtualMachine")
                        node.setAttribute('jvm', javaVirtualMachine)
                    }
                    
                    javaFlags.each {
                        jvmarg(value: it)
                    }
                    
                    properties.each { key, value ->
                        sysproperty(key: key, value: value)
                    }
                    
                    if (quiet) {
                        arg(value: '--quiet')
                    }
                    else {
                        arg(value: '--long')
                    }
                    
                    if (verbose == 1) {
                        arg(value: '--verbose')
                    }
                    else if (verbose > 1) {
                        arg(value: '--veryverbose')
                    }
                    
                    if (startModules) {
                        log.info('Overriding the set of modules to be started')
                        
                        arg(value: '--override')
                        
                        startModules.each {
                            arg(value: it)
                        }
                    }
                }
            }
            catch (ExitStatusException e) {
                def tmp = log.&info
                
                if (e.status != 0) {
                    tmp = log.&warn
                }
                
                tmp("Process exited with status: $e.status")
                
                throw e
            }
        }
        
        def server = new ServerProxy(hostname, port, username, password)
        
        launcher.verifier = {
            return server.fullyStarted
        }
        
        launcher.launch()
        
        return Command.SUCCESS
    }
    
    /**
     * Process custom rc.d scripts.
     */
    private void processScripts() {
        //
        // FIXME: Make the base directory configurable
        //
        
        def basedir = new File(geronimoHome, 'etc/rc.d')
        if (!basedir.exists()) {
            log.debug("Skipping script processing; missing base directory: $basedir")
            return
        }
        
        def scanner = ant.fileScanner {
            fileset(dir: basedir) {
                include(name: "${this.name},*.groovy")
            }
        }
        
        def binding = new Binding([command: this, log: log, io: getIO()])
        def shell = new GroovyShell(binding)
        
        for (file in scanner) {
            log.debug("Evaluating script: $file")
            
            // Use InputStream method to avoid classname problems from the file's name
            shell.evaluate(file.newInputStream())
        }
    }
    
    private String prefixSystemPath(final String name, final File file) {
        assert name
        assert file

        def path = file.path
        def prop = System.getProperty(name, '')
        if (prop) {
            path += File.pathSeparator + prop
        }
        
        return path
    }
}
