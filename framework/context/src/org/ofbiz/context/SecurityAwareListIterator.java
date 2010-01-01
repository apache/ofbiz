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

import java.util.ListIterator;
import java.util.Set;

/**
 * SecurityAwareListIterator class.  This class decorates a <code>
 * ListIterator</code> instance and filters a list of
 * <code>Object</code>s based on a set of permission services.
 * <p>The permission service must implement <code>permissionInterface</code>
 * and accept an optional <code>candidateObject</code> parameter (parameter
 * type is <code>java.lang.Object</code>). The service should
 * return <code>hasPermission = true</code> if the user is granted access
 * to the <code>candidateObject</code>.</p>
 */
public class SecurityAwareListIterator<E> extends SecurityAwareIterator<E> implements ListIterator<E> {

    public static final String module = SecurityAwareListIterator.class.getName();
    protected final ListIterator<E> listIterator;
    protected E previousValue = null;
    protected int index = 0;

    public SecurityAwareListIterator(ListIterator<E> iterator, Set<String> serviceNameList) {
        super(iterator, serviceNameList);
        this.listIterator = iterator;
    }

    protected void getPrevious() {
        // Unusual loop for EntityListIterator compatibility
        E value = null;
        try {
            value = this.listIterator.previous();
        } catch (Exception e) {}
        while (value != null) {
            if (this.hasPermission(value)) {
                this.index--;
                this.previousValue = value;
                return;
            }
            value = null;
            try {
                value = this.listIterator.previous();
            } catch (Exception e) {}
        }
    }

    public E next() {
        E value = super.next();
        if (value != null) {
            this.index++;
        }
        return value;
    }

    public void add(E o) {
        this.listIterator.add(o);
    }

    public boolean hasPrevious() {
        return this.previousValue != null;
    }

    public int nextIndex() {
        return this.index + 1;
    }

    public E previous() {
        E value = this.previousValue;
        this.previousValue = null;
        this.getPrevious();
        return value;
    }

    public int previousIndex() {
        return this.index - 1;
    }

    public void set(E o) {
        this.listIterator.set(o);
    }
}
