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
package it.iit.genomics.cru.bridges.interactome3d.local;

import it.iit.genomics.cru.bridges.interactome3d.ws.Interactome3DClient;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DInteractionStructure;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DProteinStructure;
import it.iit.genomics.cru.bridges.interactome3d.ws.Interactome3DException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Interactome3DLocalRepository implements Interactome3DClient {

    private static final Logger logger = Logger.getLogger(Interactome3DLocalRepository.class.getName());

    private String path;

    Directory interactionIndex = null; // new RAMDirectory();
    Directory proteinIndex = null; // new RAMDirectory();

    KeywordAnalyzer interactionAnalyzer = null;
    KeywordAnalyzer proteinAnalyzer = null;

    public Interactome3DLocalRepository(String path) {

        // Read interaction.dat
        BufferedReader br = null;

        try {
            logger.log(Level.INFO, "Open Interactome3D client from path: {0}", path);
            this.path = path;

            if (false == path.endsWith(File.separator)) {
                path = path + File.separator;
            }

            File interactionFile = new File(path + "interactions.dat");

            if (interactionFile.exists() && !interactionFile.isDirectory()) {

                interactionIndex = new RAMDirectory();

                interactionAnalyzer = new KeywordAnalyzer();

                IndexWriterConfig config = new IndexWriterConfig(
                        interactionAnalyzer);

                try (IndexWriter w = new IndexWriter(interactionIndex, config)) {
                    String sCurrentLine;
                    
                    br = new BufferedReader(new FileReader(interactionFile));
                    
                    while ((sCurrentLine = br.readLine()) != null) {
                        String[] columns = sCurrentLine.split("\t");
                        String protein1 = columns[0];
                        String protein2 = columns[1];
                        if ("PROT1".equals(protein1)) {
                            continue;
                        }
                        
                        addDoc(w, protein1, protein2, sCurrentLine);
                    }
                }
                br.close();
            }

            File proteinFile = new File(path + "proteins.dat");

            if (proteinFile.exists() && !proteinFile.isDirectory()) {

                proteinIndex = new RAMDirectory();

                proteinAnalyzer = new KeywordAnalyzer();

                IndexWriterConfig config = new IndexWriterConfig(
                         proteinAnalyzer);

                try (IndexWriter w = new IndexWriter(proteinIndex, config)) {
                    String sCurrentLine;
                    
                    br = new BufferedReader(new FileReader(proteinFile));
                    
                    while ((sCurrentLine = br.readLine()) != null) {
                        String[] columns = sCurrentLine.split("\t");
                        String protein = columns[0];
                        if ("UNIPROT_AC".equals(protein)) {
                            continue;
                        }
                        
                        addDoc(w, protein, sCurrentLine);
                    }
                }
                br.close();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error when initializing client for Interactome3D", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error when initializing client for Interactome3D", ex);
            }
        }

    }

    public String getPath() {
        return path;
    }

    @Override
    public Collection<I3DInteractionStructure> getInteractionStructures(
            String proteinAc1, String proteinAc2) throws Interactome3DException {

        ArrayList<I3DInteractionStructure> structures = new ArrayList<>();

        try {
            DirectoryReader reader = DirectoryReader.open(interactionIndex);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser("contents",
                    interactionAnalyzer);

            Query query = parser.parse("(PROT1:" + proteinAc1 + " AND PROT2:"
                    + proteinAc2 + ") OR (PROT2:" + proteinAc1 + " AND PROT1:"
                    + proteinAc2 + ")");

            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.doc(docId);
                String[] columns = d.get("line").split("\t");
                I3DInteractionStructure structure = new I3DInteractionStructure();
                // PROT1 PROT2 RANK_MAJOR RANK_MINOR TYPE PDB_ID BIO_UNIT
                // CHAIN1 MODEL1 SEQ_IDENT1 COVERAGE1 SEQ_BEGIN1 SEQ_END1 DOMAIN1
                // CHAIN2 MODEL2 SEQ_IDENT2 COVERAGE2 SEQ_BEGIN2 SEQ_END2 DOMAIN2
                // FILENAME
                structure.setUniprotAc1(columns[0]);
                structure.setUniprotAc2(columns[1]);
                structure.setRankMajor(columns[2]);
                structure.setRankMinor(columns[3]);
                structure.setStructureType(columns[4]);
                structure.setPdbId(columns[5]);
                // structure.setUniprotAc1(columns[6]);
                structure.setChainId1(columns[7]);
                // structure.setChainId1(columns[8]);
                structure.setSequenceIdentity1(columns[9]);
                structure.setCoverage1(columns[10]);
                structure.setStart1(Integer.parseInt(columns[11]));
                structure.setEnd1(Integer.parseInt(columns[12]));
                structure.setDomain1(columns[13]);
                structure.setChainId2(columns[14]);
                // structure.setChainId2(columns[15]);
                structure.setSequenceIdentity2(columns[16]);
                structure.setCoverage2(columns[17]);
                structure.setStart2(Integer.parseInt(columns[18]));
                structure.setEnd2(Integer.parseInt(columns[19]));
                structure.setDomain2(columns[20]);
                structure.setFilename(columns[21]);
                structures.add(structure);
            }

        } catch (ParseException | IOException e) {
            logger.log(Level.SEVERE, "Cannot open the index: {0}", this.getPath());
            throw new Interactome3DException("Cannot open the index");
        } 
        return structures;
    }

    @Override
    public Collection<I3DProteinStructure> getProteinStructures(String proteinAc) throws Interactome3DException {

        ArrayList<I3DProteinStructure> structures = new ArrayList<>();

        DirectoryReader reader;
        try {
            reader = DirectoryReader.open(proteinIndex);
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser( "contents",
                    proteinAnalyzer);

            Query query = parser.parse("UNIPROT_AC:" + proteinAc);

            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.doc(docId);
                String[] columns = d.get("line").split("\t");
                I3DProteinStructure structure = new I3DProteinStructure();
                // UNIPROT_AC RANK_MAJOR RANK_MINOR TYPE PDB_ID CHAIN SEQ_IDENT
                // COVERAGE
                // SEQ_BEGIN SEQ_END GA431 MPQS ZDOPE FILENAME
                structure.setUniprotAc(columns[0]);
                structure.setRankMajor(columns[1]);
                structure.setRankMinor(columns[2]);
                structure.setStructureType(columns[3]);
                structure.setPdbId(columns[4]);
                structure.setChainId(columns[5]);
                structure.setSequenceIdentity(columns[6]);
                structure.setCoverage(columns[7]);
                structure.setStart(Integer.parseInt(columns[8]));
                structure.setEnd(Integer.parseInt(columns[9]));
                structure.setGa341(columns[10]);
                structure.setMpqs(columns[11]);
                structure.setZdope(columns[12]);
                structure.setFilename(columns[13]);
                structures.add(structure);
            }

        } catch (ParseException | IOException e) {
            throw new Interactome3DException("Cannot open the index");
        }
        return structures;
    }

    public Collection<String> getInteractors(String proteinAc) {

        HashSet<String> interactors = new HashSet<>();

        DirectoryReader reader;
        try {
            reader = DirectoryReader.open(interactionIndex);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error when getting interactors from Interactome3D", e);
            return interactors;
        }
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser( "contents",
                interactionAnalyzer);

        Query query;
        try {
            query = parser.parse("PROT1:" + proteinAc + " OR PROT2:"
                    + proteinAc);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Error when getting interactors from Interactome3D", e);
            return interactors;
        }

        TopDocs topDocs;
        try {
        	topDocs = searcher.search(query, reader.maxDoc());

            ScoreDoc[] hits = topDocs.scoreDocs;

            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document d = searcher.doc(docId);
                String ac1 = d.get("PROT1");
                String ac2 = d.get("PROT2");
                if (ac1.equals(ac2)) {
                    interactors.add(ac1);
                } else if (false == ac1.equals(proteinAc)) {
                    interactors.add(ac1);
                } else if (false == ac2.equals(proteinAc)) {
                    interactors.add(ac2);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error when getting interactors from Interactome3D", e);
        }

        return interactors;
    }

    // Interaction
    private void addDoc(IndexWriter w, String protein1, String protein2,
            String interactome3Dline) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("PROT1", protein1, Field.Store.YES));
        doc.add(new TextField("PROT2", protein2, Field.Store.YES));
        doc.add(new TextField("line", interactome3Dline, Field.Store.YES));
        w.addDocument(doc);
    }

    // protein
    private void addDoc(IndexWriter w, String protein, String interactome3Dline)
            throws IOException {
        Document doc = new Document();
        doc.add(new TextField("UNIPROT_AC", protein, Field.Store.YES));
        doc.add(new TextField("line", interactome3Dline, Field.Store.YES));
        w.addDocument(doc);
    }

    public int getNumberOfInteractionStructures() {
        DirectoryReader reader;
        try {
            reader = DirectoryReader.open(interactionIndex);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error when getting number of structures in Interactome3D", e);
            return 0;
        }
        return reader.numDocs();
    }

    public int getNumberOfProteinStructures() {
        DirectoryReader reader;
        try {
            reader = DirectoryReader.open(proteinIndex);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error when getting number of structures in Interactome3D", e);
            return 0;
        }
        return reader.numDocs();
    }
}
