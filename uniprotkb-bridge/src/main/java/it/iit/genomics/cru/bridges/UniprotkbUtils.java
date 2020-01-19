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
 * See the License for the specific lanArrayListMultimapge governing permissions and
 * limitations under the License.
 *******************************************************************************/
package it.iit.genomics.cru.bridges;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ArrayListMultimap;

import org.apache.commons.lang.StringUtils;

/**
 * @author Arnaud Ceol
 *
 * Utilities to retrieve Uniprot AC and gene names from the Uniprotkb database
 */
public class UniprotkbUtils {

    private static final String UNIPROT_SERVER = "https://www.uniprot.org/";

    private static final String DBFETCH_SERVER = "https://www.ebi.ac.uk/Tools/dbfetch/dbfetch";

    private static final Logger LOG = Logger.getAnonymousLogger();

    private static final String UNIPROT_TOOL = "uniprot";

    private static final String TAXONOMY_TOOL = "taxonomy";

    public static ArrayListMultimap<String, String> getUniprotAcsFromGenes(
            Collection<String> genes, String organism) {
        String tool = UNIPROT_TOOL;

        ArrayListMultimap<String, String> gene2uniprots =  ArrayListMultimap.create();

        if (genes.isEmpty()) {
            return gene2uniprots;
        }

        try {
            String location = UNIPROT_SERVER
                    + tool
                    + "/?"
                    + "format=tab&columns=id,genes&query=organism:"
                    + URLEncoder.encode("\"" + organism + "\"", "UTF-8")
                    + "+gene:"
                    + URLEncoder.encode(
                            "\"" + StringUtils.join(genes, "\" OR gene:\"")
                            + "\"", "UTF-8");
            URL url = new URL(location);
            LOG.info("Submitting...");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }

            String[] lines;

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a = 0;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }

                lines = builder.toString().split("\n");

