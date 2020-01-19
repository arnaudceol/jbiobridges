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
package it.iit.genomics.cru.bridges.interactome3d.ws;

import it.iit.genomics.cru.bridges.interactome3d.model.I3DInteractionStructure;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DPdbFile;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DProteinStructure;
import it.iit.genomics.cru.bridges.interactome3d.model.Interactome3D;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Interactome3DWSClient implements Interactome3DClient {

    private final static Logger logger = Logger.getLogger(Interactome3DWSClient.class.getName());

    public final static String i3dUrl = "https://interactome3d.irbbarcelona.org/api/";

    public enum QueryType {
        protein, interaction
    }

    @Override
    public Collection<I3DProteinStructure> getProteinStructures(String uniprotAc)
            throws Interactome3DException {
        try {
            URL url = new URL(i3dUrl + "/getProteinStructures?uniprot_ac="
                    + uniprotAc);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new Interactome3DException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            Interactome3D result = parse(conn.getInputStream());

            conn.disconnect();

            return result.getProteinStructureList();

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public Collection<I3DInteractionStructure> getInteractionStructures(
            String uniprotAc1, String uniprotAc2) throws Interactome3DException {

        try {
            URL url = new URL(i3dUrl + "/getInteractionStructures?queryProt1="
                    + uniprotAc1 + "&queryProt2=" + uniprotAc2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new Interactome3DException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            Interactome3D result = parse(conn.getInputStream());

            conn.disconnect();

            return result.getInteractionStructureList();

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public Collection<I3DPdbFile> getPdbFiles(String fileName, QueryType type)
            throws Interactome3DException {
        try {
            URL url = new URL(i3dUrl + "getPdbFile?filename=" + fileName
                    + "&type=" + type);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new Interactome3DException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            Interactome3D result = parse(conn.getInputStream());

            conn.disconnect();

            return result.getPdbFileList();

        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public Interactome3D parse(InputStream is) {

        Interactome3D result = new Interactome3D();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // interaction structure			
            NodeList structureList = doc.getElementsByTagName("interaction_structure");

            for (int i = 0; i < structureList.getLength(); i++) {

                Element structureElement = (Element) structureList.item(i);

                I3DInteractionStructure structure = new I3DInteractionStructure();

                structure.setUniprotAc1(structureElement.getElementsByTagName("prot1").item(0).getFirstChild().getNodeValue());
                structure.setChainId1(structureElement.getElementsByTagName("chain1").item(0).getFirstChild().getNodeValue());
                structure.setSequenceIdentity1(structureElement.getElementsByTagName("seq_id1").item(0).getFirstChild().getNodeValue());
                structure.setCoverage1(structureElement.getElementsByTagName("cov1").item(0).getFirstChild().getNodeValue());
                structure.setStart1(Integer.parseInt(structureElement.getElementsByTagName("start1").item(0).getFirstChild().getNodeValue()));
                structure.setEnd1(Integer.parseInt(structureElement.getElementsByTagName("end1").item(0).getFirstChild().getNodeValue()));
                structure.setDomain1(structureElement.getElementsByTagName("domain1").item(0).getFirstChild().getNodeValue());

                structure.setUniprotAc2(structureElement.getElementsByTagName("prot2").item(0).getFirstChild().getNodeValue());
                structure.setChainId2(structureElement.getElementsByTagName("chain2").item(0).getFirstChild().getNodeValue());
                structure.setSequenceIdentity2(structureElement.getElementsByTagName("seq_id2").item(0).getFirstChild().getNodeValue());
                structure.setCoverage2(structureElement.getElementsByTagName("cov2").item(0).getFirstChild().getNodeValue());
                structure.setStart2(Integer.parseInt(structureElement.getElementsByTagName("start2").item(0).getFirstChild().getNodeValue()));
                structure.setEnd2(Integer.parseInt(structureElement.getElementsByTagName("end2").item(0).getFirstChild().getNodeValue()));
                structure.setDomain2(structureElement.getElementsByTagName("domain2").item(0).getFirstChild().getNodeValue());

                structure.setRankMajor(structureElement.getElementsByTagName("rank_major").item(0).getFirstChild().getNodeValue());
                structure.setRankMinor(structureElement.getElementsByTagName("rank_minor").item(0).getFirstChild().getNodeValue());
                structure.setPdbId(structureElement.getElementsByTagName("pdb").item(0).getFirstChild().getNodeValue());
                structure.setStructureType(structureElement.getElementsByTagName("stype").item(0).getFirstChild().getNodeValue());
                structure.setFilename(structureElement.getElementsByTagName("filename").item(0).getFirstChild().getNodeValue());

                result.addInteractionStructure(structure);
            }

            // Protein
            structureList = doc.getElementsByTagName("protein_structure");

            for (int iStructure = 0; iStructure < structureList.getLength(); iStructure++) {

                Element structureElement = (Element) structureList.item(iStructure);

                I3DProteinStructure structure = new I3DProteinStructure();

                structure.setUniprotAc(structureElement.getElementsByTagName("uniprot_ac").item(0).getFirstChild().getNodeValue());
                structure.setChainId(structureElement.getElementsByTagName("chain_id").item(0).getFirstChild().getNodeValue());
                structure.setSequenceIdentity(structureElement.getElementsByTagName("seq_id").item(0).getFirstChild().getNodeValue());
                structure.setCoverage(structureElement.getElementsByTagName("coverage").item(0).getFirstChild().getNodeValue());
                structure.setStart(Integer.parseInt(structureElement.getElementsByTagName("prot_start").item(0).getFirstChild().getNodeValue()));
                structure.setEnd(Integer.parseInt(structureElement.getElementsByTagName("prot_end").item(0).getFirstChild().getNodeValue()));
                structure.setGa341(structureElement.getElementsByTagName("ga341").item(0).getFirstChild().getNodeValue());
                structure.setMpqs(structureElement.getElementsByTagName("mpqs").item(0).getFirstChild().getNodeValue());
                structure.setZdope(structureElement.getElementsByTagName("zdope").item(0).getFirstChild().getNodeValue());
                structure.setRankMajor(structureElement.getElementsByTagName("rank_major").item(0).getFirstChild().getNodeValue());
                structure.setRankMinor(structureElement.getElementsByTagName("rank_minor").item(0).getFirstChild().getNodeValue());
                structure.setPdbId(structureElement.getElementsByTagName("pdb_id").item(0).getFirstChild().getNodeValue());
                structure.setStructureType(structureElement.getElementsByTagName("stype").item(0).getFirstChild().getNodeValue());
                structure.setFilename(structureElement.getElementsByTagName("filename").item(0).getFirstChild().getNodeValue());

                result.addProteinStructure(structure);
            }

            // PPDB File
            NodeList pdbFiles = doc.getElementsByTagName("pdb_file");

            for (int i = 0; i < pdbFiles.getLength(); i++) {

                Element pdbElement = (Element) pdbFiles.item(i);

                I3DPdbFile pdbFile = new I3DPdbFile();

                pdbFile.setContents(pdbElement.getElementsByTagName("contents").item(0).getFirstChild().getNodeValue());
                pdbFile.setFilename(pdbElement.getElementsByTagName("filename").item(0).getFirstChild().getNodeValue());

                result.addPdbFile(pdbFile);
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException e) {
            logger.log(Level.SEVERE, null, e);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        Interactome3DWSClient client = new Interactome3DWSClient();

        System.out.println("Get interaction structures");
        Collection<I3DInteractionStructure> structs = client
                .getInteractionStructures("P09147", "P09147");
        System.out.println(structs.size());
        for (I3DInteractionStructure struct : structs) {
            System.out.println(struct.getPdbId() + ": " + struct.getFilename());
        }

        System.out.println("\nGet protein structures");
        Collection<I3DProteinStructure> prots = client.getProteinStructures("A0A5B9");
        I3DProteinStructure prot = prots.iterator().next();
        System.out.println(prots.size());
        System.out.println(prot.getPdbId() + ": " + prot.getFilename());

        System.out.println("\nGet PDB: " + prot.getFilename());
        Collection<I3DPdbFile> pdbs = client.getPdbFiles(prot.getFilename(),
                QueryType.protein);
        System.out.println(prot.getFilename() + " : " + pdbs.size());

//		for (I3DPdbFile pdb : pdbs) {
////			System.out.println(pdb.getContents());
//			PDBFileParser pdbParser = new PDBFileParser();
//			String pdbContent = pdb.getContents();
//			InputStream is = new ByteArrayInputStream(pdbContent.getBytes());
//
////			Structure pdbStructure = pdbParser.parsePDBFile(is);
//
//			// Print it
////			String pdbCode = pdbStructure.toPDB();
//			// System.out.println("PDB code: "+pdbCode);
//			// FileWriter fstream = new FileWriter(Utils.tempPath
//			// + i3dFilename);
//			// BufferedWriter out = new BufferedWriter(fstream);
//			// out.write(pdbCode);
//			// out.close();
//		}
    }

}
