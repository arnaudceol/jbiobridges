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
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.iit.genomics.cru.bridges.ensembl.model.EnsemblException;

public class EnsemblClientManager {

    private final String USER_AGENT = "Mozilla/5.0";

    public static final String SPECIES_HUMAN = "homo sapiens";

    public final static String[] BIOMART_ENSEMBL = { "ensembl", "ensembl",
            "http://www.ensembl.org/biomart/martservice?" };

    public final static String[] BIOMART_ENSEMBL_FUNGI = { "fungi", "fungal",
            "http://fungi.ensembl.org/biomart/martservice?" };

    public final static String[] BIOMART_ENSEMBL_METAZOA = { "metazoa", "metazoa",
            "http://metazoa.ensembl.org/biomart/martservice?" };

    public final static String[] BIOMART_ENSEMBL_PLANTS = { "plants", "plants",
            "http://plants.ensembl.org/biomart/martservice?" };

    public final static String[] BIOMART_ENSEMBL_PROTISTS = { "protists", "protists",
            "http://protists.ensembl.org/biomart/martservice?" };

    public static final String[][] ENSEMBL_MARTS = { BIOMART_ENSEMBL, BIOMART_ENSEMBL_FUNGI, BIOMART_ENSEMBL_METAZOA,
            BIOMART_ENSEMBL_PLANTS };

    private static final HashMap<String, String> mart2url = new HashMap<>();

    static {
        for (String[] mart : ENSEMBL_MARTS) {
            mart2url.put(mart[0], mart[2]);
        }
    }

    private HashMap<String, String> species2mart = new HashMap<>();

    private static EnsemblClientManager instance;

    private EnsemblClientManager() throws EnsemblException {

        for (String[] mart : ENSEMBL_MARTS) {
            // get dataset from registry
            // String name = mart[0];
            String name = mart[1];
            String server = mart[2];

            try {
                String queryRegistry = server + "type=registry";

                URL url = new URL(queryRegistry);

                String nullFragment = null;

                URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);

                HttpClient client = new DefaultHttpClient();
                client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
                HttpGet request = new HttpGet(uri);

                // add request header
                request.addHeader("User-Agent", USER_AGENT);

                HttpResponse response = client.execute(request);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(response.getEntity().getContent());

                // optional, but recommended
                // read this -
                // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                doc.getDocumentElement().normalize();

                // interaction structure
                NodeList locations = doc.getElementsByTagName("MartURLLocation");

                String martWithVersion = null;

                for (int i = 0; i < locations.getLength(); i++) {

                    Element locationElement = (Element) locations.item(i);

                    String database = locationElement.getAttribute("name");

                    if (database.startsWith(name + "_mart") || "ENSEMBL_MART_ENSEMBL".equals(database)) {
                        martWithVersion = database;
                        break;
                    }
                }

                // Get species
                // get dataset from registry
                String queryDatasets = server + "type=datasets&mart=" + martWithVersion;

                url = new URL(queryDatasets);

                nullFragment = null;

                uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);

                request = new HttpGet(uri);

                // add request header
                request.addHeader("User-Agent", USER_AGENT);

                response = client.execute(request);

                try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    String line;

                    while ((line = rd.readLine()) != null) {
                        if ("".equals(line.trim())) {
                            continue;
                        }

                        String[] columns = line.toLowerCase().split("\t");
                        String species = columns[2].split(" ")[0] + "_" + columns[2].split(" ")[1];

                        species2mart.put(species, name);
                    }
                }

            } catch (IOException | ParserConfigurationException | IllegalStateException | SAXException
                    | URISyntaxException e) {
                throw new EnsemblException("Cannot acces to MART server : "
                        + name + ", " + server, e);
            }
        }

    }

    public static EnsemblClientManager getInstance() throws EnsemblException {
        if (instance == null) {
            instance = new EnsemblClientManager();
        }

        return instance;
    }

    public EnsemblClient getClient(String species) {

        // format species
        String[] parts = species.toLowerCase().split(" ");

        if (parts.length < 2) {
            System.err.println("Species not found: " + species);
            return null;
        }

        // format as it is in ensembl, e.g. homo_spapiens
        String speciesFormatted = parts[0] + "_" + parts[1];

        // format as it is in ensembl datasets, e.g. hspapiens
        String speciesShort = parts[0].charAt(0) + parts[1];

        if (false == species2mart.containsKey(speciesFormatted)) {
            System.err.println("Species not found in MART: " + species);
            return null;
        }

        String mart = species2mart.get(speciesFormatted);

        String dataset;
        if ("ensembl".equals(mart)) {
            dataset = speciesShort + "_gene_ensembl";
        } else {
            dataset = speciesShort + "_eg_gene";
        }

        return new EnsemblClient(dataset, mart2url.get(mart));

    }

}
