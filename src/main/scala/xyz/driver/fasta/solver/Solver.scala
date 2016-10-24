package xyz.driver.fasta.solver

import scala.collection.mutable.{ArrayBuffer, Set}
import xyz.driver.fasta.model._

object Solver {
  def fuse(a: ArrayBuffer[Nucleotide.Value], b: ArrayBuffer[Nucleotide.Value]): Option[ArrayBuffer[Nucleotide.Value]] = {
    val shorterLength = math.min(a.length, b.length)
    val half: Int = shorterLength / 2
    (half to shorterLength).find { n =>
      a.takeRight(n) == b.take(n)
    }.map { n =>
      a.take(a.length - n) ++ b
    }
  }

  def fuseWithReverse(reverse: Boolean)(a: ArrayBuffer[Nucleotide.Value], b: ArrayBuffer[Nucleotide.Value]): Option[ArrayBuffer[Nucleotide.Value]] = {
    if (reverse) {
      Solver.fuse(b, a)
    } else {
      Solver.fuse(a, b)
    }
  }

  def solveSequence(sequences: Seq[NucleotideSequence]): Option[Seq[Nucleotide.Value]] = {
    var result: ArrayBuffer[Nucleotide.Value] = sequences.head.nucleotides.to[ArrayBuffer]
    val remainingSequences = sequences.tail.map(_.nucleotides.to[ArrayBuffer]).to[Set]

    var reachedEnd: Boolean = false
    while (!remainingSequences.isEmpty) {
      val newResult: Option[(ArrayBuffer[Nucleotide.Value], ArrayBuffer[Nucleotide.Value])] = remainingSequences.toStream
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
