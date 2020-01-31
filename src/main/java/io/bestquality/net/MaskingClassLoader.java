package io.bestquality.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.function.Predicate;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyEnumeration;

public class MaskingClassLoader
        extends URLClassLoader {
    private final Predicate<String> type;
    private final Predicate<String> resource;

    public MaskingClassLoader(Predicate<String> type, Predicate<String> resource) {
        super(new URL[0], MaskingClassLoader.class.getClassLoader());
        this.type = type;
        this.resource = resource;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        if (type.test(name)) {
            throw new ClassNotFoundException();
        }
        return super.loadClass(name, resolve);
    }

    @Override
    public URL getResource(String name) {
        if (resource.test(name)) {
            return null;
        }
        return super.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name)
            throws IOException {
        if (resource.test(name)) {
            return emptyEnumeration();
        }
        return super.getResources(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (resource.test(name)) {
            return null;
        }
        return super.getResourceAsStream(name);
    }

    public static MaskingClassLoader masking(String[] classes, String[] resources) {
        return new MaskingClassLoader(
                candidate -> stream(classes).anyMatch(candidate::matches),
                candidate -> stream(resources).anyMatch(candidate::matches));
    }

    public static MaskingClassLoader masking(Class<?>[] classes, String[] resources) {
        return masking(
                stream(classes)
                        .map(Class::getName)
                        .toArray(String[]::new),
                resources);
    }

    public static MaskingClassLoader maskingClasses(String... patterns) {
        return masking(patterns, new String[0]);
    }

    public static MaskingClassLoader maskingClasses(Class<?>... classes) {
        return masking(classes, new String[0]);
    }

    public static MaskingClassLoader maskingResources(String... patterns) {
        return masking(new String[0], patterns);
    }
}
