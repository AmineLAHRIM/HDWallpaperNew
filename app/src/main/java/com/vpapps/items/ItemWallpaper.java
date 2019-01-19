package com.vpapps.items;

public class ItemWallpaper {

	private String id, CId, CName, image, imageThumb, totalViews, totalRate, averageRate, totalDownloads, tags;

	public ItemWallpaper(String id, String cId, String cName, String image, String imageThumb, String totalViews, String totalRate, String averageRate, String totalDownloads, String tags) {
		this.id = id;
		this.CId = cId;
		this.CName = cName;
		this.image = image;
		this.imageThumb = imageThumb;
		this.totalViews = totalViews;
		this.totalRate = totalRate;
		this.averageRate = averageRate;
		this.totalDownloads = totalDownloads;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public String getCId() {
		return CId;
	}

	public String getCName() {
		return CName;
	}

	public String getImage() {
		return image;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public String getTotalViews() {
		return totalViews;
	}

	public String getTotalRate() {
		return totalRate;
	}

	public String getAverageRate() {
		return averageRate;
	}

	public void setTotalViews(String totalViews) {
		this.totalViews = totalViews;
	}

	public void setTotalRate(String totalRate) {
		this.totalRate = totalRate;
	}

	public void setAverageRate(String averageRate) {
		this.averageRate = averageRate;
	}

	public String getTotalDownloads() {
		return totalDownloads;
	}

	public void setTotalDownloads(String totalDownloads) {
		this.totalDownloads = totalDownloads;
	}

	public String getTags() {
		return tags;
	}
}
