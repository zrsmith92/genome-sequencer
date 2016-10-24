package xyz.driver.fasta.model

import com.idealista.tlsh.TLSH

case class NucleotideSequence(description: String, nucleotides: Seq[Nucleotide.Value]) {
  lazy val tlsh: String = new TLSH(nucleotides.map(_.toString).mkString).hash()
}
