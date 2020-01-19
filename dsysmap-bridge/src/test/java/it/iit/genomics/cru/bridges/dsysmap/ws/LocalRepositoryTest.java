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

import it.iit.genomics.cru.bridges.dsysmap.local.DSysMapDownload;
import it.iit.genomics.cru.bridges.dsysmap.local.DSysMapLocalRepository;
import it.iit.genomics.cru.bridges.dsysmap.model.DSysMapResult;
import it.iit.genomics.cru.bridges.dsysmap.model.InterfaceResidue;
import it.iit.genomics.cru.bridges.dsysmap.model.StructuredResidue;
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
public class LocalRepositoryTest {
    
    public LocalRepositoryTest() {
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
        String path = System.getProperty("java.io.tmpdir" );
        
        DSysMapDownload download = new DSysMapDownload(path);
        
        if (false ==  download.isDatDownloaded()) {
            System.out.print("download");
            download.downloadDat();
        }
        
        DSysMapLocalRepository client = new DSysMapLocalRepository(download.getDSysMapDatPath());
        
        ArrayList<String> mutations = new ArrayList<>();         
                
        mutations.add("P13647:K404K");
        
        DSysMapResult result = client.mapMutations(mutations);        
        
        for (StructuredResidue mut: result.getStructuredResidues()) {
            System.out.println("str. residues: " + mut.getFileName() + " " + mut.getChain()  + " " + mut.getResidueNumber());
        }
        
        for (InterfaceResidue mut: result.getInterfaceResidues()) {
            System.out.println("int. residues: " + mut.getFileName() + " " + mut.getChain()  + " " + mut.getResidueNumber());
        }
                
//          
//        for (String r: client.getStructuredResidues("Q01196","Q01196-Q01196-EXP-1ljm.pdb3-B-1-A-1")) {
//            System.out.println(" - " + r);
//        }
//        
//        for (String r: client.getInterfaceResidues("Q01196","Q13951",  "O00499-P01106-EXP-pdb1mv0.ent-B-0-A-0")) {
//            System.out.println(" - " + r);
//        }
    }
}
