package com.xinlan.pocketav.model;

public class Actress {
	/**
	 * "create table ACTRESS (ID INTEGER primary key autoincrement," +
	 * "NAME varchar(50),JAPANESE_NAME varchar(50),ENGLISH_NAME varchar(50)," +
	 * "BIRTHDAY varchar(50),BLOOD varchar(10),HEIGHT varchar(20),VITAL varchar(40),"
	 * +
	 * "HOBBY varchar(50),HOMWTOWN varchar(50),PICMIN varchar(30),PICS varchar(100),"
	 * + "DESCRIPT varchar(1000))");
	 */
	private String descript;

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public String getPicmin() {
		return picmin;
	}

	public void setPicmin(String picmin) {
		this.picmin = picmin;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getVital() {
		return vital;
	}

	public void setVital(String vital) {
		this.vital = vital;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJapaneseName() {
		return japaneseName;
	}

	public void setJapaneseName(String japaneseName) {
		this.japaneseName = japaneseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String[] getPicsArray() {
		return picsArray;
	}

	public void setPicsArray(String[] picsArray) {
		this.picsArray = picsArray;
	}

	private String pics;
	private String picmin;
	private String hometown;
	private String hobby;
	private String vital;
	private String height;
	private String blood;
	private Integer id;
	private String name;
	private String japaneseName;
	private String englishName;
	private String birthday;
	private String[] picsArray;

	public String getInfos() {
		StringBuffer sb = new StringBuffer("姓名 : ");
		sb.append(name).append("\n日文名 : ").append(japaneseName)
				.append("\n英文名 : ").append(englishName).append("\n生日 : ")
				.append(birthday).append("\n血型 : ").append(blood)
				.append("\n身高 : ").append(height).append("\n三围 : ").append(vital)
				.append("\n爱好 : ").append(hobby).append("\n家乡 : ").append(hometown).append("\n");
		return sb.toString();
	}
}// end class
