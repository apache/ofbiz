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
package org.ofbiz.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * SecurityAwareList class.
 */
@SuppressWarnings("serial")
public class SecurityAwareList<E> extends ArrayList<E> implements List<E> {

    protected final static String module = SecurityAwareList.class.getName();
    protected final Set<String> serviceNameList;

    public SecurityAwareList(List<E> valueList, Set<String> serviceNameList) {
        super(valueList.size());
        this.addAll(valueList);
        this.trimToSize();
        this.serviceNameList = serviceNameList;
    }

    @Override
    public Iterator<E> iterator() {
        return new SecurityAwareIterator<E>(super.iterator(), this.serviceNameList);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SecurityAwareListIterator<E>(super.listIterator(), this.serviceNameList);
    }
}
