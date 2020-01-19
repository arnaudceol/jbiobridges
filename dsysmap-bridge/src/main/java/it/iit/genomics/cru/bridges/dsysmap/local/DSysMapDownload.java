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

import it.iit.genomics.cru.bridges.interactome3d.local.I3DDownload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSysMapDownload {

    private final static Logger logger = Logger.getLogger(I3DDownload.class.getName());
   
    private final String downloadPath;
   
    public DSysMapDownload(String downloadPath) {
        if (false == downloadPath.endsWith(File.separator)) {
            downloadPath = downloadPath + File.separator;
        }
        this.downloadPath = downloadPath;
    }
    
    
    public String getDSysMapDatPath() {
        return downloadPath + "dsysmap";
    }

    public boolean isDatDownloaded() {
        File dsysmapInteractionFile = new File(getDSysMapDatPath() + "/interfaces.dat");
        File dsysmapProteinFile = new File(getDSysMapDatPath() + "/structures.dat");
        return dsysmapInteractionFile.exists() && dsysmapProteinFile.exists();
    }

    /**
     * Download the interaction.dat file from interactome3d if it doesn't
     * already exists
     */
    public void downloadDat() {

        // Create directory ?
        File dsysmapDirectory = new File(getDSysMapDatPath());

        if (false == dsysmapDirectory.exists()) {
            dsysmapDirectory.mkdir();
        }

        URL website;
        try {
            website = new URL(
                    "https://dsysmap.irbbarcelona.org/repository/all_mutations_interf_res.dat");

            ReadableByteChannel rbc = Channels.newChannel(website
                    .openStream());

            FileOutputStream fos = new FileOutputStream(
                    getDSysMapDatPath() + "/interfaces.dat");

            logger.log(Level.INFO,"Download " + "https://dsysmap.irbbarcelona.org/repository/all_mutations_interf_res.dat" + " to  {0}/interfaces.dat", getDSysMapDatPath());
            
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

            website = new URL(
                    "https://dsysmap.irbbarcelona.org/repository/all_mutations_prot_structs.dat");

            rbc = Channels.newChannel(website
                    .openStream());

            fos = new FileOutputStream(
                    getDSysMapDatPath() + "/structures.dat");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

        } catch (IOException e) {
           logger.log(Level.SEVERE, "Cannot download data from DSysMap" , e);
        }

    }

}
