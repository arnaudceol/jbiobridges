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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "interaction_structure")
@XmlAccessorType(XmlAccessType.FIELD)
public class I3DInteractionStructure {

	@XmlElement(name = "prot1")
	private String uniprotAc1;
	
	@XmlElement(name = "prot2")
	private String uniprotAc2;
		
	@XmlElement(name = "rank_major")
	private String rankMajor;

	@XmlElement(name = "rank_minor")
	private String rankMinor;

	@XmlElement(name = "stype")
	private String structureType; // stype

	@XmlElement(name = "pdb")
	private String pdbId;

	@XmlElement(name = "chain1")
	private String chainId1;

	@XmlElement(name = "chain2")
	private String chainId2;
	
	@XmlElement(name = "seq_id1")
	private String sequenceIdentity1;

	@XmlElement(name = "seq_id2")
	private String sequenceIdentity2;

	@XmlElement(name = "cov1")
	private String coverage1;
	
	@XmlElement(name = "cov2")
	private String coverage2;

	@XmlElement(name = "start1")
	private int start1;

	@XmlElement(name = "start2")
	private int start2;
	
	@XmlElement(name = "end1")
	private int end1; // prot_end

	@XmlElement(name = "end2")
	private int end2; // prot_end

	@XmlElement(name = "domain1")
	private String domain1; // prot_end

	@XmlElement(name = "domain2")
	private String domain2; // prot_end

	@XmlElement(name = "filename")
	public String filename;

	public String getUniprotAc1() {
		return uniprotAc1;
	}

	public void setUniprotAc1(String uniprotAc1) {
		this.uniprotAc1 = uniprotAc1;
	}

	public String getUniprotAc2() {
		return uniprotAc2;
	}

	public void setUniprotAc2(String uniprotAc2) {
		this.uniprotAc2 = uniprotAc2;
	}

	public String getRankMajor() {
		return rankMajor;
	}

	public void setRankMajor(String rankMajor) {
		this.rankMajor = rankMajor;
	}

	public String getRankMinor() {
		return rankMinor;
	}

	public void setRankMinor(String rankMinor) {
		this.rankMinor = rankMinor;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getPdbId() {
		return pdbId;
	}

	public void setPdbId(String pdbId) {
		this.pdbId = pdbId;
	}

	public String getChainId1() {
		return chainId1;
	}

	public void setChainId1(String chainId) {
		if (chainId == null) {
			chainId= "";
		}
		this.chainId1 = chainId;
	}

	public String getChainId2() {
		return chainId2;
	}

	public void setChainId2(String chainId) {
		if (chainId == null) {
			chainId= "";
		}
		this.chainId2 = chainId;
	}

	public String getSequenceIdentity1() {
		return sequenceIdentity1;
	}

	public void setSequenceIdentity1(String sequenceIdentity1) {
		this.sequenceIdentity1 = sequenceIdentity1;
	}

	public String getSequenceIdentity2() {
		return sequenceIdentity2;
	}

	public void setSequenceIdentity2(String sequenceIdentity2) {
		this.sequenceIdentity2 = sequenceIdentity2;
	}

	public String getCoverage1() {
		return coverage1;
	}

	public void setCoverage1(String coverage1) {
		this.coverage1 = coverage1;
	}

	public String getCoverage2() {
		return coverage2;
	}

	public void setCoverage2(String coverage2) {
		this.coverage2 = coverage2;
	}

	public int getStart1() {
		return start1;
	}

	public void setStart1(int start1) {
		this.start1 = start1;
	}

	public int getStart2() {
		return start2;
	}

	public void setStart2(int start2) {
		this.start2 = start2;
	}

	public int getEnd1() {
		return end1;
	}

	public void setEnd1(int end1) {
		this.end1 = end1;
	}

	public int getEnd2() {
		return end2;
	}

	public void setEnd2(int end2) {
		this.end2 = end2;
	}

	public String getDomain1() {
		return domain1;
	}

	public void setDomain1(String domain1) {
		this.domain1 = domain1;
	}

	public String getDomain2() {
		return domain2;
	}

	public void setDomain2(String domain2) {
		this.domain2 = domain2;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
