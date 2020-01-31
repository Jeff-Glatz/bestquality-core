package io.bestquality.net;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static io.bestquality.net.MaskingClassLoader.maskingClasses;
import static io.bestquality.net.MaskingClassLoader.maskingResources;
import static org.assertj.core.api.Assertions.assertThat;

public class MaskingClassLoaderTest {

    @Test(expected = ClassNotFoundException.class)
    public void shouldRaiseClassNotFoundExceptionWhenClassIsMasked()
            throws ClassNotFoundException {
        MaskingClassLoader loader = maskingClasses(String.class);
        loader.loadClass(String.class.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void shouldRaiseClassNotFoundExceptionWhenPackageIsMasked()
            throws ClassNotFoundException {
        MaskingClassLoader loader = MaskingClassLoader.maskingClasses("java.lang.*");
        loader.loadClass(String.class.getName());
    }

    @Test
    public void shouldReturnEmptyEnumerationWhenResourceIsMasked()
            throws IOException {
        MaskingClassLoader loader = maskingResources("META-INF/.*");

        Enumeration<URL> resources = loader.getResources("META-INF/MANIFEST.MF");
        assertThat(resources.hasMoreElements())
                .isFalse();
    }

    @Test
    public void shouldReturnNullResourceWhenResourceIsMasked() {
        MaskingClassLoader loader = maskingResources("META-INF/.*");

        assertThat(loader.getResource("META-INF/MANIFEST.MF"))
                .isNull();
    }

    @Test
    public void shouldReturnNullResourceStreamWhenResourceIsMasked() {
        MaskingClassLoader loader = maskingResources("META-INF/.*");

        assertThat(loader.getResourceAsStream("META-INF/MANIFEST.MF"))
                .isNull();
    }

    @Test
    public void shouldDelegateWhenClassIsNotMasked()
            throws ClassNotFoundException {
        MaskingClassLoader loader = maskingClasses(Boolean.class);

        assertThat(loader.loadClass(String.class.getName()))
                .isSameAs(String.class);
    }

    @Test
    public void shouldReturnEnumerationWhenResourceIsNotMasked()
            throws IOException {
        MaskingClassLoader loader = maskingResources("foo");

        Enumeration<URL> resources = loader.getResources("META-INF/MANIFEST.MF");
        assertThat(resources.hasMoreElements())
                .isTrue();
    }

    @Test
    public void shouldReturnResourceWhenResourceIsNotMasked() {
        MaskingClassLoader loader = maskingResources("foo");

        assertThat(loader.getResource("META-INF/MANIFEST.MF"))
                .isNotNull();
    }

    @Test
    public void shouldReturnResourceStreamWhenResourceIsNotMasked() {
        MaskingClassLoader loader = maskingResources("foo");

        assertThat(loader.getResourceAsStream("META-INF/MANIFEST.MF"))
                .isNotNull();
    }
}
