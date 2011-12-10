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
package org.ofbiz.widget.form;

import java.io.IOException;

import org.ofbiz.base.util.GeneralException;
import org.ofbiz.widget.form.ModelFormField.CheckField;
import org.ofbiz.widget.form.ModelFormField.ContainerField;
import org.ofbiz.widget.form.ModelFormField.DateTimeField;
import org.ofbiz.widget.form.ModelFormField.DisplayField;
import org.ofbiz.widget.form.ModelFormField.DropDownField;
import org.ofbiz.widget.form.ModelFormField.HiddenField;
import org.ofbiz.widget.form.ModelFormField.HyperlinkField;
import org.ofbiz.widget.form.ModelFormField.IgnoredField;
import org.ofbiz.widget.form.ModelFormField.ImageField;
import org.ofbiz.widget.form.ModelFormField.RadioField;
import org.ofbiz.widget.form.ModelFormField.ResetField;
import org.ofbiz.widget.form.ModelFormField.SubmitField;
import org.ofbiz.widget.form.ModelFormField.TextField;
import org.ofbiz.widget.form.ModelFormField.TextareaField;

/**
 * Form widget visitor.
 */
public interface FormWidgetVisitor {

    void visit(CheckField checkField) throws IOException, GeneralException;

    void visit(ContainerField containerField) throws IOException, GeneralException;

    void visit(DateTimeField dateTimeField) throws IOException, GeneralException;

    void visit(DisplayField displayField) throws IOException, GeneralException;

    void visit(DropDownField dropDownField) throws IOException, GeneralException;

    void visit(HiddenField hiddenField) throws IOException, GeneralException;

    void visit(HyperlinkField hyperlinkField) throws IOException, GeneralException;

    void visit(IgnoredField ignoredField) throws IOException, GeneralException;

    void visit(ImageField imageField) throws IOException, GeneralException;

    void visit(ModelForm modelForm) throws IOException, GeneralException;

    void visit(ModelFormField modelFormField) throws IOException, GeneralException;

    void visit(RadioField radioField) throws IOException, GeneralException;

    void visit(ResetField resetField) throws IOException, GeneralException;

    void visit(SubmitField submitField) throws IOException, GeneralException;

    void visit(TextareaField textareaField) throws IOException, GeneralException;

    void visit(TextField textField) throws IOException, GeneralException;

}
