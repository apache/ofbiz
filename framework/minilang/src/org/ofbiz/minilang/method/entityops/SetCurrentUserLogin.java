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
package org.ofbiz.minilang.method.entityops;

import org.ofbiz.base.util.collections.FlexibleMapAccessor;
import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.minilang.MiniLangException;
import org.ofbiz.minilang.MiniLangRuntimeException;
import org.ofbiz.minilang.MiniLangValidate;
import org.ofbiz.minilang.SimpleMethod;
import org.ofbiz.minilang.method.MethodContext;
import org.ofbiz.minilang.method.MethodOperation;
import org.w3c.dom.Element;

/**
 * Implements the &lt;set-current-user-login&gt; element.
 */
public final class SetCurrentUserLogin extends MethodOperation {

    private final FlexibleMapAccessor<GenericValue> valueFma;

    public SetCurrentUserLogin(Element element, SimpleMethod simpleMethod) throws MiniLangException {
        super(element, simpleMethod);
        if (MiniLangValidate.validationOn()) {
            MiniLangValidate.handleError("Deprecated - use the called service's userLogin IN attribute", simpleMethod, element);
            MiniLangValidate.attributeNames(simpleMethod, element, "value-field");
            MiniLangValidate.requiredAttributes(simpleMethod, element, "value-field");
            MiniLangValidate.expressionAttributes(simpleMethod, element, "value-field");
            MiniLangValidate.noChildElements(simpleMethod, element);
        }
        valueFma = FlexibleMapAccessor.getInstance(element.getAttribute("value-field"));
    }

    @Override
    public boolean exec(MethodContext methodContext) throws MiniLangException {
        GenericValue userLogin = valueFma.get(methodContext.getEnvMap());
        if (userLogin == null) {
            throw new MiniLangRuntimeException("Entity value not found with name: " + valueFma, this);
        }
        methodContext.setUserLogin(userLogin, this.simpleMethod.getUserLoginEnvName());
        return true;
    }

    @Override
    public String expandedString(MethodContext methodContext) {
        return FlexibleStringExpander.expandString(toString(), methodContext.getEnvMap());
    }

    @Override
    public String rawString() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<set-current-user-login ");
        sb.append("value-field=\"").append(this.valueFma).append("\" />");
        return sb.toString();
    }

    /**
     * A factory for the &lt;set-current-user-login&gt; element.
     */
    public static final class SetCurrentUserLoginFactory implements Factory<SetCurrentUserLogin> {
        @Override
        public SetCurrentUserLogin createMethodOperation(Element element, SimpleMethod simpleMethod) throws MiniLangException {
            return new SetCurrentUserLogin(element, simpleMethod);
        }

        @Override
        public String getName() {
            return "set-current-user-login";
        }
    }
}
