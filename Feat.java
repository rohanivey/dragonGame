package com.rohan.dragonGame;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Feat {

	private String name;
	private String description;
	private String id;
	private Boolean hasRequired;
	private XmlReader reader;
	private int minStr, minWis, minInt, minAgi, minVit = 0;
	private int maxStr, maxWis, maxInt, maxAgi, maxVit = 9999;
	private int changeStr, changeWis, changeInt, changeAgi, changeVit = 0;
	private ArrayList<String> featRequirements = new ArrayList<String>();
	private ArrayList<String> traitRequirements = new ArrayList<String>();
	private ArrayList<String> itemRequirements = new ArrayList<String>();
	private ArrayList<String> questRequirements = new ArrayList<String>();
	private Player player;
	private String img;

	public Feat(String inputID) {
		id = inputID;
		reader = new XmlReader();
		try {
			Element root = reader.parse(Gdx.files.internal("featList.xml"));
			Array<Element> featList = root.getChildrenByName("Feat");
			Element activeElement;
			for (Element e : featList) {
				if (e.getChildByName("ID").equals(id)) {
					featSetup(e);
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public Feat(Player inputPlayer) { player = inputPlayer; reader = new
	 * XmlReader(); try { Element root =
	 * reader.parse(Gdx.files.internal("featList.xml")); Array<Element> featList
	 * = root.getChildrenByName("Feat"); for (Element e : featList) {
	 * featSetup(e); break; } } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } }
	 */

	public Feat(Element inputElement, Player inputPlayer) {
		player = inputPlayer;
		featSetup(inputElement);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void featSetup(Element inputElement) {
		Element activeElement = inputElement;
		this.name = activeElement.get("Name");
		this.description = activeElement.get("Description");
		img = activeElement.get("Texture");
		Element requirements = activeElement.getChildByName("Requirements");
		Array<Element> minStats = requirements.getChildrenByName("MinStats");
		for (Element e : minStats) {
			minStr = e.getInt("Strength");
			minWis = e.getInt("Wisdom");
			minInt = e.getInt("Intelligence");
			minAgi = e.getInt("Agility");
			minVit = e.getInt("Vitality");
		}

		Array<Element> maxStats = requirements.getChildrenByName("MaxStats");
		for (Element e : maxStats) {
			maxStr = e.getInt("Strength");
			maxWis = e.getInt("Wisdom");
			maxInt = e.getInt("Intelligence");
			maxAgi = e.getInt("Agility");
			maxVit = e.getInt("Vitality");
		}

		Array<Element> tempFeatRequirements = requirements
				.getChildrenByName("Feats");
		for (Element e : tempFeatRequirements) {
			featRequirements.add(e.get("ID"));
		}

		Array<Element> tempItemRequirements = requirements
				.getChildrenByName(("Items"));
		for (Element e : tempItemRequirements) {
			this.itemRequirements.add(e.get("ID"));
		}

		Array<Element> tempQuestRequirements = requirements
				.getChildrenByName(("Quests"));
		for (Element e : tempQuestRequirements) {
			this.questRequirements.add(e.get("ID"));
		}

		Array<Element> statModifiers = activeElement.getChildByName("Effects")
				.getChildrenByName("StatModify");
		for (Element e : statModifiers) {
			changeStr = e.getInt("Strength");
			changeWis = e.getInt("Wisdom");
			changeInt = e.getInt("Intelligence");
			changeAgi = e.getInt("Agility");
			changeVit = e.getInt("Vitality");
		}

	}

	public Boolean statCheck() {
		int tempStr = player.getStats("Strength");
		int tempWis = player.getStats("Wisdom");
		int tempInt = player.getStats("Intelligence");
		int tempAgi = player.getStats("Agility");
		int tempVit = player.getStats("Vitality");
		if (tempStr < minStr && tempStr > maxStr) {
			return false;
		} else if (tempWis < minWis && tempWis > maxWis) {
			return false;
		} else if (tempInt < minInt && tempInt > maxInt) {
			return false;
		} else if (tempAgi < minAgi && tempAgi > maxAgi) {
			return false;
		} else if (tempVit < minVit && tempVit > maxVit) {
			return false;
		}

		return true;
	}

	public Boolean questCheck() {
		return true;
	}

	public String getImg() {
		return img;
	}

	public Boolean itemCheck() {
		return true;
	}

	public Boolean featCheck() {
		return true;
	}

	public Boolean checkRequirements() {
		// If player has blah blah blah, then do this.
		if (statCheck() && questCheck() && itemCheck() && featCheck()) {
			return true;
		}
		return false;
	}

	public void applyModifiers() {
		player.setStats("Strength", changeStr);
		player.setStats("Wisdom", changeWis);
		player.setStats("Intel", changeInt);
		player.setStats("Agility", changeAgi);
		player.setStats("Vitality", changeVit);
	}

}
