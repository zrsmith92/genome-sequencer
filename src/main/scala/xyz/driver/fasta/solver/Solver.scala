package xyz.driver.fasta.solver

import scala.collection.mutable.Set
import xyz.driver.fasta.model._

object Solver {
  def fuse(a: Seq[Nucleotide.Value], b: Seq[Nucleotide.Value]): Option[Seq[Nucleotide.Value]] = {
    val shorterLength = math.min(a.length, b.length)
    val half: Int = shorterLength / 2
    (half to shorterLength).find { n =>
      a.takeRight(n) == b.take(n)
    }.map { n =>
      a.take(a.length - n) ++ b
    }
  }

  def fuseWithReverse(reverse: Boolean)(a: Seq[Nucleotide.Value], b: Seq[Nucleotide.Value]): Option[Seq[Nucleotide.Value]] = {
    if (reverse) {
      Solver.fuse(b, a)
    } else {
      Solver.fuse(a, b)
    }
  }

  def solveSequence(sequences: Seq[NucleotideSequence]): Option[Seq[Nucleotide.Value]] = {
    var result: Seq[Nucleotide.Value] = sequences.head.nucleotides
    val remainingSequences = Set(sequences.tail.map(_.nucleotides): _*)

    var reachedEnd: Boolean = false
    while (!remainingSequences.isEmpty) {
      val newResult: Option[(Seq[Nucleotide.Value], Seq[Nucleotide.Value])] = remainingSequences.toStream
        .map(seq => Solver.fuseWithReverse(reachedEnd)(result, seq).map((seq, _)))
        .dropWhile(_.isEmpty).headOption.flatMap(identity)

      (newResult, reachedEnd) match {
        case (Some((oldSeq, newResult)), _) => {
          result = newResult
          remainingSequences -= oldSeq
        }
        case (None, false) => { reachedEnd = true }
        case (None, true) => { return None }
      }
    }

    Some(result)
  }
}
