package org.blast.parser.main;

import org.blast.parser.objects.BlastOutput;
import org.blast.parser.objects.Hit;
import org.blast.parser.objects.Hsp;
import org.blast.parser.objects.Iteration;

public class XMLPrinter {

	public void print(BlastOutput blast_output){
		
		System.out.println(blast_output.getBlastOutput_db());
		System.out.println(blast_output.getBlastOutput_program());
		System.out.println(blast_output.getBlastOutput_query_def());
		System.out.println(blast_output.getBlastOutput_query_ID());
		System.out.println(blast_output.getBlastOutput_query_len());
		System.out.println(blast_output.getBlastOutput_reference());
		System.out.println(blast_output.getBlastOutput_version());
		
		for(Iteration i: blast_output.getBlastOutput_iterations()){
			System.out.println("\t" + i.getIteration_iter_num());
			System.out.println("\t" + i.getIteration_query_def());
			System.out.println("\t" + i.getIteration_query_ID());
			System.out.println("\t" + i.getIteration_query_len());
			
			for(Hit h: i.getIteration_hits()){
				System.out.println("\t\t" + h.getHit_accession());
				System.out.println("\t\t" + h.getHit_def());
				System.out.println("\t\t" + h.getHit_id());
				System.out.println("\t\t" + h.getHit_len());
				System.out.println("\t\t" + h.getHit_num());
			
				for(Hsp p: h.getHit_hsps()){
					System.out.println("\t\t\t" + p.getHsp_align_len());
					System.out.println("\t\t\t" + p.getHsp_bit_score());
					System.out.println("\t\t\t" + p.getHsp_evalue());
					System.out.println("\t\t\t" + p.getHsp_gaps());
					System.out.println("\t\t\t" + p.getHsp_hit_frame());
					System.out.println("\t\t\t" + p.getHsp_hit_from());
					System.out.println("\t\t\t" + p.getHsp_hit_to());
					System.out.println("\t\t\t" + p.getHsp_hseq());
					System.out.println("\t\t\t" + p.getHsp_identity());
					System.out.println("\t\t\t" + p.getHsp_midline());
					System.out.println("\t\t\t" + p.getHsp_num());
					System.out.println("\t\t\t" + p.getHsp_positive());
					System.out.println("\t\t\t" + p.getHsp_qseq());
					System.out.println("\t\t\t" + p.getHsp_query_frame());
					System.out.println("\t\t\t" + p.getHsp_query_from());
					System.out.println("\t\t\t" + p.getHsp_query_to());
					System.out.println("\t\t\t" + p.getHsp_score());
					
				}
			}
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_db_len());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_db_num());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_eff_space());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_entropy());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_hsp_len());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_kappa());
			System.out.println("\t" + i.getIteration_stat().getStatistics().getStatistics_lambda());
		}
		
	}
	
}
