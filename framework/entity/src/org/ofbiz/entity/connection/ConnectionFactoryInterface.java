package org.ofbiz.entity.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.ofbiz.context.entity.GenericEntityException;
import org.w3c.dom.Element;

/**
 * ConnectionFactoryInterface
 */
public interface ConnectionFactoryInterface {

    public Connection getConnection(String helperName, Element configElement) throws SQLException, GenericEntityException;
    public void closeAll();
}
