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
package it.iit.genomics.cru.bridges.ensembl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import it.iit.genomics.cru.bridges.ensembl.model.EnsemblException;
import it.iit.genomics.cru.bridges.ensembl.model.Exon;
import it.iit.genomics.cru.bridges.ensembl.model.Gene;

public class EnsemblClient {

    private final String USER_AGENT = "Mozilla/5.0";

    private final String version = "0.6";

    private final String ensemblDataset;

    private final String biomartUrl;

    String xmlQueryByPosition = "query=<Query virtualSchemaName = \"default\" formatter = \"TSV\" header = \"0\" uniqueRows = \"1\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" ><Filter name=\"biotype\" value=\"protein_coding\"/><Filter name = \"chromosome_name\" value = \"%s\"/><Filter name = \"start\" value = \"%s\"/><Filter name = \"end\" value = \"%s\"/><Attribute name = \"ensembl_gene_id\" /><Attribute name = \"chromosome_name\" /><Attribute name = \"start_position\" /><Attribute name = \"end_position\" /><Attribute name = \"strand\" /><Attribute name = \"uniprot_swissprot_accession\" /><Attribute name = \"external_gene_id\" /></Dataset></Query>";

    String xmlQueryByEnsemblGeneID = "query=<Query virtualSchemaName = \"default\" formatter = \"TSV\" header = \"0\" uniqueRows = \"1\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" ><Filter name=\"biotype\" value=\"protein_coding\"/><Filter name = \"ensembl_gene_id\" value = \"%s\"/><Attribute name = \"ensembl_gene_id\" /><Attribute name = \"chromosome_name\" /><Attribute name = \"start_position\" /><Attribute name = \"end_position\" /><Attribute name = \"strand\" /><Attribute name = \"uniprot_swissprot_accession\" /><Attribute name = \"external_gene_id\" /></Dataset></Query>";

    String xmlQueryByHgncSymbol = "query=<Query virtualSchemaName = \"default\" formatter = \"TSV\" header = \"0\" uniqueRows = \"1\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" ><Filter name=\"biotype\" value=\"protein_coding\"/><Filter name = \"hgnc_symbol\" value = \"%s\"/><Attribute name = \"ensembl_gene_id\" /><Attribute name = \"chromosome_name\" /><Attribute name = \"start_position\" /><Attribute name = \"end_position\" /><Attribute name = \"strand\" /><Attribute name = \"uniprot_swissprot_accession\" /><Attribute name = \"external_gene_id\" /></Dataset></Query>";

    String xmlQuerySequenceByEnsemblGeneID = "query=<Query virtualSchemaName = \"default\" formatter = \"FASTA\" header = \"0\" uniqueRows = \"1\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" ><Filter name=\"biotype\" value=\"protein_coding\"/><Filter name = \"ensembl_gene_id\" value = \"%s\"/><Attribute name = \"ensembl_gene_id\" /><Attribute name = \"gene_exon_intron\" /></Dataset></Query>";

    String xmlExonsByEnsemblGeneID = "query=<Query virtualSchemaName = \"default\" formatter = \"TSV\" header = \"0\" uniqueRows = \"1\" count = \"\" datasetConfigVersion = \"%s\" ><Dataset name = \"%s\" interface = \"default\" ><Filter name=\"biotype\" value=\"protein_coding\"/><Filter name = \"ensembl_gene_id\" value = \"%s\"/><Attribute name = \"exon_chrom_start\" /> <Attribute name = \"exon_chrom_end\" /></Dataset></Query>";

    public void init() {
        // get registry

        // get species
    }

    public EnsemblClient(String dataset, String martUrl) {

        this.biomartUrl = martUrl;
        this.ensemblDataset = dataset;

        // others : _eg_gene instead of _gene_ensembl (stands for ensembl
        // genomes)
    }

    public Collection<Gene> getGenesByPosition(String chromosomeName, int start, int end) throws EnsemblException {

        try {
            ArrayList<Gene> genes = new ArrayList<>();

            String xmlQuery = String.format(xmlQueryByPosition, version, ensemblDataset, chromosomeName, start, end);

            String query = biomartUrl + xmlQuery;
            URL url = new URL(query);
            String nullFragment = null;
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);

            HttpClient client = new DefaultHttpClient();
            client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
            HttpGet request = new HttpGet(uri);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(request);

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line;

                while ((line = rd.readLine()) != null) {
                    String[] columns = line.split("\t");
                    String ensemblGeneID = columns[0];
                    String geneChromosomeName = columns[1];
                    int geneStart = Integer.parseInt(columns[2]);
                    int geneEnd = Integer.parseInt(columns[3]);
                    String strand = columns[4];
                    String uniprotAc = columns[5];
                    String geneName = columns[6];
                    if (false == "".equals(uniprotAc)) {
                        HashSet<String> acs = new HashSet<>();
                        acs.add(uniprotAc);

                        Gene gene = new Gene(ensemblGeneID, geneName, geneChromosomeName, geneStart, geneEnd, strand,
                                acs);
                        genes.add(gene);
                    }

                }
            }

