/*
 * This file is part of the CloudCraftNetwork, licensed under the MIT License (MIT).
 *
 * Copyright (c) Matthew Broomfield <m0pt0pmatt17@gmail.com>
 * Copyright (c) cloudcraftnetwork.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cloudcraftnetwork.plugin.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.UUID;

/**
 * PrivatePlayerInfo holds all saved player data across the CloudCraftNetwork which is sensitive.
 * <p>
 * PrivatePlayerInfo JSONs are stored in the "com.cloudcraftnetwork.players" S3 bucket.
 * The key for each player info is the player's UUID.
 */
@JsonSerialize
public class PrivatePlayerInfo extends CloudCraftNetworkInfo {

    /**
     * The id of the player (This is the player's Minecraft UUID)
     */
    private final UUID id;

    /**
     * DewPoints are the in-game currency for the CloudCraftNetwork
     */
    private long dewPoints;

    /**
     * FrostPoints are the currency used for eSports for the CloudCraftNetwork
     */
    private double frostPoints;

    public PrivatePlayerInfo(UUID id) {
        this.id = id;
    }

    @JsonCreator
    public PrivatePlayerInfo(
            @JsonProperty(value = "dewPoints") UUID id,
            @JsonProperty(value = "dewPoints") long dewPoints,
            @JsonProperty(value = "frostPoints") double frostPoints
    ) {
        this(id);
        this.dewPoints = dewPoints;
        this.frostPoints = frostPoints;
    }

    @Override
    public String createKey() {
        return id.toString();
    }

    public UUID getId() {
        return id;
    }

    public long getDewPoints() {
        return dewPoints;
    }

    public void setDewPoints(long dewPoints) {
        this.dewPoints = dewPoints;
    }

    public double getFrostPoints() {
        return frostPoints;
    }

    public void setFrostPoints(double frostPoints) {
        this.frostPoints = frostPoints;
    }
}