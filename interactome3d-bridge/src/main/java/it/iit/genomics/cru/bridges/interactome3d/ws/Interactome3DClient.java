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
import it.iit.genomics.cru.bridges.interactome3d.model.I3DProteinStructure;

import java.util.Collection;


public interface Interactome3DClient {
	
	public Collection<I3DInteractionStructure> getInteractionStructures(
			String uniprotAc1, String uniprotAc2) throws Interactome3DException ;

	public Collection<I3DProteinStructure> getProteinStructures(String uniprotAc)
			throws Interactome3DException;
	
}
