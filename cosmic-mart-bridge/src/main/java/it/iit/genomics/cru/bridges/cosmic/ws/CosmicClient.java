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
package it.iit.genomics.cru.bridges.cosmic.ws;

import it.iit.genomics.cru.bridges.cosmic.model.CosmicException;
import it.iit.genomics.cru.bridges.cosmic.model.CosmicMutation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

//import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URI;
import java.net.URISyntaxException;

public class CosmicClient {

	private final String USER_AGENT = "Mozilla/5.0";

	private String version = "0.6";

	private String cosmicDataset;

	private String biomartUrl;

	/*
	 * <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE Query> <Query
	 * virtualSchemaName = "default" formatter = "TSV" header = "0" uniqueRows = "0"
	 * count = "" datasetConfigVersion = "0.6" >
	 * 
	 * <Dataset name = "COSMIC68" interface = "default" > <Filter name =
	 * "mut_type_aa" value = "Substitution - Missense"/> <Filter name =
	 * "samp_gene_mutated" value = "y"/> <Filter name = "gene_name" value =
	 * "PINK1"/> <Attribute name = "pubmed_pmid" /> <Attribute name = "hist_primary"
	 * /> <Attribute name = "site_primary" /> <Attribute name = "id_mutation" />
	 * <Attribute name = "cds_mut_syntax" /> <Attribute name = "aa_mut_syntax" />
	 * <Attribute name = "zygosity" /> <Attribute name = "gene_name" /> <Attribute
	 * name = "accession_number" /> <Attribute name = "id_sample" /> <Attribute name
	 * = "sample_name" /> <Attribute name = "sample_source" /> <Attribute name =
	 * "tumour_source" /> <Attribute name = "ncbi36" /> <Attribute name = "grch37"
	 * /> <Attribute name = "mut_type_cds" /> <Attribute name = "somatic_status" />
	 * <Attribute name = "systematic_screen" /> <Attribute name =
	 * "validation_status" /> <Attribute name = "in_cancer_census" /> <Attribute
	 * name = "id_gene" /> <Attribute name = "site_subtype_1" /> <Attribute name =
	 * "site_subtype_2" /> <Attribute name = "site_subtype_3" /> <Attribute name =
	 * "hist_subtype_1" /> <Attribute name = "hist_subtype_2" /> <Attribute name =
	 * "hist_subtype_3" /> <Attribute name = "mut_type_aa" /> <Attribute name =
	 * "id_fusion_mutation" /> <Attribute name = "whole_gene_screen" /> <Attribute
	 * name = "entrezgene" /> <Attribute name = "uniprot_swissprot" /> <Attribute
	 * name = "ensembl_gene_id" /> </Dataset> </Query>
	 */

	private String sqlQueryFilterGeneName = "<Filter name = \"gene_name\" value = \"%s\"/>";

	private String sqlQueryFilterEnsemblID = "<Filter name = \"ensembl_gene_id\" value = \"%s\"/>";

	private String sqlQueryFilterEntrezGeneID = "<Filter name = \"entrezgene\" value = \"%s\"/>";

	private String sqlQueryFilterMissense = "<Filter name = \"mut_type_aa\" value = \"Substitution - Missense\"/>";

	private String sqlQueryFilterRegionGrch37 = "<Filter name = \"grch37\" value = \"%s\"/>";

	private String sqlQueryFilterRegionNcbi36 = "<Filter name = \"ncbi36\" value = \"%s\"/>";

	private String xmlQueryAttributes = "<Attribute name = \"pubmed_pmid\" />" + "<Attribute name = \"hist_primary\" />"
			+ "<Attribute name = \"site_primary\" />" + "<Attribute name = \"cds_mut_syntax\" />"
			+ "<Attribute name = \"aa_mut_syntax\" />" + "<Attribute name = \"zygosity\" />"
			+ "<Attribute name = \"gene_name\" />" + "<Attribute name = \"id_sample\" />"
			+ "<Attribute name = \"sample_name\" />" + "<Attribute name = \"sample_source\" />"
			+ "<Attribute name = \"tumour_source\" />" + "<Attribute name = \"ncbi36\" />"
			+ "<Attribute name = \"grch37\" />" + "<Attribute name = \"mut_type_cds\" />"
			+ "<Attribute name = \"mut_type_aa\" />" + "<Attribute name = \"somatic_status\" />"
			+ "<Attribute name = \"validation_status\" />" + "<Attribute name = \"in_cancer_census\" />"
			+ "<Attribute name = \"site_subtype_1\" />" + "<Attribute name = \"site_subtype_2\" />"
			+ "<Attribute name = \"site_subtype_3\" />" + "<Attribute name = \"hist_subtype_1\" />"
			+ "<Attribute name = \"hist_subtype_2\" />" + "<Attribute name = \"hist_subtype_3\" />"
			+ "<Attribute name = \"mut_type_aa\" />"
	// + "<Attribute name = \"entrezgene\" />"
	// + "<Attribute name = \"uniprot_swissprot\" />"
	// + "<Attribute name = \"ensembl_gene_id\" />"
	;

	String xmlQueryByPosition = "query=<Query virtualSchemaName = \"default\" formatter = \"TSV\" header = \"0\" uniqueRows = \"0\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" >%s%s</Dataset></Query>";

	public void init() {
		// get registry

		// get species
	}

