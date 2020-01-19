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
package it.iit.genomics.cru.bridges.interactome3d.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "interactome3D")
@XmlAccessorType(XmlAccessType.FIELD)
public class Interactome3D {

	@XmlElementWrapper(name = "interaction_structure_list")
	@XmlElement(name = "interaction_structure")
	private final List<I3DInteractionStructure> interactionStructureList = new ArrayList<>();

	public List<I3DInteractionStructure> getInteractionStructureList() {
		return interactionStructureList;
	}

	@XmlElementWrapper(name = "protein_structure_list")
	@XmlElement(name = "protein_structure")
	private final List<I3DProteinStructure> proteinStructureList = new ArrayList<>();;

	public List<I3DProteinStructure> getProteinStructureList() {
		return proteinStructureList;
	}

	@XmlElementWrapper(name = "pdb_file_list")
	@XmlElement(name = "pdb_file")
	private final List<I3DPdbFile> pdbFileList = new ArrayList<>();;

	public List<I3DPdbFile> getPdbFileList() {
		return pdbFileList;
	}

	@Override
	public String toString() {
		return "Structure: " + this.getInteractionStructureList().size();

	}

	public void addProteinStructure(
			I3DProteinStructure proteinStructure) {
		this.proteinStructureList.add(proteinStructure) ;
	}

	public void addInteractionStructure(
			I3DInteractionStructure interactionStructure) {
		this.interactionStructureList.add(interactionStructure);
	}

	public void addPdbFile(I3DPdbFile pdbFile) {
		this.pdbFileList.add(pdbFile);
	}

}
