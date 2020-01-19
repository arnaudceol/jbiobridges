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
package it.iit.genomics.cru.bridges.ensembl.model;

import java.util.HashSet;

public class Gene {

    private final String ensemblGeneID;

    private final String name;

    private final String chromosomeName;

    private final int start;

    private final int end;

    private final boolean reverse;

    private HashSet<String> uniprotAcs = new HashSet<>();

    private final HashSet<Exon> exons = new HashSet<>();

    public Gene(String ensemblGeneID, String name, String chromosomeName,
            int start, int end, String strand, HashSet<String> uniprotAcs) {
        super();
        this.ensemblGeneID = ensemblGeneID;
        this.name = name;
        this.chromosomeName = chromosomeName;
        this.start = start;
        this.end = end;
        this.reverse = "-1".equals(strand);
        this.uniprotAcs = uniprotAcs;
    }

    public String getEnsemblGeneID() {
        return ensemblGeneID;
    }

    public String getName() {
        return name;
    }

    public String getChromosomeName() {
        return chromosomeName;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getMin() {
        if (start < end) {
            return start;
        }

        return end;
    }

    public int getMax() {
        if (start > end) {
            return start;
        }

        return end;
    }

    public boolean isReverseStrand() {
        return reverse;
    }

    public HashSet<String> getUniprotAcs() {
        return uniprotAcs;
    }

    public HashSet<Exon> getExons() {
        return exons;
    }

    @Override
    public String toString() {
        return "Gene[ensemblGeneID=" + ensemblGeneID
                + ", name=" + name
                + ", chromosomeName=" + chromosomeName
                + ", start=" + start
                + ", end=" + end
                + ", reverse=" + reverse
                + ", uniprotAcs=" + uniprotAcs;
    }

}
