package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.exception.SilkLinksNotDiscoveredException;
import es.eurohelp.lod.aldapa.impl.modification.SILKLinkDiscovery;

public class SilkLinkDiscoveryTest {
    private String configurationFile = "silk-configuration.xml";
    private String resultPath = "accepted-links.nt";
    private SILKLinkDiscovery silk;

    @Before
    public void setUp() {
        silk = new SILKLinkDiscovery();
    }

    @Test
    public void testEJIECalidadAireConverter() throws SilkLinksNotDiscoveredException {
        assertTrue(silk.discoverLinks(configurationFile, resultPath));
    }
}
