package xyz.driver.fasta.parser

import org.scalatest._
import scala.io.Source
import xyz.driver.fasta.model._
import xyz.driver.fasta.model.Nucleotide._

class ParserSpec extends FlatSpec with Matchers {

  "The Parser" should "correctly parse a simple nucleotide sequence" in {
    val file = Source.fromString(""">Sequence 1
ATGC
""")
    Parser.fromFile(file) should be (Some(Seq(
      NucleotideSequence("Sequence 1", Seq(A, T, G, C))
    )))
  }

  it should "correctly parse multiple nucleotide sequences from a single buffer" in {
    val file = Source.fromString(""">Sequence 1
ACGTACGTACGT
TGCATCGTACTC

>Sequence 2
ATGCATGCATGC
GTACTGACTAGC
""")
    Parser.fromFile(file) should be (Some(Seq(
      NucleotideSequence("Sequence 1", Seq(A, C, G, T, A, C, G, T, A, C, G, T, T, G, C, A, T, C, G, T, A, C, T, C)),
      NucleotideSequence("Sequence 2", Seq(A, T, G, C, A, T, G, C, A, T, G, C, G, T, A, C, T, G, A, C, T, A, G, C))
    )))
  }

  it should "allow lines with comments" in {
    val file = Source.fromString(""">Sequence 1
; This line is a comment
ACGT
""")
    Parser.fromFile(file) should be (Some(Seq(
      NucleotideSequence("Sequence 1", Seq(A, C, G, T))
    )))
  }

  it should "return None for any invalid characters" in {
    val file = Source.fromString(""">Sequence 1
ABCT
""")
    Parser.fromFile(file) should be (None)
  }
}
