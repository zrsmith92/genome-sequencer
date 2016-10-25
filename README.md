# Genome Sequence Reconstructor

This project aims to reconstruct a genome sequence from a series of fragments that have been split up for processing in parallel.

Two contiguous fragments must overlap by >=50% of their length.

## Implementation

The approach taken here is to begin by starting with one of the fragments, and iterating through the remaining set of fragments to find one which can be appended to the current result. If one is found, it is added to the result sequence, and removed from the set of remaining fragments.

If one is not found, then we know we have reached the end of the sequence, and should begin searching for fragments to be prepended to the beginning of the result sequence, instead of appended. If we cannot find a sequence to be prepended, then we know that we have an invalid set of fragments that cannot be constructed into one contiguous fragment.

## Minor optimization

By computing a locality-sensitive hash of each fragment, and sorting the list of fragments based on that, we can hopefully get sequences close to their neighbors in the list before iterating through the remaining fragments.

## Running

Any recent version of Scala and sbt should be fine to run this. The program can be run with the following:

```bash
$ sbt "run /path/to/fasta_file.txt"
```
## Testing

To run the unit tests for the solver and parser, run:

```bash
$ sbt test
```
