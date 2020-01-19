/*******************************************************************************
 * Copyright 2015 Fondazione Istituto Italiano di Tecnologia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.bridges.dsysmap.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aceol
 */
@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
public class DSysMapResult {

    @XmlElementWrapper(name = "structured_residues")
    @XmlElement(name = "structured_residue")
    private final List<StructuredResidue> structuredResidues = new ArrayList<>();

    @XmlElementWrapper(name = "interface_residues")
    @XmlElement(name = "interface_residue")
    private final List<InterfaceResidue> interfaceResidues = new ArrayList<>();

    public List<StructuredResidue> getStructuredResidues() {
        return structuredResidues;
    }

    public List<InterfaceResidue> getInterfaceResidues() {
        return interfaceResidues;
    }

}
