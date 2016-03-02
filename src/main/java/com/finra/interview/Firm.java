/**
 * 
 */
package com.finra.interview;

/**
 * @author ramana
 *Firm object
 */
public class Firm {

	/**
	 * @param firmId
	 */
	public Firm(int firmId) {
		this.firmId = firmId;
	}

	private int firmId;

	/**
	 * @return the firmId
	 */
	public int getFirmId() {
		return firmId;
	}
//Ideally we should not setFirmId and we should be using Constructors.
//But adding setter based on requirements.	
	/**
	 * @param firmId the firmId to set
	 */
	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Firm [firmId=");
		builder.append(firmId);
		builder.append("]");
		return builder.toString();
	}
	
}
