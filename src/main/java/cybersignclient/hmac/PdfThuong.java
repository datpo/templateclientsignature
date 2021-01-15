package cybersignclient.hmac;

import java.io.Serializable;

class PdfThuong{
	String base64pdf;
	String hashalg;
	Integer typesignature;
	String signaturename;
	String base64image;
	String textout;
	Integer pagesign;
	Integer xpoint;
	Integer ypoint;
	Integer width;
	Integer height;
	
	public PdfThuong() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getBase64pdf() {
		return base64pdf;
	}

	public void setBase64pdf(String base64pdf) {
		this.base64pdf = base64pdf;
	}

	public String getHashalg() {
		return hashalg;
	}

	public void setHashalg(String hashalg) {
		this.hashalg = hashalg;
	}

	public Integer getTypesignature() {
		return typesignature;
	}

	public void setTypesignature(Integer typesignature) {
		this.typesignature = typesignature;
	}

	public String getSignaturename() {
		return signaturename;
	}

	public void setSignaturename(String signaturename) {
		this.signaturename = signaturename;
	}

	public String getBase64image() {
		return base64image;
	}

	public void setBase64image(String base64image) {
		this.base64image = base64image;
	}

	public String getTextout() {
		return textout;
	}

	public void setTextout(String textout) {
		this.textout = textout;
	}

	public Integer getPagesign() {
		return pagesign;
	}

	public void setPagesign(Integer pagesign) {
		this.pagesign = pagesign;
	}

	public Integer getXpoint() {
		return xpoint;
	}

	public void setXpoint(Integer xpoint) {
		this.xpoint = xpoint;
	}

	public Integer getYpoint() {
		return ypoint;
	}

	public void setYpoint(Integer ypoint) {
		this.ypoint = ypoint;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "PdfThuong [base64pdf=" + base64pdf + ", hashalg=" + hashalg + ", typesignature=" + typesignature
				+ ", signaturename=" + signaturename + ", base64image=" + base64image + ", textout=" + textout
				+ ", pagesign=" + pagesign + ", xpoint=" + xpoint + ", ypoint=" + ypoint + ", width=" + width
				+ ", height=" + height + "]";
	}
	
	

}