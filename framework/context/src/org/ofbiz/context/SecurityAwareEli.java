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

import java.util.List;
import java.util.Set;

import javolution.util.FastList;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityListIterator;

/**
 * SecurityAwareEli class.  This class decorates an <code>
 * EntityListIterator</code> instance and filters a list of
 * <code>GenericValue</code>s based on a set of permission services.
 * <p>The permission service must implement <code>permissionInterface</code>
 * and accept an optional <code>candidateObject</code> parameter (parameter
 * type is <code>java.lang.Object</code>). The service should
 * return <code>hasPermission = true</code> if the user is granted access
 * to the <code>candidateObject</code>.</p>
 */
public class SecurityAwareEli extends SecurityAwareListIterator<GenericValue> {

    public static final String module = SecurityAwareEli.class.getName();
    protected final EntityListIterator listIterator;
    protected GenericValue previousValue = null;

    public SecurityAwareEli(EntityListIterator iterator, Set<String> serviceNameList) {
        super(iterator, serviceNameList);
        this.listIterator = iterator;
    }

    public boolean absolute(int rowNum) throws GenericEntityException {
        return this.listIterator.absolute(rowNum);
    }

    public void afterLast() throws GenericEntityException {
        this.listIterator.afterLast();
    }

    public void beforeFirst() throws GenericEntityException {
        this.listIterator.beforeFirst();
    }

    public void close() throws GenericEntityException {
        this.listIterator.close();
    }

    public GenericValue currentGenericValue() throws GenericEntityException {
        GenericValue value = this.listIterator.currentGenericValue();
        while (value != null && !this.hasPermission(value)) {
            value = this.next();
        }
        return value;
    }

    public int currentIndex() throws GenericEntityException {
        return this.listIterator.currentIndex();
    }

    public boolean first() throws GenericEntityException {
        return this.listIterator.first();
    }

    public List<GenericValue> getCompleteList() throws GenericEntityException {
        List<GenericValue> list = FastList.newInstance();
        GenericValue nextValue = this.next();
        while (nextValue != null) {
            list.add(nextValue);
            nextValue = this.next();
        }
        return list;
    }

    public List<GenericValue> getPartialList(int start, int number) throws GenericEntityException {
        List<GenericValue> list = FastList.newInstance();
        if (number == 0) {
            return list;
        }
        if (start == 0) {
            start = 1;
        }
        GenericValue nextValue = null;
        if (start == 1) {
            nextValue = this.next();
            if (nextValue == null) {
                return list;
            }
        } else {
            nextValue = this.getAbsolute(start);
        }
        int numRetreived = 1;
        while (number > numRetreived && nextValue != null) {
            list.add(nextValue);
            numRetreived++;
            nextValue = this.next();
        }
        return list;
    }

    protected GenericValue getAbsolute(int start) throws GenericEntityException {
        if (!this.absolute(start)) {
            return null;
        }
        return this.currentGenericValue();
    }

    public int getResultsSizeAfterPartialList() throws GenericEntityException {
        return this.listIterator.getResultsSizeAfterPartialList();
    }

    public boolean last() throws GenericEntityException {
        return this.listIterator.last();
    }

    public boolean relative(int rows) throws GenericEntityException {
        return this.listIterator.relative(rows);
    }

    public void setDelegator(GenericDelegator delegator) {
        this.listIterator.setDelegator(delegator);
    }

    public void setFetchSize(int rows) throws GenericEntityException {
        this.listIterator.setFetchSize(rows);
    }

}
