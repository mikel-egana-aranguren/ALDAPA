package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.jena.util.FileUtils;

import org.junit.Before;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.modification.SILKLinkDiscovery;


public class SilkLinkDiscoveryTest {
	private String configurationFile = "data/silk-configuration.xml";
	private String resultPath = "accepted_links.nt";
	private SILKLinkDiscovery silk;

	@Before
	public void setUp() {
		silk = new SILKLinkDiscovery();
	}

	@Test
	public void testEJIECalidadAireConverter() throws Exception {
		String silkConfFile = FileUtils.readWholeFileAsUTF8(configurationFile);
		assertTrue(silk.discoverLinks(configurationFile, resultPath));
	}
}
