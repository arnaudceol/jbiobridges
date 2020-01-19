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
package it.iit.genomics.cru.bridges.dsysmap.ws;

import it.iit.genomics.cru.bridges.dsysmap.model.DSysMapResult;
import it.iit.genomics.cru.bridges.dsysmap.model.InterfaceResidue;
import it.iit.genomics.cru.bridges.dsysmap.model.StructuredResidue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

public class DSysMapMockClient implements DSysMapClient {

    public final static Logger logger = Logger.getLogger(DSysMapMockClient.class.getName());

    public final static String dsysmapUrl = "https://dsysmap.irbbarcelona.org/api/";

    @Override
    public DSysMapResult mapMutations(List<String> mutations) {
        try {
            DSysMapResult result = new DSysMapResult();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("dsysmap.xml").getFile());
            FileInputStream is = new FileInputStream(file);
            DSysMapResult partialResult = parse(is);
            result.getStructuredResidues().addAll(partialResult.getStructuredResidues());
            result.getInterfaceResidues().addAll(partialResult.getInterfaceResidues());
            return result;
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to map mutations from " + dsysmapUrl, e);
        }
        return null;
    }

    public DSysMapResult parse(InputStream is) {

        DSysMapResult result = new DSysMapResult();

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
            NodeList structureList = doc.getElementsByTagName("structured_residue");

            for (int i = 0; i < structureList.getLength(); i++) {

                Element structureElement = (Element) structureList.item(i);

                StructuredResidue structure = new StructuredResidue();

                structure.setUniprotAc(structureElement.getElementsByTagName("uniprot_ac").item(0).getFirstChild().getNodeValue());
                structure.setResidueNumber(Integer.parseInt(structureElement.getElementsByTagName("res_num").item(0).getFirstChild().getNodeValue()));
                structure.setOriginalResidue(structureElement.getElementsByTagName("res_orig").item(0).getFirstChild().getNodeValue());
                structure.setStructureType(structureElement.getElementsByTagName("struct_type").item(0).getFirstChild().getNodeValue());
                structure.setFileName(structureElement.getElementsByTagName("pdb_filename").item(0).getFirstChild().getNodeValue());
                structure.setChain(structureElement.getElementsByTagName("chain").item(0).getFirstChild().getNodeValue());
                structure.setStructureResidueNumber(Integer.parseInt(structureElement.getElementsByTagName("struct_res_num").item(0).getFirstChild().getNodeValue()));
                structure.setStructutreResidueName(structureElement.getElementsByTagName("struct_res_name").item(0).getFirstChild().getNodeValue());
                structure.setClassification(structureElement.getElementsByTagName("classification").item(0).getFirstChild().getNodeValue());

                result.getStructuredResidues().add(structure);
            }

            // interaction structure			
            NodeList interfaceList = doc.getElementsByTagName("interface_residue");

            for (int i = 0; i < interfaceList.getLength(); i++) {

                Element structureElement = (Element) interfaceList.item(i);

                InterfaceResidue structure = new InterfaceResidue();

                structure.setUniprotAc(structureElement.getElementsByTagName("uniprot_ac").item(0).getFirstChild().getNodeValue());
                structure.setResidueNumber(Integer.parseInt(structureElement.getElementsByTagName("res_num").item(0).getFirstChild().getNodeValue()));
                structure.setOriginalResidue(structureElement.getElementsByTagName("res_orig").item(0).getFirstChild().getNodeValue());
                structure.setStructureType(structureElement.getElementsByTagName("struct_type").item(0).getFirstChild().getNodeValue());
                structure.setFileName(structureElement.getElementsByTagName("pdb_filename").item(0).getFirstChild().getNodeValue());
                structure.setChain(structureElement.getElementsByTagName("chain").item(0).getFirstChild().getNodeValue());
                structure.setStructureResidueNumber(Integer.parseInt(structureElement.getElementsByTagName("struct_res_num").item(0).getFirstChild().getNodeValue()));
                structure.setStructutreResidueName(structureElement.getElementsByTagName("struct_res_name").item(0).getFirstChild().getNodeValue());
                structure.setPartner(structureElement.getElementsByTagName("partner").item(0).getFirstChild().getNodeValue());
                structure.setPartnerChain(structureElement.getElementsByTagName("partner_chain").item(0).getFirstChild().getNodeValue());

                result.getInterfaceResidues().add(structure);
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException e) {
            logger.log(Level.SEVERE, "Failed to parse mutations from " + dsysmapUrl, e);
        }

        return result;
    }

}
