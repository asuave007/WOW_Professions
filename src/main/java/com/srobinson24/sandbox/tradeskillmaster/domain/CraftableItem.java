package com.srobinson24.sandbox.tradeskillmaster.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 3/24/2017.
 */
public class CraftableItem extends TradeSkillMasterItem {

    public CraftableItem(String name) {
        super(name);
    }

    public CraftableItem() {
    }

    private int quantityOnhand;
    private int quantityDesired;
    private final Map<TradeSkillMasterItem, Double> craftingMaterials =  new HashMap<>(); // item to quantity

    public int getQuantityOnhand() {
        return quantityOnhand;
    }

    public void setQuantityOnhand(int quantityOnhand) {
        this.quantityOnhand = quantityOnhand;
    }

    public int getQuantityDesired() {
        return quantityDesired;
    }

    public void setQuantityDesired(int quantityDesired) {
        this.quantityDesired = quantityDesired;
    }

    public void addCraftingMaterial (TradeSkillMasterItem craftingMaterial, Double quantity) {
        craftingMaterials.put(craftingMaterial, quantity);
    }

    public Map<TradeSkillMasterItem, Double> getCraftingMaterials() {
        return craftingMaterials;
    }
}
