package xyz.driver.fasta.Solver

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import org.scalatest._
import xyz.driver.fasta.model._
import xyz.driver.fasta.model.Nucleotide._
import xyz.driver.fasta.parser.Parser
import xyz.driver.fasta.solver.Solver

class SolverSpec extends FlatSpec with Matchers {
  "The fuser" should "find the longest common subsequence of two nucleotide sequences" in {
    val a = ArrayBuffer(A, C, G, T, T, G, C, A, C, T, G)
    val b = ArrayBuffer(G, C, A, C, T, G, A, C, C, T, G)

    val result = Solver.fuse(a, b)
    result should be (Some(ArrayBuffer(A, C, G, T, T, G, C, A, C, T, G, A, C, C, T, G)))
  }

  it should "return none if no common sequence >= half the length is found" in {
    val a = ArrayBuffer(A, C, G, T, T, G, C, A, C, T, G)
    val b = ArrayBuffer(G, C, A, C, T, T, A, C, C, T, G)

    Solver.fuse(a, b) should be (None)
  }

  it should "fuse together two sequences where the first is considerably longer than the second" in {
    val a = ArrayBuffer(T, G, C, A, A, T, T, T, A, C, G, T, T, G, C, A, C, T, G)
    val b = ArrayBuffer(G, C, A, C, T, G, A, C, C, T, G)

    val result = Solver.fuse(a, b)
    result should be (Some(ArrayBuffer(T, G, C, A, A, T, T, T, A, C, G, T, T, G, C, A, C, T, G, A, C, C, T, G)))
  }

  it should "fuse together two sequences where the second is considerably longer than the first" in {
    val a = ArrayBuffer(A, C, G, T, T, G, C, A, C, T, G)
    val b = ArrayBuffer(G, C, A, C, T, G, A, C, C, T, G, A, A, C, C, G, G, T, T)

    val result = Solver.fuse(a, b)
    result should be (Some(ArrayBuffer(A, C, G, T, T, G, C, A, C, T, G, A, C, C, T, G, A, A, C, C, G, G, T, T)))
  }

  "The solver" should "solve a simple list of sequences" in {
    val a = NucleotideSequence("Sequence 1", Seq(A, C, G, T))
    val b = NucleotideSequence("Sequence 2", Seq(G, T, A, A))

    Solver.solveSequence(Seq(a, b)) should be (Some(ArrayBuffer(A, C, G, T, A, A)))
  }

  it should "solve a simple list out of order" in {
    val a = NucleotideSequence("Sequence 1", Seq(A, C, G, T))
    val b = NucleotideSequence("Sequence 2", Seq(G, T, A, A))

    Solver.solveSequence(Seq(b, a)) should be (Some(ArrayBuffer(A, C, G, T, A, A)))
  }

  it should "solve a longer list of out-of-order sequences" in {
    val a = NucleotideSequence("Sequence 1", Seq(A, C, G, T, A, C, T, G))
    val b = NucleotideSequence("Sequence 2", Seq(A, C, T, G, T, A, G, G))
    val c = NucleotideSequence("Sequence 3", Seq(T, A, G, G, A, C, A, C))
    val d = NucleotideSequence("Sequence 4", Seq(A, C, A, C, G, T, G, T))

    Solver.solveSequence(Seq(b, c, a, d)) should be (Some(ArrayBuffer(
      A, C, G, T, A, C, T, G, T, A, G, G, A, C, A, C, G, T, G, T
    )))
  }

  it should "solve the original test case provided by Driver" in {
    val source = Source.fromString(""">Frag_56
ATTAGACCTG
>Frag_57
CCTGCCGGAA
>Frag_58
AGACCTGCCG
>Frag_59
GCCGGAATAC
""")
    val sequence = Parser.fromFile(source).get
    Solver.solveSequence(sequence) should be (Some(Seq(A,T,T,A,G,A,C,C,T,G,C,C,G,G,A,A,T,A,C)))
  }
}
