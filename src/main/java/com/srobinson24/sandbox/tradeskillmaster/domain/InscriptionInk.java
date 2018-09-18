package com.srobinson24.sandbox.tradeskillmaster.domain;

public class InscriptionInk extends CraftableItem {

    private boolean cheaperToCraft;

    public InscriptionInk(String name) {
        super(name);
    }

    public InscriptionInk() {
    }

    public boolean isCheaperToCraft() {
        return cheaperToCraft;
    }

    public void setCheaperToCraft(boolean cheaperToCraft) {
        this.cheaperToCraft = cheaperToCraft;
    }

}
