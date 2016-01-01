package com.cloudcraftnetwork.plugin.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

@JsonSerialize
public class ServerInfo extends CloudCraftNetworkInfo {

    /**
     * The id of the server.
     */
    private final UUID id;

    /**
     * The type of the server.
     */
    private final String type;

    /**
     * The ip address or hostname of the server.
     */
    private String address;

    public ServerInfo(UUID id, String type) {
        this.id = id;
        this.type = type;
    }

    @JsonCreator
    public ServerInfo(
            @JsonProperty(value = "id") UUID id,
            @JsonProperty(value = "type") String type,
            @JsonProperty(value = "address") String address
    ) {
        this(id, type);
        this.address = address;
    }

    @Override
    public String createKey() {
        return type + "/" + id;
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