            return genes;

        } catch (IOException | IllegalStateException | NumberFormatException | URISyntaxException e) {
            throw new EnsemblException(
                    "cannot retrieve ensembl genes at position "
                    + chromosomeName + ": " + start + " - " + end, e);
        }

    }

    public Collection<Gene> getGenesHgncSymbol(String hgncSymbol)
            throws EnsemblException {

        try {

            ArrayList<Gene> genes = new ArrayList<>();

            String xmlQuery = String.format(xmlQueryByHgncSymbol, version,
                    ensemblDataset, hgncSymbol);

            String query = biomartUrl + xmlQuery;

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

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()))) {
                String line;
                
                while ((line = rd.readLine()) != null) {
                    String[] columns = line.split("\t");
                    String ensemblGeneID = columns[0];
                    String geneChromosomeName = columns[1];
                    int geneStart = Integer.parseInt(columns[2]);
                    int geneEnd = Integer.parseInt(columns[3]);
                    String strand = columns[4];
                    String uniprotAc = columns[5];
                    String geneName = columns[6];
                    if (false == "".equals(uniprotAc)) {
                        HashSet<String> acs = new HashSet<>();
                        acs.add(uniprotAc);
                        
                        Gene gene = new Gene(ensemblGeneID, geneName,
                                geneChromosomeName, geneStart, geneEnd, strand, acs);
                        genes.add(gene);
                    }
                    
                }
            }

            return genes;

        } catch (IOException | IllegalStateException | NumberFormatException | URISyntaxException e) {
            throw new EnsemblException("cannot retrieve ensembl genes for symbol "
                    + hgncSymbol, e);
        }

    }

    public Collection<Gene> getGenesEnsemblGeneId(String ensemblID)
            throws EnsemblException {

        try {

            ArrayList<Gene> genes = new ArrayList<>();

            String xmlQuery = String.format(xmlQueryByEnsemblGeneID, version,
                    ensemblDataset, ensemblID);

            String query = biomartUrl + xmlQuery;

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

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()))) {
                String line;
                
                while ((line = rd.readLine()) != null) {
                    String[] columns = line.split("\t");
                    String ensemblGeneID = columns[0];
                    String geneChromosomeName = columns[1];
                    int geneStart = Integer.parseInt(columns[2]);
                    int geneEnd = Integer.parseInt(columns[3]);
                    String strand = columns[4];
                    String uniprotAc = columns[5];
                    String geneName = columns[6];
                    if (false == "".equals(uniprotAc)) {
                        HashSet<String> acs = new HashSet<>();
                        acs.add(uniprotAc);
                        
                        Gene gene = new Gene(ensemblGeneID, geneName,
                                geneChromosomeName, geneStart, geneEnd, strand, acs);
                        genes.add(gene);
                    }
                    
                }
            }

            return genes;

        } catch (IOException | IllegalStateException | NumberFormatException | URISyntaxException e) {
            throw new EnsemblException("cannot retrieve ensembl genes for ID "
                    + ensemblID, e);
        }

    }

    public Collection<Exon> getExons(String ensemblGeneId)
            throws EnsemblException {
        try {

            ArrayList<Exon> exons = new ArrayList<>();

            String xmlQuery = String.format(xmlExonsByEnsemblGeneID, version,
                    ensemblDataset, ensemblGeneId);

            String query = biomartUrl + xmlQuery;
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

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()))) {
                String line;
                
                while ((line = rd.readLine()) != null) {
                    String[] columns = line.split("\t");
                    
                    int exonStart = Integer.parseInt(columns[0]);
                    int exonEnd = Integer.parseInt(columns[1]);
                    exons.add(new Exon(exonStart, exonEnd));
                    
                }
            }

            return exons;

        } catch (IOException | IllegalStateException | NumberFormatException | URISyntaxException e) {
            throw new EnsemblException(
                    "cannot retrieve exon positions for ensembl gene "
                    + ensemblGeneId, e);
        }
    }

    public String getSequence(String ensemblGeneId) throws EnsemblException {
        try {

            String xmlQuery = String.format(xmlQuerySequenceByEnsemblGeneID,
                    version, ensemblDataset, ensemblGeneId);

            String query = biomartUrl + xmlQuery;
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

            String fasta;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()))) {
                String line;
                fasta = "";
                while ((line = rd.readLine()) != null) {
                    if (line.startsWith(">")) {
                        continue;
                    }
                    fasta += line.trim();
                }
            }

            return fasta;

        } catch (IOException | IllegalStateException | URISyntaxException e) {
            throw new EnsemblException(
                    "cannot retrieve exon positions for ensembl gene "
                    + ensemblGeneId, e);
        }
    }

}
