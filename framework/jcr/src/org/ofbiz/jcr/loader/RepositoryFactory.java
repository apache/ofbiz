/*
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
 */
package org.ofbiz.jcr.loader;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.apache.commons.collections.map.ReferenceMap;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.TransientRepository;

public class RepositoryFactory implements ObjectFactory {

    private static final Map<Object, Object> cache = new ReferenceMap();

    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws RepositoryException {
        synchronized (cache) {
            Object instance = cache.get(obj);
            if (instance == null && obj instanceof Reference) {
                Reference reference = (Reference) obj;
                String repHomeDir = reference.get(JCRJndi.ADDR_TYPE_FOR_REPOSITORY_HOME_DIR).getContent().toString();
                // check if the repository is already started, than use it
                // otherwise create it
                File lock = new File(repHomeDir);
                if (lock.exists()) {
                    instance = JcrUtils.getRepository(lock.toURI().toString());
                } else {
                    instance = new TransientRepository(reference.get(JCRContainer.DEFAULT_JCR_CONFIG_PATH).getContent().toString(), repHomeDir);
                }

                cache.put(obj, instance);
            }

            return instance;
        }
    }

}
