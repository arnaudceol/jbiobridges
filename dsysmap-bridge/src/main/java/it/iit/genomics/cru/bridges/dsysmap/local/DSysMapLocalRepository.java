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
package it.iit.genomics.cru.bridges.dsysmap.local;

import it.iit.genomics.cru.bridges.dsysmap.model.DSysMapResult;
import it.iit.genomics.cru.bridges.dsysmap.model.InterfaceResidue;
import it.iit.genomics.cru.bridges.dsysmap.model.StructuredResidue;
import it.iit.genomics.cru.bridges.dsysmap.ws.DSysMapClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.core.KeywordAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Local repository of Interactome3d data. The directory submitted should
 * contain at least a file named interactions.dat and the structures described
 * in it
 *
 * @author Arnaud Ceol
 *
 */
public class DSysMapLocalRepository implements DSysMapClient {

    private static final Logger logger = Logger.getLogger(DSysMapLocalRepository.class.getName());

    private String path;

    Directory interfaceIndex = null; // new RAMDirectory();
    Directory structuresIndex = null; // new RAMDirectory();

//    Analyzer interfaceAnalyzer = null;
//    Analyzer structuresAnalyzer = null;

    public DSysMapLocalRepository(String path) {

        // Read interaction.dat
        BufferedReader br = null;

        try {

            this.path = path;

            if (false == path.endsWith(File.separator)) {
                path = path + File.separator;
            }

            File interfaceFile = new File(path + "interfaces.dat");

            if (interfaceFile.exists() && !interfaceFile.isDirectory()) {

                interfaceIndex = new RAMDirectory();
                logger.info("Init interface index");
//                interfaceAnalyzer = new KeywordAnalyzer();

                IndexWriterConfig config = new IndexWriterConfig(
                         new KeywordAnalyzer());

                try (IndexWriter w = new IndexWriter(interfaceIndex, config)) {
                    String sCurrentLine;

                    br = new BufferedReader(new FileReader(interfaceFile));

                    while ((sCurrentLine = br.readLine()) != null) {
                        String[] columns = sCurrentLine.split("\t");
                        String protein1 = columns[0];
                        String protein2 = columns[8];
                        String resNum = columns[1];
                        String fileName = columns[4];
                        if ("UNIPROT_AC".equals(protein1)) {
                            continue;
                        }
                   
                        addDoc(w, protein1, protein2, resNum, fileName,sCurrentLine);                       
                        
                    }
                }
                br.close();
            }

            File proteinFile = new File(path + "structures.dat");

            if (proteinFile.exists() && !proteinFile.isDirectory()) {

                structuresIndex = new RAMDirectory();

//                structuresAnalyzer = new KeywordAnalyzer();

                IndexWriterConfig config = new IndexWriterConfig(
                         new KeywordAnalyzer());

                try (IndexWriter w = new IndexWriter(structuresIndex, config)) {
                    String sCurrentLine;

                    br = new BufferedReader(new FileReader(proteinFile));

                    while ((sCurrentLine = br.readLine()) != null) {
                        String[] columns = sCurrentLine.split("\t");
                        String protein = columns[0];
                        String fileName = columns[4];
                        String resNum = columns[1];
                        if ("UNIPROT_AC".equals(protein)) {
                            continue;
                        }

                        
                        addDoc(w, protein, resNum, fileName, sCurrentLine);
                    }
                }
                br.close();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }

    public String getPath() {
        return path;
    }

    private void addDoc(IndexWriter w, String protein1, String protein2, String resNum, String fileName,
            String dsysmapLine) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("UNIPROT_AC", protein1, Field.Store.YES));
        doc.add(new TextField("PARTNER", protein2, Field.Store.YES));       
        doc.add(new TextField("RES_NUM", resNum, Field.Store.NO));       
        doc.add(new TextField("PDB_FILENAME", fileName, Field.Store.YES));
        doc.add(new TextField("line", dsysmapLine, Field.Store.YES));
        w.addDocument(doc);
    }

    // protein
    private void addDoc(IndexWriter w, String protein, String resNum, String fileName, String dsysmapLine)
            throws IOException {
        Document doc = new Document();
        doc.add(new TextField("UNIPROT_AC", protein, Field.Store.YES));
        doc.add(new TextField("PDB_FILENAME", fileName, Field.Store.YES));    
        doc.add(new TextField("RES_NUM", resNum, Field.Store.NO));     
        doc.add(new TextField("line", dsysmapLine, Field.Store.YES));
        w.addDocument(doc);
    }

    public Collection<String> getInterfaceResidues(String proteinAc1, String proteinAc2, String structureId) {
        ArrayList<String> results = new ArrayList<>();
        
       // logger.log(Level.INFO, "DSysMap Get interface residues: {0} {1} {2}", new Object[]{proteinAc1, proteinAc2, structureId});
        
        try {
            DirectoryReader reader = DirectoryReader.open(interfaceIndex);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser( "contents",
                    new KeywordAnalyzer());

            Query query = parser.parse("UNIPROT_AC:" + proteinAc1 + " AND PARTNER:"
                    + proteinAc2 + " AND PDB_FILENAME:" + structureId + ".pdb");
           
            logger.info("Query: " +query.toString());
            
            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                int docId = hit.doc;                
                Document d = searcher.doc(docId);
                String[] columns = d.get("line").split("\t");
                String chain = columns[5];
                String pdbResidue = columns[6];
                results.add(pdbResidue + ":" + chain);
                logger.info("result: " +pdbResidue + ":" + chain);
            }
        } catch (ParseException | IOException e) {
            logger.log(Level.SEVERE, "Exception while getting interface", e);
        }
//        logger.log(Level.INFO, "Number of results: {0}", results.size());
        
