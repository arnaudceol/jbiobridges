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
package it.iit.genomics.cru.bridges;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Arnaud Ceol
 * @date 01/mar/2013
 * @time 15:56:24
 */
public class UniprotkbUtilsTest {

	// Skip this test untill we find an alternative to the usage of teh 181 keywor
	public void testGetUniprotAcFromGene() {
		
		String[] expectedAcs = {"P07813"};
		
		String[] acs = UniprotkbUtils.getUniprotAcFromGene("leuS", "Escherichia coli (strain K12)");
		
		Assert.assertArrayEquals(expectedAcs, acs);
	}

	@Test
	public void testGetGeneFromUniprotAc() {
		
		String expectedGene = "sucC";
		
		String gene = UniprotkbUtils.getGeneFromUniprotAc("P0A836");
		
		Assert.assertEquals(expectedGene, gene);
	}
	

	@Test
	public void testGetGenesFromUniprotAcs() {
		
//		String expectedGene = "sucC";
		
		String[] uniprotAcs = { "Q9NUR3", "P52803", "P02765", "Q9BX66", "P29074", "Q9ULD4" };
		
		ArrayList<String> uniprotAcsCollection = new ArrayList<String>();
		for (String uniprotAc : uniprotAcs) {
			uniprotAcsCollection.add(uniprotAc);
		}
		
		HashMap<String, String> genes = UniprotkbUtils.getGenesFromUniprotAcs(uniprotAcsCollection);
		
		Assert.assertEquals(uniprotAcs.length, genes.values().size());
	}
	

	@Test
	public void testIsUniprotAcs() {
		Assert.assertTrue(UniprotkbUtils.isUniprotAc("P12345") && UniprotkbUtils.isUniprotAc("P123X5-12")  && false == UniprotkbUtils.isUniprotAc("CHEBI:1223")   && false == UniprotkbUtils.isUniprotAc("P123X5-12-3") );
	}
	
	
	
}
