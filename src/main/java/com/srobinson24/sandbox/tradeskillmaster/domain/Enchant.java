package com.srobinson24.sandbox.tradeskillmaster.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 3/24/2017.
 */
public class Enchant extends TradeSkillMasterItem {

    private int quantityOnhand;
    private final Map<TradeSkillMasterItem, Integer> craftingMaterials =  new HashMap<>(); // item to quantity
    private final CraftingType craftingType = CraftingType.ENCHANT;

    public int getQuantityOnhand() {
        return quantityOnhand;
    }

    public void setQuantityOnhand(int quantityOnhand) {
        this.quantityOnhand = quantityOnhand;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public void addCraftingMaterial (TradeSkillMasterItem craftingMaterial, Integer quantity) {
        craftingMaterials.put(craftingMaterial, quantity);
    }

    public Map<TradeSkillMasterItem, Integer> getCraftingMaterials() {
        return craftingMaterials;
    }
}
