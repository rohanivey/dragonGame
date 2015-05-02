package com.rohan.dragonGame;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class FeatManager {

	private ArrayList<Feat> featList = new ArrayList<Feat>();
	private XmlReader reader;
	private Player player;

	public FeatManager(Player inputPlayer) {
		player = inputPlayer;
		reader = new XmlReader();
		try {
			System.out.println("FeatManager setting up");
			Element root = reader.parse(Gdx.files.internal("featList.xml"));
			Array<Element> tempList = root.getChildrenByName("Feat");
			for (Element e : tempList) {
				Feat tempFeat = new Feat(e, player);
				featList.add(tempFeat);
				System.out.println("Added feat " + tempFeat.getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
