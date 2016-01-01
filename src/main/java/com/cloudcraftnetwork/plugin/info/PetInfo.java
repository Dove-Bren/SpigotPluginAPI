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

import java.util.UUID;

public class PetInfo extends CloudCraftNetworkInfo {

    /**
     * The id of the pet.
     */
    private final UUID id;

    /**
     * The type of animal the pet is
     */
    private final String type;

    /**
     * The name of the pet
     */
    private String name;

    /**
     * The pet's birthday (time since epoch)
     */
    private long birthday;

    /**
     * The personality type of the pet
     */
    private String personality;

    public PetInfo(UUID id, String type) {
        this.id = id;
        this.type = type;
    }

    @JsonCreator
    public PetInfo(
            @JsonProperty(value = "type") UUID id,
            @JsonProperty(value = "type") String type,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "birthday") long birthday,
            @JsonProperty(value = "personality") String personality
    ) {
        this(id, type);
        this.name = name;
        this.birthday = birthday;
        this.personality = personality;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }
}
