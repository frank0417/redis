package cn.wiesel.domain;

/**
 * 
 * @ClassName: Region
 * @Description:
 * @author: 吴建
 * @date: 2016年7月19日 上午11:36:39
 *
 */

public class Region {
	private String ID;
	private String TYPE;
	private String PARENT_ID;
	private String NAME;
	private String SHORT;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getPARENT_ID() {
		return PARENT_ID;
	}

	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getSHORT() {
		return SHORT;
	}

	public void setSHORT(String sHORT) {
		SHORT = sHORT;
	}

	@Override
	public String toString() {
		return "Region [ID=" + ID + ", TYPE=" + TYPE + ", PARENT_ID="
				+ PARENT_ID + ", NAME=" + NAME + ", SHORT=" + SHORT + "]";
	}

}