                for (String line : lines) {
                    if (line.split("\t").length < 2) {
                        LOG.log(Level.WARNING, "Strange line in uniprot tab: "
                                + line);
                        continue;
                    }
                    String uniprotAc = line.split("\t")[0];
                    String[] geneNames = line.split("\t")[1].split(" ");

                    for (String gene : geneNames) {
                        if (gene2uniprots.containsKey(gene)) {
                            gene2uniprots.put(gene, uniprotAc);
                        }
                    }
                }

            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}: {2}", new Object[]{conn.getResponseMessage(), StringUtils.join(genes, ","), location});
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for {1}", new Object[]{e.getMessage(), StringUtils.join(genes, ",")});
        }

        return gene2uniprots;
    }

    public static String[] getUniprotAcFromGene(String gene, String organism) {
        String tool = UNIPROT_TOOL;
        String[] acs;

        try {
            String location = UNIPROT_SERVER + tool + "/?"
                    + "format=list&query=organism:"
                    + URLEncoder.encode("\"" + organism + "\"", "UTF-8")
                    + "+gene:"
                    + URLEncoder.encode("\"" + gene + "\"", "UTF-8");
System.out.println(location);
            URL url = new URL(location);
            // LOG.info("Submitting...");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                acs = builder.toString().split("\n");
            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}: {2}", new Object[]{conn.getResponseMessage(), gene, location});
                acs = null;
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for ", e.getMessage());
            acs = null;
        }

        return acs;
    }

    public static ArrayListMultimap<String, String> getUniprotAcsFromRefSeqs(
            Collection<String> refSeqs) {
        String tool = UNIPROT_TOOL;

        ArrayListMultimap<String, String> refseq2uniprots = ArrayListMultimap.create();

        if (refSeqs.isEmpty()) {
            return refseq2uniprots;
        }

        try {
            String location = UNIPROT_SERVER
                    + tool
                    + "/?"
                    + "query=(database%3A(type%3Arefseq+"
                    + URLEncoder
                    .encode(""
                            + StringUtils.join(refSeqs,
                                    ") OR database:(type:refseq ") + "",
                            "UTF-8") + "))&format=txt";
            URL url = new URL(location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                LOG.log(Level.INFO, "Waiting ({0})...", wait);
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }

            String[] lines;

            if (status == HttpURLConnection.HTTP_OK) {

                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }

                lines = builder.toString().split("\n");

                String ac = "";

                for (String line : lines) {
                    if (line.startsWith("AC ")) {
                        ac = line.substring(5, line.indexOf(";"));
                    } else if (line.startsWith("DR   RefSeq;")) {
                        String[] xrefs = line.split(";");
                        for (String xref : xrefs) {
                            xref = xref.trim();
                            if (xref.endsWith(".")) {
                                xref = xref.substring(0, xref.length() - 1);
                            }
                            if (refseq2uniprots.containsKey(xref.trim())) {
                                refseq2uniprots.put(xref, ac);
                            } else if (refseq2uniprots.containsKey(xref
                                    .split("[.]")[0])) {
                                refseq2uniprots.put(xref.split("[.]")[0], ac);
                            }
                        }
                    }
                }

            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}: {2}", new Object[]{conn.getResponseMessage(), StringUtils.join(refSeqs, ","), location});
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for ", e.getMessage());
        }

        return refseq2uniprots;
    }

    public static String[] getUniprotAcFromRefSeq(String refSeq) {
        String tool = UNIPROT_TOOL;
        String[] acs;

        try {
            String location = UNIPROT_SERVER + tool + "/?"
                    + "query=database%3A(type%3Arefseq+"
                    + URLEncoder.encode("\"" + refSeq + "\"", "UTF-8")
                    + ")&format=list";
            URL url = new URL(location);
            LOG.info("Submitting...");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                LOG.log(Level.INFO, "Waiting ({0})...", wait);
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }
            if (status == HttpURLConnection.HTTP_OK) {

                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                acs = builder.toString().split("\n");
            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}, {2}", new Object[]{conn.getResponseMessage(), refSeq, location});
                acs = null;
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for ", e.getMessage());
            acs = null;
        }

        return acs;
    }

    public static HashMap<String, String> getGenesFromUniprotAcs(
            Collection<String> xrefs) {

        // remove xrefs that are not uniprotAcs
        Collection<String> uniprotAcs = getUniprotAcs(xrefs);

        HashMap<String, String> results = new HashMap<>();
        try {

            String location = DBFETCH_SERVER + "?db=uniprotkb;id="
                    + URLEncoder.encode(StringUtils.join(uniprotAcs, ","), "UTF-8")
                    + ";format=default;style=raw";

            URL url = new URL(location);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }

            String[] lines;

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                lines = builder.toString().split("\n");

                String ac = null;

                boolean skipNextAcLine = false;

                for (String line : lines) {
                    if (line.startsWith("AC ")) {
                        if (false == skipNextAcLine) {
                            ac = line.substring(5, line.indexOf(";"));
                            skipNextAcLine = true;
                        }
                    } else if (line.startsWith("GN ")) {
                        String gene = line.substring(line.indexOf("=") + 1,
                                line.indexOf(";"));
                        results.put(ac, gene);
                        skipNextAcLine = false;
                    } else {
                        skipNextAcLine = false;
                    }
                }
            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}: {2}", new Object[]{conn.getResponseMessage(), StringUtils.join(uniprotAcs, ","), location});
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for ", e.getMessage());
            e.printStackTrace(System.err);
        }

        return results;
    }

    public static ArrayListMultimap<String, String> getStructuresFromUniprotAcs(
            Collection<String> xrefs) {

        // remove xrefs that are not uniprotAcs
        Collection<String> uniprotAcs = getUniprotAcs(xrefs);

        ArrayListMultimap<String, String> results = ArrayListMultimap.create();

        String location = DBFETCH_SERVER + "?db=uniprotkb;id="
                + StringUtils.join(uniprotAcs, ",")
                + ";format=default;style=raw";

        try {

            URL url = new URL(location);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }
                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }

            String[] lines;

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a = 0;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                lines = builder.toString().split("\n");

                String ac = null;
                for (String line : lines) {
                    if (line.startsWith("AC ")) {
                        ac = line.substring(5, line.indexOf(";"));
                    } else if (line.startsWith("DR   PDB;")) {
                        String pdb = line.split(";")[1].trim();
                        results.put(ac, pdb);
                    }
                }
            } else {
                LOG.log(Level.SEVERE, "Failed, got response {0} for {1}: {2}", new Object[]{conn.getResponseMessage(), StringUtils.join(uniprotAcs, ","), location});
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for {1}", new Object[]{e.getMessage(), location});
            e.printStackTrace(System.err);
        }

        return results;
    }

    public static String getGeneFromUniprotAc(String uniprotAc) {
        String tool = UNIPROT_TOOL;
        String[] acs;

        if (false == isUniprotAc(uniprotAc)) {
            return null;
        }

        try {
            String location = UNIPROT_SERVER + tool + "/?"
                    + "query=accession%3a"
                    + URLEncoder.encode("\"" + uniprotAc + "\"", "UTF-8")
                    + "&format=tab&columns=id,genes";

            URL url = new URL(location);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }

                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                acs = builder.toString().split("\n");
            } else {
                LOG.log(Level.SEVERE, "Failed, got response message{0} for {1}: {2}", new Object[]{conn.getResponseMessage(), uniprotAc, location});
                acs = null;
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            e.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for {1}", new Object[]{e.getMessage(), uniprotAc});
            e.printStackTrace(System.err);
            acs = null;
        }

        if (acs != null && acs.length > 1) {
            return acs[1].split("\t")[1].split(" ")[0];
        }

        return null;
    }

    public static ArrayList<String[]> getSpeciesFromName(String name) {
        String tool = TAXONOMY_TOOL;

        ArrayList<String[]> results = new ArrayList<>();

        try {
            String location = UNIPROT_SERVER + tool + "/?"
                    + "query=complete:yes+AND+("
                    + URLEncoder.encode(name, "UTF-8") + ")&format=tab";
            URL url = new URL(location);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null) {
                    wait = Integer.valueOf(header);
                }
                if (wait == 0) {
                    break;
                }

                conn.disconnect();
                Thread.sleep(wait * 1000);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }

            String[] lines;

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();

                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }

                lines = builder.toString().split("\n");

                for (String line : lines) {
                    if (false == line.startsWith("Taxon")) {
                        String[] fields = line.split("\t");
                        if (fields.length >= 3) {
                            String[] species = {fields[2], fields[0]};
                            results.add(species);
                        }
                    }
                }

            } else {
                LOG.log(Level.SEVERE, "Failed, got response message{0} for {1}: {2}", new Object[]{conn.getResponseMessage(), name, location});
            }
            conn.disconnect();
        } catch (IOException | NumberFormatException | InterruptedException e) {
            LOG.log(Level.SEVERE, "Failed, got Exception {0} for {1}", new Object[]{e.getMessage(), name});
            e.printStackTrace(System.err);
        }

        return results;
    }

    /**
     * Check syntax of a cross reference and verify it is a Uniprot Ac (without
     * isoform)
     *
     * @param xref
     * @return
     */
    public static boolean isUniprotAc(String xref) {
        return xref.matches("[A-Za-z0-9]{6}(\\-[0-9]+)?");
    }

    private static Collection<String> getUniprotAcs(Collection<String> xrefs) {
        HashSet<String> uniprotAcs = new HashSet<>();

        for (String xref : xrefs) {
            if (xref.matches(".*\\-[0-9]+")) {
                xref = xref.split("-")[0];
            }

            if (isUniprotAc(xref)) {
                uniprotAcs.add(xref);
            }
        }
        return uniprotAcs;
    }

}
