/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jpa.blueprint.impl;

import javax.persistence.EntityManager;

import org.osgi.service.coordinator.Coordination;
import org.osgi.service.coordinator.Participant;

final class JPAParticipant implements Participant {
    private final EntityManager em;

    JPAParticipant(EntityManager em) {
        this.em = em;
    }

    @Override
    public void failed(Coordination coordination) throws Exception {
        em.getTransaction().setRollbackOnly();
    }

    @Override
    public void ended(Coordination coordination) throws Exception {
        if (em.getTransaction().getRollbackOnly()) {
            try {
                em.getTransaction().rollback();
             } catch (Exception e1) {
                 // Ignore
             }
        } else {
            em.getTransaction().commit();
        }
    }
}