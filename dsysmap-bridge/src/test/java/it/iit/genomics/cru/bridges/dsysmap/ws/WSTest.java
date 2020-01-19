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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.iit.genomics.cru.bridges.dsysmap.ws;

import it.iit.genomics.cru.bridges.dsysmap.model.DSysMapResult;
import it.iit.genomics.cru.bridges.dsysmap.model.InterfaceResidue;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author aceol
 */
public class WSTest {
    
    public WSTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testMutMap() {
    //APC:p.Ala1582Lys,p.Thr506Trp|AXIN1:p.Phe119Ala,p.Gln190Arg
        
        DSysMapClient client = new DSysMapWSClient();
        
        ArrayList<String> mutations = new ArrayList<>();         
       
         mutations.add("P84022:T261I"); //Q01196:A107Y");
        
        DSysMapResult result = client.mapMutations(mutations);        
        
        for (InterfaceResidue mut: result.getInterfaceResidues()) {
            System.out.println("int. residues: " + mut.getFileName() + " " + mut.getChain()  + " " + mut.getResidueNumber());
        }
        
        for (InterfaceResidue mut: result.getInterfaceResidues()) {
            System.out.println(mut.getFileName() + " " + mut.getChain()  + " " + mut.getResidueNumber());
        }
    }
}
