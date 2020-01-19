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

@XmlRootElement(name = "protein_structure")
@XmlAccessorType(XmlAccessType.FIELD)
public class I3DProteinStructure {

	@XmlElement(name = "uniprot_ac")
	private String uniprotAc;

	@XmlElement(name = "rank_major")
	private String rankMajor;

	@XmlElement(name = "rank_minor")
	private String rankMinor;

	@XmlElement(name = "stype")
	private String structureType; // stype

	@XmlElement(name = "pdb_id")
	private String pdbId;

	@XmlElement(name = "chain_id")
	private String chainId;

	@XmlElement(name = "seq_id")
	private String sequenceIdentity;

	@XmlElement(name = "coverage")
	private String coverage;

	@XmlElement(name = "prot_start")
	private int start;

	@XmlElement(name = "prot_end")
	private int end; // prot_end

	@XmlElement(name = "ga341")
	private String ga341;

	@XmlElement(name = "mpqs")
	private String mpqs;

	@XmlElement(name = "zdope")
	private String zdope;

	@XmlElement(name = "filename")
	private String filename;

	public String getUniprotAc() {
		return uniprotAc;
	}

	public void setUniprotAc(String uniprotAc) {
		this.uniprotAc = uniprotAc;
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

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		if (chainId == null || "".equals(chainId.trim())) {
			// Model, always called A
			chainId = "A";
		}
		this.chainId = chainId;
	}

	public String getSequenceIdentity() {
		return sequenceIdentity;
	}

	public void setSequenceIdentity(String sequenceIdentity) {
		this.sequenceIdentity = sequenceIdentity;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getGa341() {
		return ga341;
	}

	public void setGa341(String ga341) {
		this.ga341 = ga341;
	}

	public String getMpqs() {
		return mpqs;
	}

	public void setMpqs(String mpqs) {
		this.mpqs = mpqs;
	}

	public String getZdope() {
		return zdope;
	}

	public void setZdope(String zdope) {
		this.zdope = zdope;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
