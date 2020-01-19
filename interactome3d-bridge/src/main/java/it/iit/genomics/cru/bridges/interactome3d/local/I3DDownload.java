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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class I3DDownload {

    private final static Logger logger = Logger.getLogger(I3DDownload.class.getName());

    private static final HashMap<String, String> taxid2i3d = new HashMap<>();

    private final String downloadPath;

    // "complete"
    public final String DATASET_TYPE = "representative";
    
    static {
        taxid2i3d.put("9606", "human");
        taxid2i3d.put("10090", "mouse");
    }

    public I3DDownload(String downloadPath) {
        if (false == downloadPath.endsWith(File.separator)) {
            downloadPath = downloadPath + File.separator;
        }
        this.downloadPath = downloadPath;

    }

    public String getI3DdatPath(String taxid) {
        String speciesLabel = taxid2i3d.get(taxid);
        return downloadPath + "interactome3d_" + speciesLabel;
    }

    public boolean isDatDownloaded(String taxid) {
        File i3dInteractionFile = new File(getI3DdatPath(taxid) + "/interactions.dat");
        File i3dProteinFile = new File(getI3DdatPath(taxid) + "/proteins.dat");
        return i3dInteractionFile.exists() && i3dProteinFile.exists();
    }

    /**
     * Download the interaction.dat file from interactome3d if it doesn't
     * already exists
     */
    public void downloadDat(String taxid) {

        String speciesLabel = taxid2i3d.get(taxid);
		// check if exists
        // http://interactome3d.irbbarcelona.org/user_data/human/download/complete/interactions.dat
        // Download

        // Create directory ?
        File i3dDirectory = new File(getI3DdatPath(taxid));

        if (false == i3dDirectory.exists()) {
            i3dDirectory.mkdir();
        }

        URL website;
        try {
            website = new URL(
                    "https://interactome3d.irbbarcelona.org/user_data/"
                    + speciesLabel
                    + "/download/" + DATASET_TYPE + "/interactions.dat");

            ReadableByteChannel rbc = Channels.newChannel(website
                    .openStream());

            FileOutputStream fos = new FileOutputStream(
                    getI3DdatPath(taxid) + "/interactions.dat");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

            website = new URL(
                    "https://interactome3d.irbbarcelona.org/user_data/"
                    + speciesLabel
                    + "/download/" + DATASET_TYPE + "/proteins.dat");

            rbc = Channels.newChannel(website
                    .openStream());

            fos = new FileOutputStream(
                    getI3DdatPath(taxid) + "/proteins.dat");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}Cannot download data from Interactome3D for taxid ", taxid);
        }

    }

}
