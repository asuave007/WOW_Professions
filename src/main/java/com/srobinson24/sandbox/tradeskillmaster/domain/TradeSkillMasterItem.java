package com.srobinson24.sandbox.tradeskillmaster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    public TradeSkillMasterItem(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "TradeSkillMasterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rawMinBuyout=" + rawMinBuyout +
                ", lastUpdate=" + lastUpdate +
                ", numberOfAuctions=" + numberOfAuctions +
                ", rawMarketValue=" + rawMarketValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeSkillMasterItem)) return false;

        TradeSkillMasterItem that = (TradeSkillMasterItem) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
