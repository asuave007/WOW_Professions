package com.srobinson24.sandbox.tradeskillmaster.domain;

import java.util.HashMap;
import java.util.Map;

public class InscriptionPigment extends CraftableItem {

    private boolean cheaperToCraft;
    private TradeSkillMasterItem cheapestHerb;
    private Map<TradeSkillMasterItem, Double> herbMillingRateMap = new HashMap<>();

    public InscriptionPigment (String name) {
        super(name);
    }

    public InscriptionPigment () {
        super();
    }

    public boolean isCheaperToCraft() {
        return cheaperToCraft;
    }

    public void setCheaperToCraft(boolean cheaperToCraft) {
        this.cheaperToCraft = cheaperToCraft;
    }

    public TradeSkillMasterItem getCheapestHerb() {
        return cheapestHerb;
    }

    public void setCheapestHerb(TradeSkillMasterItem cheapestHerb) {
        this.cheapestHerb = cheapestHerb;
    }

    public Map<TradeSkillMasterItem, Double> getHerbMillingRateMap() {
        return herbMillingRateMap;
    }

    public void setHerbMillingRateMap(Map<TradeSkillMasterItem, Double> herbMillingRateMap) {
        this.herbMillingRateMap = herbMillingRateMap;
    }

    public void addHerbMillingRate (TradeSkillMasterItem tradeSkillMasterItem, Double rate) {
        this.herbMillingRateMap.put(tradeSkillMasterItem, rate);
    }
}
