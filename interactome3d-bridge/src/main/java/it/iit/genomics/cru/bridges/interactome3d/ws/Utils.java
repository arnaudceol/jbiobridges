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

import java.util.HashMap;

public class Utils {

	
	public final static String STRUCTURE_TYPE_STRUCTURE =  "Structure";
	
	public final static String STRUCTURE_TYPE_MODEL =  "Model";
	
	public final static String[] datasets = {"athaliana", "celegans", "fly", "ecoli", "hpylori", "human", "mouse", "mtuberculosis", "yeast"};
	
	private final static HashMap<String, String> taxid2dataset = new HashMap<String, String>();
	
	static {
		taxid2dataset.put("3702", "athaliana"); 
		taxid2dataset.put("6239", "celegans"); 
		taxid2dataset.put("7227", "fly"); 
		taxid2dataset.put("83333", "ecoli"); 
		taxid2dataset.put("85692", "hpylori"); 
		taxid2dataset.put("9606", "human"); 
		taxid2dataset.put("10090", "mouse"); 
		taxid2dataset.put("1773", "mtuberculosis"); 
		taxid2dataset.put("559292", "yeast");
	}
	
	public static String getDataset(String taxid) throws Interactome3DException {
		if (false == taxid2dataset.containsKey(taxid)) {
			throw new Interactome3DException("Taxid "+taxid+" is not available in current version of Interactome3D");
		}
		
		return taxid2dataset.get(taxid);
	}
	
}
