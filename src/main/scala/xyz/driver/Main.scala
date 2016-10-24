package xyz.driver

import scala.io.{BufferedSource, Source}
import xyz.driver.fasta.model._
import xyz.driver.fasta.parser.Parser
import xyz.driver.fasta.solver.Solver

object Main {
  def main(args: Array[String]) {
    if (args.length != 1 || args(0) == null) {
        printUsageAndExit()
    }

    val filename = args(0)
    val file: BufferedSource = try {
      Source.fromFile(filename)
    } catch {
      case ex: java.io.FileNotFoundException => {
        printFileNotFoundAndExit(filename)
        null
      }
    }

    val parsedNucleotides: Seq[NucleotideSequence] = Parser.fromFile(file) match {
      case Some(nucleotides) => nucleotides
      case None => {
        printParseErrorAndExit()
        null
      }
    }

    Solver.solveSequence(parsedNucleotides) match {
      case Some(solved) => {
        println(solved.map(_.toString).mkString)
      }
      case None => {
        System.err.println("Sequence not solvable")
        System.exit(1)
      }
    }
  }

  def printUsageAndExit() {
    System.err.println("""
USAGE:
  sbt "run /path/to/fasta_file.txt"
""")
    System.exit(1)
  }

  def printFileNotFoundAndExit(filename: String) {
    System.err.println(s"$filename not found")
    System.exit(1)
  }

  def printParseErrorAndExit() {
    System.err.println("Parse error. Please ensure the input file is in valid fasta format")
    System.exit(1)
  }
}
