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

import org.ofbiz.party.contact.ContactMechWorker;

if (parameters.partyId){
    if (! parameters.primaryId) parameters.primaryId = parameters.partyId;
    if (! parameters.entity)    parameters.entity = "Party";
} else if (parameters.facilityId){
    if (! parameters.primaryId) parameters.primaryId = parameters.facilityId;
    if (! parameters.entity)    parameters.entity = "Facility";
} else if (parameters.orderId){
    if (! parameters.primaryId) parameters.primaryId = parameters.orderId;
    if (! parameters.entity)    parameters.entity = "Order";
} else if (parameters.workEffortId){
    if (! parameters.primaryId) parameters.primaryId = parameters.workEffortId;
    if (! parameters.entity)    parameters.entity = "WorkEffort";
}

primaryId = parameters.primaryId;
showOld = "true".equals(parameters.SHOW_OLD);

if (parameters.entity == "Party")
    context.contactMeches = ContactMechWorker.getPartyContactMechValueMaps(delegator, primaryId, showOld);

if (parameters.entity == "Facility")
    context.contactMeches = ContactMechWorker.getFacilityContactMechValueMaps(delegator, primaryId, showOld, null);

if (parameters.entity == "Order")
    context.contactMeches = ContactMechWorker.getOrderContactMechValueMaps(delegator, primaryId);

if (parameters.entity == "WorkEffort")
    context.contactMeches = ContactMechWorker.getWorkEffortContactMechValueMaps(delegator, primaryId, showOld);

