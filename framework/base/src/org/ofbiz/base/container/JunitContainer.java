/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.base.container;

import java.util.Enumeration;
import java.util.Iterator;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.ObjectType;


public class JunitContainer implements Container {

    public static final String module = JunitContainer.class.getName();
    protected TestResult results = null;
    protected String configFile = null;

    /**
     * @see org.ofbiz.base.container.Container#init(java.lang.String[], java.lang.String)
     */
    public void init(String[] args, String configFile) {
        this.configFile = configFile;
    }

    public boolean start() throws ContainerException {
        ContainerConfig.Container jc = ContainerConfig.getContainer("junit-container", configFile);

        // get the tests to run
        Iterator<ContainerConfig.Container.Property> ti = jc.properties.values().iterator();
        if (ti == null) {
            Debug.logInfo("No tests to load", module);
            return true;
        }

        // load the tests into the suite
        TestSuite suite = new TestSuite();
        while (ti.hasNext()) {
            ContainerConfig.Container.Property prop = ti.next();
            Class<?> clz = null;
            try {
                clz = ObjectType.loadClass(prop.value);
                suite.addTestSuite(clz);
            } catch (Exception e) {
                Debug.logError(e, "Unable to load test suite class : " + prop.value, module);
            }
        }

        // holder for the results
        results = new TestResult();

        // run the tests
        suite.run(results);

        // dispay the results
        Debug.logInfo("[JUNIT] Pass: " + results.wasSuccessful() + " | # Tests: " + results.runCount() + " | # Failed: " +
                results.failureCount() + " # Errors: " + results.errorCount(), module);
        if (Debug.infoOn()) {
            Debug.logInfo("[JUNIT] ----------------------------- ERRORS ----------------------------- [JUNIT]", module);
            Enumeration<?> err = results.errors();
            if (!err.hasMoreElements()) {
                Debug.logInfo("None", module);
            } else {
                while (err.hasMoreElements()) {
                    Debug.logInfo("--> " + err.nextElement(), module);
                }
            }
            Debug.logInfo("[JUNIT] ------------------------------------------------------------------ [JUNIT]", module);
            Debug.logInfo("[JUNIT] ---------------------------- FAILURES ---------------------------- [JUNIT]", module);
            Enumeration<?> fail = results.failures();
            if (!fail.hasMoreElements()) {
                Debug.logInfo("None", module);
            } else {
                while (fail.hasMoreElements()) {
                    Debug.logInfo("--> " + fail.nextElement(), module);
                }
            }
            Debug.logInfo("[JUNIT] ------------------------------------------------------------------ [JUNIT]", module);
        }

        return true;
    }

    public void stop() throws ContainerException {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }
}
