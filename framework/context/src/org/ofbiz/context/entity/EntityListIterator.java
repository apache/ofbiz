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

package org.ofbiz.context.entity;


import java.util.List;
import java.util.ListIterator;


/**
 * Generic Entity Cursor List Iterator for Handling Cursored DB Results
 */
public interface EntityListIterator extends ListIterator<GenericValue> {

    public void setDelegator(GenericDelegator delegator);

    /** Sets the cursor position to just after the last result so that previous() will return the last result */
    public void afterLast() throws GenericEntityException;

    /** Sets the cursor position to just before the first result so that next() will return the first result */
    public void beforeFirst() throws GenericEntityException;

    /** Sets the cursor position to last result; if result set is empty returns false */
    public boolean last() throws GenericEntityException;

    /** Sets the cursor position to last result; if result set is empty returns false */
    public boolean first() throws GenericEntityException;

    public void close() throws GenericEntityException;

    /** NOTE: Calling this method does return the current value, but so does calling next() or previous(), so calling one of those AND this method will cause the value to be created twice */
    public GenericValue currentGenericValue() throws GenericEntityException;

    public int currentIndex() throws GenericEntityException;

    /** performs the same function as the ResultSet.absolute method;
     * if rowNum is positive, goes to that position relative to the beginning of the list;
     * if rowNum is negative, goes to that position relative to the end of the list;
     * a rowNum of 1 is the same as first(); a rowNum of -1 is the same as last()
     */
    public boolean absolute(int rowNum) throws GenericEntityException;

    /** performs the same function as the ResultSet.relative method;
     * if rows is positive, goes forward relative to the current position;
     * if rows is negative, goes backward relative to the current position;
     */
    public boolean relative(int rows) throws GenericEntityException;

    /**
     * PLEASE NOTE: Because of the nature of the JDBC ResultSet interface this method can be very inefficient; it is much better to just use next() until it returns null
     * For example, you could use the following to iterate through the results in an EntityListIterator:
     *
     *      GenericValue nextValue = null;
     *      while ((nextValue = (GenericValue) this.next()) != null) { ... }
     *
     */
    public boolean hasNext();

    /** PLEASE NOTE: Because of the nature of the JDBC ResultSet interface this method can be very inefficient; it is much better to just use previous() until it returns null */
    public boolean hasPrevious();

    /** Moves the cursor to the next position and returns the GenericValue object for that position; if there is no next, returns null
     * For example, you could use the following to iterate through the results in an EntityListIterator:
     *
     *      GenericValue nextValue = null;
     *      while ((nextValue = (GenericValue) this.next()) != null) { ... }
     *
     */
    public GenericValue next();

    /** Returns the index of the next result, but does not guarantee that there will be a next result */
    public int nextIndex();

    /** Moves the cursor to the previous position and returns the GenericValue object for that position; if there is no previous, returns null */
    public GenericValue previous();

    /** Returns the index of the previous result, but does not guarantee that there will be a previous result */
    public int previousIndex();

    public void setFetchSize(int rows) throws GenericEntityException;

    public List<GenericValue> getCompleteList() throws GenericEntityException;

    /** Gets a partial list of results starting at start and containing at most number elements.
     * Start is a one based value, ie 1 is the first element.
     */
    public List<GenericValue> getPartialList(int start, int number) throws GenericEntityException;

    public int getResultsSizeAfterPartialList() throws GenericEntityException;
}
