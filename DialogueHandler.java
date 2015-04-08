package com.rohan.dragonGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
	private XmlReader reader;
	private String NPCName;
	private String[][] playerKnowledgeCopy;
	private Element root, NPC;
	private Array<Element> options;
	
	private Boolean chatting = false;
	
	private Boolean responding = false;
	private String currentResponse;
	
	
	DialogueHandler(String inputNPC)
	{
		talkingPoints = new ArrayList<String>();
		img = new Texture(Gdx.files.internal("pointer.png"));
		sprite = new Sprite(img);
		NPCName = inputNPC;
		reader = new XmlReader();
		
		//May need to set to 1,1
		playerKnowledgeCopy = new String[0][0];
		

		//createDialogue();

	}
	
	public String selectDialogue(int inputInt)
	{
		System.out.println("Trying to select Dialogue in DH");
		if(inputInt == talkingPoints.size()-1)
		{
			chatting = false;
		}
		for(Element child: options)
		{
			if(talkingPoints.get(inputInt).equals(child.getAttribute("text")))
			{
	
				for(Element c: options)
				{
					if(child.getAttribute("response").equals(c.getAttribute("id")))
					{
						currentResponse = c.getAttribute("text");
						responding = true;
					}
				}
				
				System.out.println("The returned id is " + child.getAttribute("id"));
				return child.getAttribute("id");
			}
		}
		System.out.println("Couldn't find a talking point string match");
		return null;
	}
	
	public String continueDialogue()
	{
		//Temporary fill statement here. Should be replaced
		Element currentChild = options.get(0);
		
		for(Element child: options)
		{
			if(child.getAttribute("text").equals(currentResponse))
			{
				//Should probably make current child a new instance of an Element with all the parts of child, for sec sake.
				currentChild = child;
				responding = false;
			}
		}
		for(Element c : options)
		{
			if(currentChild.getAttribute("response").equals(c.getAttribute("id")))
			{
				currentResponse = c.getAttribute("text");
				responding  = true;
				return currentResponse;
			}
		}		
		return currentResponse;
	}
	
	public void setChatting(Boolean inputBool){chatting = inputBool;}
	
	public Boolean getChatting(){return chatting;}
	
	public void setResponding(Boolean inputBool){responding = inputBool;}
	
	public Boolean getResponding(){return responding;}
	
	public String getResponse(){return currentResponse;}
	
	public String produceDialogue(int inputInt){return talkingPoints.get(inputInt);}
	
	public ArrayList<String> getTalkingPoints()
	{
		//System.out.println("There are this many talking Points: " + talkingPoints.size());
		return talkingPoints;
	}
	
	public int getTalkingPointsSize()
	{
		return talkingPoints.size();
	}
	
	public Sprite getTexture()
	{
		return sprite;
	}
	
	public void setPlayerKnowledgeCopy(String[][] inputStringArray)
	{
		System.out.println("I'm refreshing this entity's knowledge of the player!");
		playerKnowledgeCopy = new String[inputStringArray.length][inputStringArray[0].length];
		for(int i = 0; i < inputStringArray.length; i++)
		{
			for(int j = 0; j < inputStringArray[i].length; j++)
			{
				playerKnowledgeCopy[i][j] = inputStringArray[i][j];
			}
		}
	}
	
	public void createDialogue()
	{
		getChatOptions();
		//Adds each of those talking points to an easily presentable ArrayList in string format
		// Loop through each option child in Options
		System.out.println("Adding talking points!");
		for(Element child : options)
		{	
			if(checkCharacterKnowledge(NPCName, child.getAttribute("requirement")) && !checkCharacterKnowledge(NPCName, child.getAttribute("id")))
			{
				if(child.getAttribute("saidBy").equals("Player"))
				talkingPoints.add(child.getAttribute("text"));
				//System.out.println("Talking point added!");
			}
		}
		talkingPoints.add("Goodbye");
		//When calling for the chat option, can also make a check for deletion of that particular option
		//in the arraylist
	}
	
	public void getChatOptions()
	{
		System.out.println("Getting ChatOptions()!");
		talkingPoints = new ArrayList<String>();
		//This gets you the list of all NPCs
		try {
			root = reader.parse(Gdx.files.internal("DialogueList.xml"));
		} catch (IOException e) {
			System.out.println("AIN'T NO FILE THERE MOHONKA!");			
			e.printStackTrace();
		}
		//This checks for the npc named in the file list compared to the inputNPC name given at entity creation
		NPC = root.getChildByName(NPCName);
		
		//Making an arraylist of elements based on the options of dialogue
		//You have to use an Array here. ArrayList hasn't been overridden in the LIBGDX framework like Array has
		options = NPC.getChildByName("options").getChildrenByName("option");
	}
	
	public boolean checkCharacterKnowledge(String inputNPC, String inputID)
	{
		//System.out.println("Checking character knowledge for " + inputNPC);
		for(int i = 0; i < playerKnowledgeCopy.length; i++)
		{
			if(playerKnowledgeCopy[i][0] == inputNPC)
			{
				for(int j = 0; j < playerKnowledgeCopy[i].length; j++)
				{
					
					//System.out.println("I know this NPC! Do I know what I need to about option " + j + " for NPC " + playerKnowledgeCopy[i][0] + "?");
					if(inputID.equals("none"))
					{
						//System.out.println("No knowledge Needed for " + inputID);
						return true;
					}
					else if(playerKnowledgeCopy[i][j] == null)
					{
						//System.out.println("No knowledge at " + j);
						return false;
					}
					else if(playerKnowledgeCopy[i][j].equals(inputID))
					{
						System.out.println("I know of " + inputID);
						return true;
					}
					else
					{
						System.out.println("I don't know about " + inputID);
						//System.out.println("The inner for loop for checking player knowledge is on iteration: " + j);
					}
				}
				
			}
		}
		return false;
	}	
	
}
