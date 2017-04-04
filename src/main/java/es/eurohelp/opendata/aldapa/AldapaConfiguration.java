/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class AldapaConfiguration extends Configuration {
	
	public AldapaConfiguration(String name, String comment, String author, String iNTERNAL_BASE, String pROJECT, String bASE, String dCAT_CATALOG,
	        String dCAT_DATASET, String dCAT_DISTRIBUTION, String rESOURCE, String pROPERTY) {
		super(name, comment, author);
		INTERNAL_BASE = iNTERNAL_BASE;
		PROJECT = pROJECT;
		BASE = bASE;
		DCAT_CATALOG = dCAT_CATALOG;
		DCAT_DATASET = dCAT_DATASET;
		DCAT_DISTRIBUTION = dCAT_DISTRIBUTION;
		RESOURCE = rESOURCE;
		PROPERTY = pROPERTY;
	}

	/**
	 * @return the iNTERNAL_BASE
	 */
	public String getINTERNAL_BASE() {
		return INTERNAL_BASE;
	}

	/**
	 * Setter method for iNTERNAL_BASE
	 * 
	 * @param iNTERNAL_BASE
	 *            the iNTERNAL_BASE to set (type: {@link String})
	 */
	public void setINTERNAL_BASE(String iNTERNAL_BASE) {
		INTERNAL_BASE = iNTERNAL_BASE;
	}

	/**
	 * @return the pROJECT
	 */
	public String getPROJECT() {
		return PROJECT;
	}

	/**
	 * Setter method for pROJECT
	 * 
	 * @param pROJECT
	 *            the pROJECT to set (type: {@link String})
	 */
	public void setPROJECT(String pROJECT) {
		PROJECT = pROJECT;
	}

	/**
	 * @return the bASE
	 */
	public String getBASE() {
		return BASE;
	}

	/**
	 * Setter method for bASE
	 * 
	 * @param bASE
	 *            the bASE to set (type: {@link String})
	 */
	public void setBASE(String bASE) {
		BASE = bASE;
	}

	/**
	 * @return the dCAT_CATALOG
	 */
	public String getDCAT_CATALOG() {
		return DCAT_CATALOG;
	}

	/**
	 * Setter method for dCAT_CATALOG
	 * 
	 * @param dCAT_CATALOG
	 *            the dCAT_CATALOG to set (type: {@link String})
	 */
	public void setDCAT_CATALOG(String dCAT_CATALOG) {
		DCAT_CATALOG = dCAT_CATALOG;
	}

	/**
	 * @return the dCAT_DATASET
	 */
	public String getDCAT_DATASET() {
		return DCAT_DATASET;
	}

	/**
	 * Setter method for dCAT_DATASET
	 * 
	 * @param dCAT_DATASET
	 *            the dCAT_DATASET to set (type: {@link String})
	 */
	public void setDCAT_DATASET(String dCAT_DATASET) {
		DCAT_DATASET = dCAT_DATASET;
	}

	/**
	 * @return the dCAT_DISTRIBUTION
	 */
	public String getDCAT_DISTRIBUTION() {
		return DCAT_DISTRIBUTION;
	}

	/**
	 * Setter method for dCAT_DISTRIBUTION
	 * 
	 * @param dCAT_DISTRIBUTION
	 *            the dCAT_DISTRIBUTION to set (type: {@link String})
	 */
	public void setDCAT_DISTRIBUTION(String dCAT_DISTRIBUTION) {
		DCAT_DISTRIBUTION = dCAT_DISTRIBUTION;
	}

	/**
	 * @return the rESOURCE
	 */
	public String getRESOURCE() {
		return RESOURCE;
	}

	/**
	 * Setter method for rESOURCE
	 * 
	 * @param rESOURCE
	 *            the rESOURCE to set (type: {@link String})
	 */
	public void setRESOURCE(String rESOURCE) {
		RESOURCE = rESOURCE;
	}

	/**
	 * @return the pROPERTY
	 */
	public String getPROPERTY() {
		return PROPERTY;
	}

	/**
	 * Setter method for pROPERTY
	 * 
	 * @param pROPERTY
	 *            the pROPERTY to set (type: {@link String})
	 */
	public void setPROPERTY(String pROPERTY) {
		PROPERTY = pROPERTY;
	}

	private String INTERNAL_BASE;
	private String PROJECT;
	private String BASE;
	private String DCAT_CATALOG;
	private String DCAT_DATASET;
	private String DCAT_DISTRIBUTION;
	private String RESOURCE;
	private String PROPERTY;
}
