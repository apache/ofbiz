package org.ofbiz.entity.util;

import java.util.List;
import java.util.ListIterator;

import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;

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

    public void setFetchSize(int rows) throws GenericEntityException;

    public List<GenericValue> getCompleteList() throws GenericEntityException;

    /** Gets a partial list of results starting at start and containing at most number elements.
     * Start is a one based value, ie 1 is the first element.
     */
    public List<GenericValue> getPartialList(int start, int number)
            throws GenericEntityException;

    public int getResultsSizeAfterPartialList() throws GenericEntityException;

}