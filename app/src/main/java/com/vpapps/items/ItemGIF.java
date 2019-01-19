package com.vpapps.items;

public class ItemGIF {

	private String id, image, views, totalRate, aveargeRate, totalDownload, tags;

	public ItemGIF(String id, String image, String views, String totalRate, String aveargeRate, String totalDownload, String tags) {
		this.id = id;
		this.image = image;
		this.views = views;
		this.tags = tags;
		this.totalRate = totalRate;
		this.aveargeRate = aveargeRate;
		this.totalDownload = totalDownload;
	}

	public String getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public String getTotalViews() {
		return views;
	}

	public void setTotalViews(String views) {
		this.views = views;
	}

	public String getTags() {
		return tags;
	}

	public String getTotalRate() {
		return totalRate;
	}

	public String getAveargeRate() {
		return aveargeRate;
	}

	public void setAveargeRate(String aveargeRate) {
		this.aveargeRate = aveargeRate;
	}

	public String getTotalDownload() {
		return totalDownload;
	}

	public void setTotalDownload(String totalDownload) {
		this.totalDownload = totalDownload;
	}
}
