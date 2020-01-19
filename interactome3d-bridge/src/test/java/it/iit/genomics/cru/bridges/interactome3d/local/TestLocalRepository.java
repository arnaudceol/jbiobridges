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

import static org.junit.Assert.assertEquals;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DInteractionStructure;
import it.iit.genomics.cru.bridges.interactome3d.model.I3DProteinStructure;
import it.iit.genomics.cru.bridges.interactome3d.ws.Interactome3DException;

import java.util.Collection;

import org.junit.Test;

public class TestLocalRepository {

	
	@Test
	public void testParseDirectory() throws Interactome3DException {
		
		String localPath = getClass().getResource("/interactome3D/").getFile();
		
		Interactome3DLocalRepository local = new Interactome3DLocalRepository(localPath) ;
		
		
		// Interactions
		assertEquals(26, local.getNumberOfInteractionStructures());
		
		assertEquals(3, local.getInteractors("A0A5B9").size());
		
		Collection<I3DInteractionStructure> interactionStructures = local.getInteractionStructures("P04229","P04233");
		
		assertEquals(1, interactionStructures.size());
		
		I3DInteractionStructure interactionStructure = interactionStructures.iterator().next();
				
		assertEquals("P04229-P04233-EXP-3pdo.pdb1-B-0-C-0.pdb", interactionStructure.getFilename());
		assertEquals(30, interactionStructure.getStart1());
		assertEquals(219, interactionStructure.getEnd1());
		assertEquals(103, interactionStructure.getStart2());
		assertEquals(119, interactionStructure.getEnd2());
		
		
		// proteins
		assertEquals(25, local.getNumberOfProteinStructures());
				
		Collection<I3DProteinStructure> proteinStructures = local.getProteinStructures("P01892");
		
		assertEquals(2, proteinStructures.size());
				
		I3DProteinStructure proteinStructure = proteinStructures.iterator().next();
		
		assertEquals("P01892-EXP-2bnr_A.pdb", proteinStructure.getFilename());
		assertEquals(25, proteinStructure.getStart());
		assertEquals(300, proteinStructure.getEnd());

		
	}
	
	
}
