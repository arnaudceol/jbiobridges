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
package it.iit.genomics.cru.bridges.ensembl;

import static org.junit.Assert.assertEquals;
import it.iit.genomics.cru.bridges.ensembl.model.EnsemblException;
import it.iit.genomics.cru.bridges.ensembl.model.Gene;

import java.util.Collection;

import org.junit.Test;

public class TestsEnsemblClient {

    
    public void testGetByName() throws EnsemblException {

        EnsemblClient client;

        client = EnsemblClientManager.getInstance().getClient(
                EnsemblClientManager.SPECIES_HUMAN);

        String name = "TP53";

        Collection<Gene> genes;

        genes = client.getGenesHgncSymbol(name);

        assertEquals(1, genes.size());
        assertEquals("TP53", genes.iterator().next().getName());

    }

    
    public void testExons() throws EnsemblException {

        EnsemblClient client = EnsemblClientManager.getInstance().getClient(
                EnsemblClientManager.SPECIES_HUMAN);

		// int[][] positions = { { 1, 115256529, 115256530 },
        // { 1, 115256530, 115256531 }, { 10, 112333495, 112333496 },
        // { 11, 32413566, 32413567 }, { 12, 112926889, 112926890 },
        // { 13, 28578215, 28578216 }, { 15, 90631839, 90631840 },
        // { 15, 90631918, 90631919 }, { 15, 90631935, 90631936 },
        // { 2, 25457243, 25457244 }, { 21, 36252995, 36252996 },
        // { 21, 44524457, 44524458 }, { 4, 55599322, 55599323 },
        // { 7, 142960621, 142960622 }, { 8, 117875450, 117875451 } };
        int[] position = {1, 115256529, 115256530};

        Collection<Gene> genes = client.getGenesByPosition("" + position[0],
                position[1], position[2]);

        assertEquals(1, genes.size());

        Gene gene = genes.iterator().next();

        assertEquals("ENSG00000213281", gene.getEnsemblGeneID());

        assertEquals(7, client.getExons(gene.getEnsemblGeneID()).size());

        genes = client.getGenesEnsemblGeneId(gene.getEnsemblGeneID());
        assertEquals(1, genes.size());

    }

}
