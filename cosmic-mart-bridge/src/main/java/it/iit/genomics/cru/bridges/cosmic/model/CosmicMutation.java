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
package it.iit.genomics.cru.bridges.cosmic.model;

public class CosmicMutation {

	private String pubmed;
	private String primaryHistology;
	private String primarySite;
	private String mutationCDS;
	private String mutationAA;
	private String zygosity;
	private String geneName;
	private String sampleID;
	private String sampleName;
	private String sampleSource;
	private String tumourSource;
	private String ncbi36;
	private String grch37;
	private String mutationTypeCDS;
	private String somaticStatus;
	private String validationStatus;
	private boolean inCancerCensus;
	private String siteSubtype1;
	private String siteSubtype2;
	private String siteSubtype3;
	private String histologySubtype1;
	private String histologySubtype2;
	private String histologySubtype3;
	private String mutationTypeAA;
	private String entrezGene;
	private String swissprot;
	private String ensemblGeneId;
	

//	private String aa_mut_start;
//	private String aa_mut_stop;
//	private String cds_mut_start;
//	private String cds_mut_stop;
//	private String samp_gene_mutated;
	
	public CosmicMutation() {
		
	}
	
	public String getPubmed() {
		return pubmed;
	}
	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}
	public String getPrimaryHistology() {
		return primaryHistology;
	}
	public void setPrimaryHistology(String primaryHistology) {
		this.primaryHistology = primaryHistology;
	}
	public String getPrimarySite() {
		return primarySite;
	}
	public void setPrimarySite(String primarySite) {
		this.primarySite = primarySite;
	}
	public String getMutationCDS() {
		return mutationCDS;
	}
	public void setMutationCDS(String mutationCDS) {
		this.mutationCDS = mutationCDS;
	}
	public String getMutationAA() {
		return mutationAA;
	}
	public void setMutationAA(String mutationAA) {
		this.mutationAA = mutationAA;
	}
	public String getZygosity() {
		return zygosity;
	}
	public void setZygosity(String zygosity) {
		this.zygosity = zygosity;
	}
	public String getGeneName() {
		return geneName;
	}
	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}
	public String getSampleID() {
		return sampleID;
	}
	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getSampleSource() {
		return sampleSource;
	}
	public void setSampleSource(String sampleSource) {
		this.sampleSource = sampleSource;
	}
	public String getTumourSource() {
		return tumourSource;
	}
	public void setTumourSource(String tumourSource) {
		this.tumourSource = tumourSource;
	}
	public String getNcbi36() {
		return ncbi36;
	}
	public void setNcbi36(String ncbi36) {
		this.ncbi36 = ncbi36;
	}
	public String getGrch37() {
		return grch37;
	}
	public void setGrch37(String grch37) {
		this.grch37 = grch37;
	}
	public String getMutationTypeCDS() {
		return mutationTypeCDS;
	}
	public void setMutationTypeCDS(String mutationTypeCDS) {
		this.mutationTypeCDS = mutationTypeCDS;
	}
	public String getSomaticStatus() {
		return somaticStatus;
	}
	public void setSomaticStatus(String somaticStatus) {
		this.somaticStatus = somaticStatus;
	}
	public String getValidationStatus() {
		return validationStatus;
	}
	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}
	public boolean getInCancerCensus() {
		return inCancerCensus;
	}
	public void setInCancerCensus(boolean inCancerCensus) {
		this.inCancerCensus = inCancerCensus;
	}
	public String getSiteSubtype1() {
		return siteSubtype1;
	}
	public void setSiteSubtype1(String siteSubtype1) {
		this.siteSubtype1 = siteSubtype1;
	}
	public String getSiteSubtype2() {
		return siteSubtype2;
	}
	public void setSiteSubtype2(String siteSubtype2) {
		this.siteSubtype2 = siteSubtype2;
	}
	public String getSiteSubtype3() {
		return siteSubtype3;
	}
	public void setSiteSubtype3(String siteSubtype3) {
		this.siteSubtype3 = siteSubtype3;
	}
	public String getHistologySubtype1() {
		return histologySubtype1;
	}
	public void setHistologySubtype1(String histologySubtype1) {
		this.histologySubtype1 = histologySubtype1;
	}
	public String getHistologySubtype2() {
		return histologySubtype2;
	}
	public void setHistologySubtype2(String histologySubtype2) {
		this.histologySubtype2 = histologySubtype2;
	}
	public String getHistologySubtype3() {
		return histologySubtype3;
	}
	public void setHistologySubtype3(String histologySubtype3) {
		this.histologySubtype3 = histologySubtype3;
	}
	public String getMutationTypeAA() {
		return mutationTypeAA;
	}
	public void setMutationTypeAA(String mutationTypeAA) {
		this.mutationTypeAA = mutationTypeAA;
	}
	public String getEntrezGene() {
		return entrezGene;
	}
	public void setEntrezGene(String entrezGene) {
		this.entrezGene = entrezGene;
	}
	public String getSwissprot() {
		return swissprot;
	}
	public void setSwissprot(String swissprot) {
		this.swissprot = swissprot;
	}
	public String getEnsemblGeneId() {
		return ensemblGeneId;
	}
	public void setEnsemblGeneId(String ensemblGeneId) {
		this.ensemblGeneId = ensemblGeneId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ensemblGeneId == null) ? 0 : ensemblGeneId.hashCode());
		result = prime * result
				+ ((entrezGene == null) ? 0 : entrezGene.hashCode());
		result = prime * result
				+ ((geneName == null) ? 0 : geneName.hashCode());
		result = prime * result + ((grch37 == null) ? 0 : grch37.hashCode());
		result = prime
				* result
				+ ((histologySubtype1 == null) ? 0 : histologySubtype1
						.hashCode());
		result = prime
				* result
				+ ((histologySubtype2 == null) ? 0 : histologySubtype2
						.hashCode());
		result = prime
				* result
				+ ((histologySubtype3 == null) ? 0 : histologySubtype3
						.hashCode());
		result = prime * result + (inCancerCensus ? 1231 : 1237);
		result = prime * result
				+ ((mutationAA == null) ? 0 : mutationAA.hashCode());
		result = prime * result
				+ ((mutationCDS == null) ? 0 : mutationCDS.hashCode());
		result = prime * result
				+ ((mutationTypeAA == null) ? 0 : mutationTypeAA.hashCode());
		result = prime * result
				+ ((mutationTypeCDS == null) ? 0 : mutationTypeCDS.hashCode());
		result = prime * result + ((ncbi36 == null) ? 0 : ncbi36.hashCode());
		result = prime
				* result
				+ ((primaryHistology == null) ? 0 : primaryHistology.hashCode());
		result = prime * result
				+ ((primarySite == null) ? 0 : primarySite.hashCode());
		result = prime * result + ((pubmed == null) ? 0 : pubmed.hashCode());
		result = prime * result
				+ ((sampleID == null) ? 0 : sampleID.hashCode());
		result = prime * result
				+ ((sampleName == null) ? 0 : sampleName.hashCode());
		result = prime * result
				+ ((sampleSource == null) ? 0 : sampleSource.hashCode());
		result = prime * result
				+ ((siteSubtype1 == null) ? 0 : siteSubtype1.hashCode());
		result = prime * result
				+ ((siteSubtype2 == null) ? 0 : siteSubtype2.hashCode());
		result = prime * result
				+ ((siteSubtype3 == null) ? 0 : siteSubtype3.hashCode());
		result = prime * result
				+ ((somaticStatus == null) ? 0 : somaticStatus.hashCode());
		result = prime * result
				+ ((swissprot == null) ? 0 : swissprot.hashCode());
		result = prime * result
				+ ((tumourSource == null) ? 0 : tumourSource.hashCode());
		result = prime
				* result
				+ ((validationStatus == null) ? 0 : validationStatus.hashCode());
		result = prime * result
				+ ((zygosity == null) ? 0 : zygosity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CosmicMutation other = (CosmicMutation) obj;
		if (ensemblGeneId == null) {
			if (other.ensemblGeneId != null)
				return false;
		} else if (!ensemblGeneId.equals(other.ensemblGeneId))
			return false;
		if (entrezGene == null) {
			if (other.entrezGene != null)
				return false;
		} else if (!entrezGene.equals(other.entrezGene))
			return false;
		if (geneName == null) {
			if (other.geneName != null)
				return false;
		} else if (!geneName.equals(other.geneName))
			return false;
		if (grch37 == null) {
			if (other.grch37 != null)
				return false;
		} else if (!grch37.equals(other.grch37))
			return false;
		if (histologySubtype1 == null) {
			if (other.histologySubtype1 != null)
				return false;
		} else if (!histologySubtype1.equals(other.histologySubtype1))
			return false;
		if (histologySubtype2 == null) {
			if (other.histologySubtype2 != null)
				return false;
		} else if (!histologySubtype2.equals(other.histologySubtype2))
			return false;
		if (histologySubtype3 == null) {
			if (other.histologySubtype3 != null)
				return false;
		} else if (!histologySubtype3.equals(other.histologySubtype3))
			return false;
		if (inCancerCensus != other.inCancerCensus)
			return false;
		if (mutationAA == null) {
			if (other.mutationAA != null)
				return false;
		} else if (!mutationAA.equals(other.mutationAA))
			return false;
		if (mutationCDS == null) {
			if (other.mutationCDS != null)
				return false;
		} else if (!mutationCDS.equals(other.mutationCDS))
			return false;
		if (mutationTypeAA == null) {
			if (other.mutationTypeAA != null)
				return false;
		} else if (!mutationTypeAA.equals(other.mutationTypeAA))
			return false;
		if (mutationTypeCDS == null) {
			if (other.mutationTypeCDS != null)
				return false;
		} else if (!mutationTypeCDS.equals(other.mutationTypeCDS))
			return false;
		if (ncbi36 == null) {
			if (other.ncbi36 != null)
				return false;
		} else if (!ncbi36.equals(other.ncbi36))
			return false;
		if (primaryHistology == null) {
			if (other.primaryHistology != null)
				return false;
		} else if (!primaryHistology.equals(other.primaryHistology))
			return false;
		if (primarySite == null) {
			if (other.primarySite != null)
				return false;
		} else if (!primarySite.equals(other.primarySite))
			return false;
		if (pubmed == null) {
			if (other.pubmed != null)
				return false;
		} else if (!pubmed.equals(other.pubmed))
			return false;
		if (sampleID == null) {
			if (other.sampleID != null)
				return false;
		} else if (!sampleID.equals(other.sampleID))
			return false;
		if (sampleName == null) {
			if (other.sampleName != null)
				return false;
		} else if (!sampleName.equals(other.sampleName))
			return false;
		if (sampleSource == null) {
			if (other.sampleSource != null)
				return false;
		} else if (!sampleSource.equals(other.sampleSource))
			return false;
		if (siteSubtype1 == null) {
			if (other.siteSubtype1 != null)
				return false;
		} else if (!siteSubtype1.equals(other.siteSubtype1))
			return false;
		if (siteSubtype2 == null) {
			if (other.siteSubtype2 != null)
				return false;
		} else if (!siteSubtype2.equals(other.siteSubtype2))
			return false;
		if (siteSubtype3 == null) {
			if (other.siteSubtype3 != null)
				return false;
		} else if (!siteSubtype3.equals(other.siteSubtype3))
			return false;
		if (somaticStatus == null) {
			if (other.somaticStatus != null)
				return false;
		} else if (!somaticStatus.equals(other.somaticStatus))
			return false;
		if (swissprot == null) {
			if (other.swissprot != null)
				return false;
		} else if (!swissprot.equals(other.swissprot))
			return false;
		if (tumourSource == null) {
			if (other.tumourSource != null)
				return false;
		} else if (!tumourSource.equals(other.tumourSource))
			return false;
		if (validationStatus == null) {
			if (other.validationStatus != null)
				return false;
		} else if (!validationStatus.equals(other.validationStatus))
			return false;
		if (zygosity == null) {
			if (other.zygosity != null)
				return false;
		} else if (!zygosity.equals(other.zygosity))
			return false;
		return true;
	}

	
}