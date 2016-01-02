package com.cloudcraftnetwork.api.plugin.spigot.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * PublicPlayerInfo holds all saved player data across the CloudCraftNetwork.
 * <p>
 * PublicPlayerInfo JSONs are stored in the "com.cloudcraftnetwork.players" S3 bucket
 * The key for each player info is the player's UUID.
 */
@JsonSerialize
public class PublicPlayerInfo extends CloudCraftNetworkInfo {

    /**
     * The id of the player (This is the player's Minecraft UUID)
     */
    private final UUID id;

    /**
     * The UUIDs of all the player's friends
     */
    private Set<UUID> friendsList;

    /**
     * The UUIDs of all the other players which have been ignored by the player
     */
    private Set<UUID> ignoredList;

    /**
     * The UUID of the player's pet, if the player owns a pet
     */
    private UUID petId;

    /**
     * The UUID of the player's costume, if the player is wearing one
     */
    private UUID costumeId;

    /**
     * The special items the player is currently holding
     */
    private Map<UUID, Integer> inventory;

    public PublicPlayerInfo(UUID id) {
        this.id = id;
    }

    @JsonCreator
    public PublicPlayerInfo(
            @JsonProperty(value = "dewPoints") UUID id,
            @JsonProperty(value = "dewPoints") long dewPoints,
            @JsonProperty(value = "frostPoints") double frostPoints,
            @JsonProperty(value = "friendsList") Set<UUID> friendsList,
            @JsonProperty(value = "ignoredList") Set<UUID> ignoredList,
            @JsonProperty(value = "petId") UUID petId,
            @JsonProperty(value = "costumeId") UUID costumeId,
            @JsonProperty(value = "inventory") Map<UUID, Integer> inventory
    ) {
        this(id);
        this.friendsList = friendsList;
        this.ignoredList = ignoredList;
        this.petId = petId;
        this.costumeId = costumeId;
        this.inventory = inventory;
    }

    @Override
    public String createKey() {
        return id.toString();
    }

    public UUID getId() {
        return id;
    }

    public Set<UUID> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(Set<UUID> friendsList) {
        this.friendsList = friendsList;
    }

    public Set<UUID> getIgnoredList() {
        return ignoredList;
    }

    public void setIgnoredList(Set<UUID> ignoredList) {
        this.ignoredList = ignoredList;
    }

    public UUID getPetId() {
        return petId;
    }

    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    public UUID getCostumeId() {
        return costumeId;
    }

    public void setCostumeId(UUID costumeId) {
        this.costumeId = costumeId;
    }

    public Map<UUID, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<UUID, Integer> inventory) {
        this.inventory = inventory;
    }
}