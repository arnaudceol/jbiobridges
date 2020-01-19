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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Interactome3DJaxbClient {

    private final static Logger logger = Logger.getLogger(Interactome3DJaxbClient.class.getName());

    public final static String i3dUrl = "http://interactome3d.irbbarcelona.org/api/";

    public List<I3DProteinStructure> getProteinStructures(String uniprotAc) {
        try {
            URL url = new URL(i3dUrl + "/getProteinStructures?uniprot_ac="
                    + uniprotAc);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmler = xmlif.createXMLStreamReader(conn.getInputStream());

            JAXBContext jaxbContext = JAXBContext
                    .newInstance(Interactome3D.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Interactome3D result = (Interactome3D) jaxbUnmarshaller
                    .unmarshal(xmler);
            
            conn.disconnect();
            return result.getProteinStructureList();
        } catch (IOException | JAXBException | XMLStreamException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public List<I3DInteractionStructure> getInteractionStructures(
            String uniprotAc1, String uniprotAc2) {

        try {
            URL url = new URL(i3dUrl + "/getInteractionStructures?queryProt1="
                    + uniprotAc1 + "&queryProt2=" + uniprotAc2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmler = xmlif.createXMLStreamReader(conn.getInputStream());

            JAXBContext jaxbContext = JAXBContext.newInstance(
                    Interactome3D.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Interactome3D result = (Interactome3D) jaxbUnmarshaller
                    .unmarshal(xmler);

            conn.disconnect();
            
            return result.getInteractionStructureList();

        } catch (IOException | JAXBException | XMLStreamException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public enum QueryType {

        protein, interaction
    }

    public List<I3DPdbFile> getPdbFiles(String fileName, QueryType type) {
        try {
            URL url = new URL(i3dUrl + "getPdbFile?filename=" + fileName
                    + "&type=" + type);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            XMLStreamReader xmler = xmlif.createXMLStreamReader(conn.getInputStream());
            
            JAXBContext jaxbContext = JAXBContext.newInstance(
                    Interactome3D.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            Interactome3D result = (Interactome3D) jaxbUnmarshaller
                    .unmarshal(xmler);

            conn.disconnect();

            return result.getPdbFileList();
        } catch (IOException | JAXBException | XMLStreamException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static void main(String[] args) {

        Interactome3DJaxbClient client = new Interactome3DJaxbClient();

        System.out.println("Get interaction structures");
        List<I3DInteractionStructure> structs = client
                .getInteractionStructures("P09147", "P09147");
        System.out.println(structs.size());
        for (I3DInteractionStructure struct : structs) {
            System.out.println(struct.getPdbId() + ": " + struct.getFilename());
        }

        System.out.println("\nGet protein structures");
        List<I3DProteinStructure> prots = client.getProteinStructures("A0A5B9");
        I3DProteinStructure prot = prots.iterator().next();
        System.out.println(prots.size());
        System.out.println(prot.getPdbId() + ": " + prot.getFilename());

        System.out.println("\nGet PDB: " + prot.getFilename());
        List<I3DPdbFile> pdbs = client.getPdbFiles(prot.getFilename(),
                QueryType.protein);
        System.out.println(prot.getFilename() + " : " + pdbs.size());
        // for (PdbFile pdb : pdbs) {
        // System.out.println(pdb.getContents() );
        // }
    }

}
