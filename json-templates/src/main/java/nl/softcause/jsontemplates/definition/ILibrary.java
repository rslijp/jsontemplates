package nl.softcause.jsontemplates.definition;

import java.util.Optional;

public interface ILibrary {
    Optional<Class> getNodeClass(String name);
}