        return results;
    }

    public Collection<String> getStructuredResidues(String proteinAc, String structureId) {
        ArrayList<String> results = new ArrayList<>();

        try {
            DirectoryReader reader = DirectoryReader.open(interfaceIndex);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser( "contents",
                    new KeywordAnalyzer());

            Query query = parser.parse("UNIPROT_AC:" + proteinAc + " AND PDB_FILENAME:" + structureId + ".pdb");

            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.doc(docId);
                String[] columns = d.get("line").split("\t");
                String chain = columns[5];
                String pdbResidue = columns[6];
                results.add(pdbResidue + ":" + chain);
            }
        } catch (ParseException | IOException e) {
            logger.log(Level.SEVERE, null, e);
        }

        return results;
    }

    @Override
    public DSysMapResult mapMutations(List<String> mutations) {
        // ArrayList<I3DProteinStructure> structures = new ArrayList<I3DProteinStructure>();

        DSysMapResult result = new DSysMapResult();

        try {
            DirectoryReader structureReader = DirectoryReader.open(structuresIndex);
            IndexSearcher structureSearcher = new IndexSearcher(structureReader);
            QueryParser structuredResidueParser = new QueryParser( "contents",
                    new KeywordAnalyzer());

            DirectoryReader interfaceReader = DirectoryReader.open(interfaceIndex);
            IndexSearcher interfaceSearcher = new IndexSearcher(interfaceReader);
            QueryParser interfaceResidueParser = new QueryParser( "contents",
                   new KeywordAnalyzer());
            
            for (String mutation : mutations) {
                String proteinAc = mutation.split(":")[0];

                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(mutation.split(":")[1]);
                while (m.find()) {
                    String position = m.group();
                    Query query = structuredResidueParser.parse("UNIPROT_AC:" + proteinAc + " AND RES_NUM:" + position);

                    TopDocs topDocs = structureSearcher.search(query, structureReader.numDocs());

                    ScoreDoc[] hits = topDocs.scoreDocs;

                    
                    
                    for (ScoreDoc hit : hits) {
                        int docId = hit.doc;
                        Document d = structureSearcher.doc(docId);
                        String[] columns = d.get("line").split("\t");
                        
                        StructuredResidue residue = new StructuredResidue();
                        
                        // UNIPROT_AC	RES_NUM	RES_ORIG	STRUCT_TYPE	
                        // PDB_FILENAME	CHAIN	STRUCT_RES_NUM	STRUCT_RES_NAME	
                        // CLASSIFICATION

                        residue.setUniprotAc(columns[0]);
                        residue.setResidueNumber(Integer.parseInt(columns[1]));
                        residue.setOriginalResidue(columns[2]);
                        residue.setStructureType(columns[3]);
                        residue.setFileName(columns[4]);
                        residue.setChain(columns[5]);
                        residue.setStructureResidueNumber(Integer.parseInt(columns[6]));
                        residue.setStructutreResidueName(columns[7]);
                        residue.setClassification(columns[8]);
                        
                        result.getStructuredResidues().add(residue);
                    }
                    
                    
                    // interfaces
                    query = interfaceResidueParser.parse("UNIPROT_AC:" + proteinAc + " AND RES_NUM:" + position);
System.out.println("UNIPROT_AC:" + proteinAc + " AND RES_NUM=" + position);
                    topDocs = interfaceSearcher.search(query, interfaceReader.numDocs());


                    hits = topDocs.scoreDocs;

                    System.out.println("interface: " + position + " -> " + hits.length );
                    
                    for (ScoreDoc hit : hits) {
                        int docId = hit.doc;
                        Document d = interfaceSearcher.doc(docId);
                        String[] columns = d.get("line").split("\t");
                        
                        InterfaceResidue residue = new InterfaceResidue();
                        
                        // UNIPROT_AC	RES_NUM	RES_ORIG	STRUCT_TYPE	
                        // PDB_FILENAME	CHAIN	STRUCT_RES_NUM	STRUCT_RES_NAME	
                        // PARTNER	PARTNER_CHAIN

                        residue.setUniprotAc(columns[0]);
                        residue.setResidueNumber(Integer.parseInt(columns[1]));
                        residue.setOriginalResidue(columns[2]);
                        residue.setStructureType(columns[3]);
                        residue.setFileName(columns[4]);
                        residue.setChain(columns[5]);
                        residue.setStructureResidueNumber(Integer.parseInt(columns[6]));
                        residue.setStructutreResidueName(columns[7]);
                        residue.setPartner(columns[8]);
                        residue.setPartnerChain(columns[9]);
                        
                        result.getInterfaceResidues().add(residue);
                    }
                }
            }
        } catch (IOException | ParseException ex) {
            logger.log(Level.SEVERE, null, ex);
        } 
        return result ;
    }
}
