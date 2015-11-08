package ninja.eivind.mpq.builders;

import java.io.IOException;

/**
 * @author Eivind Vegsundvåg
 */
@FunctionalInterface
public interface Builder<T> {

    T build() throws IOException;
}
