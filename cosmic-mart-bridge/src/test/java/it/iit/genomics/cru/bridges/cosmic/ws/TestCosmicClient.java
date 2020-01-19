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
package it.iit.genomics.cru.bridges.cosmic.ws;

import it.iit.genomics.cru.bridges.cosmic.model.CosmicException;
import it.iit.genomics.cru.bridges.cosmic.model.CosmicMutation;

import java.util.Collection;


public class TestCosmicClient {

    public void getMutationsByPosition() throws CosmicException {

        CosmicClient client = new CosmicClient(CosmicUtils.COSMIC_DATASET, CosmicUtils.COSMIC_SERVER);
//	 	1:156849949-156849949
        //9:140711892-140711892
        Collection<CosmicMutation> mutations = client.getMutationsByPosition("9", 140711892, 140711892);

        for (CosmicMutation mutation : mutations) {
            System.out.println(
                    "->" + mutation.getGeneName() + "\n   "
                    + mutation.getPrimarySite() + "\n   "
                    + mutation.getPrimaryHistology() + "\n   "
                    + mutation.getTumourSource() + "\n   "
                    + mutation.getSampleName() + "\n   "
                    + mutation.getMutationTypeAA());
        }

    }

    public void getMutationsByGeneName() throws CosmicException {

        CosmicClient client = new CosmicClient(CosmicUtils.COSMIC_DATASET, CosmicUtils.COSMIC_SERVER);
//	 	1:156849949-156849949
        //9:140711892-140711892
        String name = "ABL1";

        Collection<CosmicMutation> mutations = client.getMutationsByGeneName(name);

        for (CosmicMutation mutation : mutations) {
            System.out.println(
                    "->" + mutation.getGeneName() + "\n   "
                    + mutation.getPrimarySite() + "\n   "
                    + mutation.getPrimaryHistology() + "\n   "
                    + mutation.getTumourSource() + "\n   "
                    + mutation.getSampleName() + "\n   "
                    + mutation.getMutationTypeAA());
        }

    }

}
