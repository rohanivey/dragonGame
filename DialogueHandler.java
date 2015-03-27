package com.rohan.dragonGame;

import java.util.ArrayList;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class DialogueHandler {
	
	private String npc;
	private ArrayList<String> talkingPoints; 

	
	
	DialogueHandler(String inputNPC)
	{
		npc = inputNPC;
		XmlReader reader = new XmlReader();
		Element root = reader.parse("DialogueList.xml");
		Element NPCList = root.getChildByName("NPCS");
		Element NPC = NPCList.getChildByName(npc);
		Element options = NPC.getChildByName("options");
		ArrayList<Element> dialogueOptions = new ArrayList<Element>(options.getChildCount());
		for(Element option : dialogueOptions)
		{
			talkingPoints.add(option.getChildByName("option").getAttribute("action"));
		}
	}
	
	public void produceDialogue(int inputInt)
	{
		//Say thing at dialogue[i];
	}
	
}
