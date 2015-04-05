package com.rohan.dragonGame;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class DialogueHandler {
	
	//private String npc;
	private ArrayList<String> talkingPoints; 
	private Texture img;
	private Sprite sprite;

	
	
	DialogueHandler(String inputNPC)
	{
		talkingPoints = new ArrayList<String>();
		img = new Texture(Gdx.files.internal("pointer.png"));
		sprite = new Sprite(img);
		
		XmlReader reader = new XmlReader();
		try {
			//This gets you the list of all NPCs
			Element root = reader.parse(Gdx.files.internal("DialogueList.xml"));
			//This checks for the npc named in the file list compared to the inputNPC name given at entity creation
			Element NPC = root.getChildByName(inputNPC);
			
			//Making an arraylist of elements based on the options of dialogue
			//You have to use an Array here. ArrayList hasn't been overridden in the LIBGDX framework like Array has
			Array<Element> options = NPC.getChildByName("options").getChildrenByName("option");
			

			//Adds each of those talking points to an easily presentable ArrayList in string format
			// Loop through each option child in Options
			for(Element child : options)
			{
				talkingPoints.add(child.getAttribute("text"));
			}
			//When calling for the chat option, can also make a check for deletion of that particular option
			//in the arraylist
			
			//This is system error checking
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DUN GUUFED THE DIALOGUEHANDLER");
		}
	}
	
	public String produceDialogue(int inputInt)
	{
		return talkingPoints.get(inputInt);
	}
	
	public ArrayList<String> getTalkingPoints()
	{
		return talkingPoints;
	}
	
	public Sprite getTexture()
	{
		return sprite;
	}
	
}