	public CosmicClient(String dataset, String martUrl) {

		this.biomartUrl = martUrl;
		this.cosmicDataset = dataset;

		// others : _eg_gene instead of _gene_ensembl (stands for ensembl
		// genomes)
	}

	private CosmicMutation parseLine(String line) {
		String[] columns = line.split("\t");
		System.out.println("####" + line);
		CosmicMutation mutation = new CosmicMutation();

		// + "<Attribute name = \"hist_primary\" />"

		int index = 0;
		mutation.setPubmed(columns[index++]);
		mutation.setPrimaryHistology(columns[index++]);
		// + "<Attribute name = \"site_primary\" />"
		mutation.setPrimarySite(columns[index++]);
		// + "<Attribute name = \"cds_mut_syntax\" />"
		mutation.setMutationCDS(columns[index++]);
		// + "<Attribute name = \"aa_mut_syntax\" />"
		mutation.setMutationAA(columns[index++]);
		// + "<Attribute name = \"zygosity\" />"
		mutation.setZygosity(columns[index++]);
		// + "<Attribute name = \"gene_name\" />"
		mutation.setGeneName(columns[index++]);
		// + "<Attribute name = \"id_sample\" />"
		mutation.setSampleID(columns[index++]);
		// + "<Attribute name = \"sample_name\" />"
		mutation.setSampleName(columns[index++]);
		// + "<Attribute name = \"sample_source\" />"
		mutation.setSampleSource(columns[index++]);
		// + "<Attribute name = \"tumour_source\" />"
		mutation.setTumourSource(columns[index++]);
		// + "<Attribute name = \"ncbi36\" />"
		mutation.setNcbi36(columns[index++]);
		// + "<Attribute name = \"grch37\" />"
		mutation.setGrch37(columns[index++]);
		// + "<Attribute name = \"mut_type_cds\" />"
		mutation.setMutationTypeCDS(columns[index++]);
		// + "<Attribute name = \"somatic_status\" />"
		mutation.setMutationTypeAA(columns[index++]);
		// + "<Attribute name = \"validation_status\" />"
		mutation.setValidationStatus(columns[index++]);
		// + "<Attribute name = \"in_cancer_census\" />"
		mutation.setInCancerCensus("y".equals(columns[index++]));
		// + "<Attribute name = \"site_subtype_1\" />"
		mutation.setSiteSubtype1(columns[index++]);
		// + "<Attribute name = \"site_subtype_2\" />"
		mutation.setSiteSubtype2(columns[index++]);
		// + "<Attribute name = \"site_subtype_3\" />"
		mutation.setSiteSubtype3(columns[index++]);
		// + "<Attribute name = \"hist_subtype_1\" />"
		mutation.setHistologySubtype1(columns[index++]);
		// + "<Attribute name = \"hist_subtype_2\" />"
		mutation.setHistologySubtype2(columns[index++]);
		// + "<Attribute name = \"hist_subtype_3\" />"
		mutation.setHistologySubtype3(columns[index++]);
		// + "<Attribute name = \"mut_type_aa\" />"
		mutation.setMutationTypeAA(columns[index++]);
		// + "<Attribute name = \"entrezgene\" />"
		// mutation.setEntrezGene(columns[index++]);
		//// + "<Attribute name = \"uniprot_swissprot\" />"
		// mutation.setSwissprot(columns[index++]);
		//// + "<Attribute name = \"ensembl_gene_id\" />";
		// mutation.setEnsemblGeneId(columns[index++]);

		return mutation;
	}

	private Collection<CosmicMutation> getMutationsFromXmlQuery(String xmlQuery)
			throws ClientProtocolException, IOException, URISyntaxException {
		HashSet<CosmicMutation> mutations = new HashSet<CosmicMutation>();		

		String query = biomartUrl + "martservice?" + xmlQuery;
		URL url = new URL(query);
		String nullFragment = null;
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(),
				url.getQuery(), nullFragment);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		HttpGet request = new HttpGet(uri);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		String line;

		while ((line = rd.readLine()) != null) {
			mutations.add(parseLine(line));
		}
		rd.close();

		return mutations;

	}
	
	
	public Collection<CosmicMutation> getMutationsByPosition(String chromosomeName,
			int start, int end) throws CosmicException {

		
		try {
			String position = chromosomeName +":" + start + "-" + end;
			
			String filter  = String.format(this.sqlQueryFilterRegionGrch37, position);
			
			String xmlQuery = String.format(xmlQueryByPosition, version,
					cosmicDataset, filter, xmlQueryAttributes);

			return getMutationsFromXmlQuery(xmlQuery);
			
		} catch (Exception e) {
			throw new CosmicException(
					"cannot retrieve ensembl genes at position "
							+ chromosomeName + ": " + start + " - " + end, e);
		}
	}

	
	public Collection<CosmicMutation> getMutationsByGeneName(String geneName) throws CosmicException {

		
		try {
			
			String filter  = String.format(this.sqlQueryFilterGeneName, geneName);
			
			String xmlQuery = String.format(xmlQueryByPosition, version,
					cosmicDataset, filter, xmlQueryAttributes);

			return getMutationsFromXmlQuery(xmlQuery);
			
		} catch (Exception e) {
			throw new CosmicException(
					"cannot retrieve ensembl genes "
							+ geneName, e);
		}
	}
	
}
