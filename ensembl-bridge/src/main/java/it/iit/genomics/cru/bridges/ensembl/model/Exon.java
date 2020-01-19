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

public class Exon {
	
	private final int start;
	
	private final int end;
	
	private String sequence;


	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getMin() {
		if (start <= end) {
			return start;
		}
		
		return end;
	}
	

	public int getMax() {
		if (start >= end) {
			return start;
		}
		
		return end;
	}
	
	public Exon(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exon other = (Exon) obj;
		if (end != other.end)
			return false;
		return start == other.start;
	}
	
	
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	
}