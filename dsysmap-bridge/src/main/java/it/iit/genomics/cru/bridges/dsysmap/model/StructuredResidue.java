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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author aceol
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StructuredResidue {
    
    
    @XmlElement(name = "uniprot_ac")
    String uniprotAc;

    @XmlElement(name = "res_num")
    int residueNumber;

    @XmlElement(name = "res_orig")
    String originalResidue;

    @XmlElement(name = "struct_type")
    String structureType;

    @XmlElement(name = "pdb_filename")
    String fileName;

    @XmlElement(name = "chain")
    String chain;

    @XmlElement(name = "struct_res_num")
    int structureResidueNumber;

    @XmlElement(name = "struct_res_name")
    String structutreResidueName;

    @XmlElement(name = "classification")
    String classification;

    public String getUniprotAc() {
        return uniprotAc;
    }

    public void setUniprotAc(String uniprotAc) {
        this.uniprotAc = uniprotAc;
    }

    public int getResidueNumber() {
        return residueNumber;
    }

    public void setResidueNumber(int residueNumber) {
        this.residueNumber = residueNumber;
    }

    public String getOriginalResidue() {
        return originalResidue;
    }

    public void setOriginalResidue(String originalResidue) {
        this.originalResidue = originalResidue;
    }

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public int getStructureResidueNumber() {
        return structureResidueNumber;
    }

    public void setStructureResidueNumber(int structureResidueNumber) {
        this.structureResidueNumber = structureResidueNumber;
    }

    public String getStructutreResidueName() {
        return structutreResidueName;
    }

    public void setStructutreResidueName(String structutreResidueName) {
        this.structutreResidueName = structutreResidueName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
    
    
}
