package ninja.eivind.mpq.builders;

import java.io.IOException;

/**
 * Functional interface for classes that can build an object from a single source.
 * Very useful for doing many different things in a specific sequence.
 */
@FunctionalInterface
public interface Builder<T> {

    /**
     * Builds the object from a pre-supplied source
     *
     * @return An instance of T
     * @throws IOException if the builder fails to construct from the given source
     */
    T build() throws IOException;
}
