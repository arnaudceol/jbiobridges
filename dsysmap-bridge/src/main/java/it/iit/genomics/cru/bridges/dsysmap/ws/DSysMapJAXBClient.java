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
import org.apache.commons.lang.StringUtils;

public class DSysMapJAXBClient  implements DSysMapClient {

    public final static Logger logger = Logger.getLogger(DSysMapJAXBClient.class.getName());

    public final static String dsysmapUrl = "https://dsysmap.irbbarcelona.org/api/";

    @Override
    public DSysMapResult mapMutations(List<String> mutations) {
        try {

            DSysMapResult result = new DSysMapResult();

            // Only 10 at once
            for (int i = 0; i < mutations.size(); i += 10) {

                URL url = new URL(dsysmapUrl + "mapMutations?mutations="
                        + StringUtils.join(mutations.subList(i, Math.min(i + 10, mutations.size())), "|"));

                System.out.println("JAXB URL: " +url);
                
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
                        .newInstance(DSysMapResult.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                DSysMapResult partialResult = (DSysMapResult) jaxbUnmarshaller
                        .unmarshal(xmler);
                result.getStructuredResidues().addAll(partialResult.getStructuredResidues());
                result.getInterfaceResidues().addAll(partialResult.getInterfaceResidues());
                
                conn.disconnect();
            }
            return result;
        } catch (XMLStreamException | JAXBException | IOException e) {
            logger.log(Level.SEVERE, "Failed to map mutations from " + dsysmapUrl, e);
        }
        return null;
    }
}
