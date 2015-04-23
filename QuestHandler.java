package com.rohan.dragonGame;

import java.util.ArrayList;

public class QuestHandler {

	private ArrayList<Quest> questJournal;

	public QuestHandler() {
		questJournal = new ArrayList<Quest>();
	}

	public void addQuest(int inputID) {
		Quest q = new Quest(inputID);
		questJournal.add(q);
	}

}
