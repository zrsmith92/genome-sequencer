package xyz.driver.fasta.parser

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import xyz.driver.fasta.model._

object Parser {
  def linePrefixParser(prefix: String)(str: String): Option[String] = str match {
    case s if s.startsWith(prefix) => Some(s.stripPrefix(prefix))
    case _ => None
  }

  object CommentParser {
    private val prefix: String = ";"

    def unapply(str: String): Option[String] = str match {
      case s if s.startsWith(prefix) => Some(s.stripPrefix(prefix))
      case _ => None
    }
  }

  object DescriptionParser {
    private val prefix: String = ">"

    def unapply(str: String): Option[String] = str match {
      case s if s.startsWith(prefix) => Some(s.stripPrefix(prefix))
      case _ => None
    }
  }

  object NucleotideParser {
    def unapply(str: String): Option[Seq[Nucleotide.Value]] = {
      try {
        Some(str.map(x => Nucleotide.withName(x.toString)))
      } catch {
        case _: Exception => None
      }
    }
  }

  def fromFile(file: Source): Option[Seq[NucleotideSequence]] = {
    var sequences: Seq[NucleotideSequence] = Seq()
    var currentDescription: Option[String] = None
    var currentNucleotides: ArrayBuffer[Nucleotide.Value] = ArrayBuffer()

    def addAndResetCurrentNucleotideSequence() {
      if (currentDescription.isDefined && currentNucleotides.length > 0) {
        sequences :+= NucleotideSequence(currentDescription.get, currentNucleotides)
      }
      currentNucleotides = ArrayBuffer()
      currentDescription = None
    }

    for (line <- file.getLines) {
      line match {
        case CommentParser(_) => ()
        case DescriptionParser(desc) => {
          addAndResetCurrentNucleotideSequence()
          currentDescription = Some(desc)
        }
        case NucleotideParser(nucleotides) =>
          currentNucleotides = currentNucleotides ++ nucleotides
        case _ => return None
      }
    }

    addAndResetCurrentNucleotideSequence()

    if (sequences.length > 0) {
      Some(sequences)
    } else {
      None
    }
  }
}
