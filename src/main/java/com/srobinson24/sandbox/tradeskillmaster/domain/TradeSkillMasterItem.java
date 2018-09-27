package com.srobinson24.sandbox.tradeskillmaster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by srobinso on 3/24/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming
public class TradeSkillMasterItem implements Serializable {

    @JsonProperty("Id")
    private int id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("MinBuyout")
    private long rawMinBuyout;
    private LocalDateTime lastUpdate;
    @JsonProperty("NumAuctions")
    private int numberOfAuctions;
    @JsonProperty("MarketValue")
    private long rawMarketValue;
    //todo: need to remove the below line!!!!
    private CraftingType craftingType = CraftingType.ENCHANTING;

    public TradeSkillMasterItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TradeSkillMasterItem(int id) {
        this.id = id;
    }

    public TradeSkillMasterItem(String name) {
        this.name = name;
    }

    public TradeSkillMasterItem(String name, int id,long rawMinBuyout) {
        this.name = name;
        this.id = id;
        this.rawMinBuyout = rawMinBuyout;
    }

    public TradeSkillMasterItem() {
    }

    public long getRawMarketValue() {
        return rawMarketValue;
    }

    public void setRawMarketValue(long rawMarketValue) {
        this.rawMarketValue = rawMarketValue;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getNumberOfAuctions() {
        return numberOfAuctions;
    }

    public void setNumberOfAuctions(int numberOfAuctions) {
        this.numberOfAuctions = numberOfAuctions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRawMinBuyout() {
        return rawMinBuyout;
    }

    public void setRawMinBuyout(long rawMinBuyout) {
        this.rawMinBuyout = rawMinBuyout;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public void setCraftingType (CraftingType craftingType) {
        this.craftingType = craftingType;
    }

    @Override
    public String toString() {
        return "TradeSkillMasterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rawMinBuyout=" + rawMinBuyout +
                ", lastUpdate=" + lastUpdate +
                ", numberOfAuctions=" + numberOfAuctions +
                ", rawMarketValue=" + rawMarketValue +
                ", craftingType=" + craftingType +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeSkillMasterItem)) return false;
        TradeSkillMasterItem that = (TradeSkillMasterItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
