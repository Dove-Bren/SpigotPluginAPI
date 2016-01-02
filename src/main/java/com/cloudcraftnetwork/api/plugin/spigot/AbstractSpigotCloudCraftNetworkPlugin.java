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

package com.cloudcraftnetwork.api.plugin.spigot;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringInputStream;
import com.cloudcraftnetwork.api.plugin.spigot.info.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractSpigotCloudCraftNetworkPlugin extends JavaPlugin implements AWSCredentials {

    /**
     * Class which holds the names of all the S3 buckets used by the CloudCraftNetwork.
     */
    class Bucket {
        private static final String PREFIX = "com.cloudcraftnetwork.";
        public static final String SERVERS = PREFIX + "servers";
        public static final String PUBLIC_PLAYERS = PREFIX + "players.public";
        public static final String PRIVATE_PLAYERS = PREFIX + "players.private";
        public static final String ITEMS = PREFIX + "items";
        public static final String PETS = PREFIX + "pets";
        public static final String COSTUMES = PREFIX + "costumes";
        public static final String BLACKLIST = PREFIX + "blacklist";
    }

    private final AmazonS3Client s3Client = new AmazonS3Client(this);
    private final AmazonEC2Client ec2Client = new AmazonEC2Client(this);
    private final JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

    public List<String> getServerTypes() {
        return listAllObjects(Bucket.SERVERS, null, "/");
    }

    public List<String> getServerKeys(String serverType) {
        return listAllObjects(Bucket.SERVERS, serverType, null);
    }

    public Optional<ServerInfo> getServerInfo(String serverKey) {
        return getObject(Bucket.SERVERS, serverKey, ServerInfo.class);
    }

    public void putServerInfo(ServerInfo serverInfo) {
        putObject(Bucket.SERVERS, serverInfo);
    }

    public void deleteServerInfo(ServerInfo serverInfo) {
        deleteObject(Bucket.SERVERS, serverInfo.createKey());
    }

    public List<String> getPlayerPublicKeys() {
        return listAllObjects(Bucket.PUBLIC_PLAYERS, null, null);
    }

    public Optional<PublicPlayerInfo> getPlayerPublicInfo(String publicPlayerKey) {
        return getObject(Bucket.PUBLIC_PLAYERS, publicPlayerKey, PublicPlayerInfo.class);
    }

    public void putPlayerPublicInfo(PublicPlayerInfo publicPlayerInfo) {
        putObject(Bucket.PUBLIC_PLAYERS, publicPlayerInfo);
    }

    public void deletePlayerPublicInfo(PublicPlayerInfo publicPlayerInfo) {
        deleteObject(Bucket.PUBLIC_PLAYERS, publicPlayerInfo.createKey());
    }

    public List<String> getPlayerPrivateKeys() {
        return listAllObjects(Bucket.PRIVATE_PLAYERS, null, null);
    }

    public Optional<PrivatePlayerInfo> getPlayerPrivateInfo(String privatePlayerKey) {
        return getObject(Bucket.PRIVATE_PLAYERS, privatePlayerKey, PrivatePlayerInfo.class);
    }

    public void putPlayerPrivateInfo(PrivatePlayerInfo privatePlayerInfo) {
        putObject(Bucket.PRIVATE_PLAYERS, privatePlayerInfo);
    }

    public void deletePlayerPrivateInfo(PrivatePlayerInfo privatePlayerInfo) {
        deleteObject(Bucket.PUBLIC_PLAYERS, privatePlayerInfo.createKey());
    }

    public List<String> getItemKeys() {
        return listAllObjects(Bucket.ITEMS, null, null);
    }

    public Optional<ItemInfo> getItemInfo(String itemKey) {
        return getObject(Bucket.ITEMS, itemKey, ItemInfo.class);
    }

    public void putItemInfo(ItemInfo itemInfo) {
        putObject(Bucket.ITEMS, itemInfo);
    }

    public void deleteItemInfo(ItemInfo itemInfo) {
        deleteObject(Bucket.ITEMS, itemInfo.createKey());
    }

    public List<String> getPetKeys() {
        return listAllObjects(Bucket.PETS, null, null);
    }

    public Optional<PetInfo> getPetInfo(String petKey) {
        return getObject(Bucket.PETS, petKey, PetInfo.class);
    }

    public void putPetInfo(PetInfo petInfo) {
        putObject(Bucket.PETS, petInfo);
    }

    public void deletePetInfo(PetInfo petInfo) {
        deleteObject(Bucket.PETS, petInfo.createKey());
    }

    public List<String> getCostumeKeys() {
        return listAllObjects(Bucket.COSTUMES, "costumes", null);
    }

    public Optional<CostumeInfo> getCostumeInfo(String costumeKey) {
        return getObject(Bucket.COSTUMES, costumeKey, CostumeInfo.class);
    }

    public void putCostumeInfo(CostumeInfo costumeInfo) {
        putObject(Bucket.COSTUMES, costumeInfo);
    }

    public void deleteCostumeInfo(CostumeInfo costumeInfo) {
        deleteObject(Bucket.COSTUMES, costumeInfo.createKey());
    }

    /**
     * Returns the type of the server.
     * Every server on the CloudCraftNetwork must give a type to better classify itself.
     * Because of the potential variety of server types, the version or creator should be prefixed to the game type.
     * For example, if your company is called "mycompany" and you want to register your own version of spleef,
     * your type might be: "mycompany.spleef"
     *
     * @return The type of the server.
     */
    public abstract String getType();

    /**
     * Deletes an object from S3
     *
     * @param bucketName The name of the bucket.
     * @param key        The key of the object to be deleted
     * @return true if the object was deleted, false otherwise.
     */
    private boolean deleteObject(String bucketName, String key) {
        try {
            s3Client.deleteObject(bucketName, key);
            return true;
        } catch (AmazonClientException e) {
            getLogger().warning(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Uploads an object to S3
     *
     * @param bucketName The name of the bucket.
     * @param object     The CloudCraftNetworkInfo object to upload.
     * @return true if the object was uploaded successfully, false otherwise.
     */
    private <T extends CloudCraftNetworkInfo> boolean putObject(String bucketName, T object) {

        // Create a JSON String from the object
        StringWriter writer = new StringWriter();
        try {
            jsonFactory.createGenerator(writer).writeObject(object);
        } catch (IOException e) {
            getLogger().warning("Unable to parse object into a JSON String");
            e.printStackTrace();
            return false;
        }

        // upload the JSON String to S3
        try (StringInputStream inputStream = new StringInputStream(writer.toString())) {
            s3Client.putObject(bucketName, object.createKey(), inputStream, null);
            return true;
        } catch (Exception e) {
            getLogger().warning(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves an object from S3
     *
     * @param bucketName The name of the bucket.
     * @param key        The key of the object.
     * @param infoClass  The class of the info object to return.
     * @return the S3 object mapped to a CloudCraftNetworkInfo object.
     */
    private <T extends CloudCraftNetworkInfo> Optional<T> getObject(String bucketName, String key, Class<T> infoClass) {

        if (bucketName == null) {
            return Optional.empty();
        }

        // Retrieve the raw object from S3
        S3Object s3Object = null;
        try {
            s3Object = s3Client.getObject(bucketName, key);
        } catch (AmazonClientException e) {
            getLogger().warning(e.toString());
            e.printStackTrace();
        }
        if (s3Object == null) {
            return Optional.empty();
        }

        // Read the raw object into a String
        String rawObject;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
            rawObject = reader.readLine();
        } catch (IOException e) {
            getLogger().warning("Unable to read S3 Object into a string.");
            e.printStackTrace();
            return Optional.empty();
        }

        // Parse the string JSON into a CloudCraftNetworkInfo object
        try {
            return Optional.of(jsonFactory.createParser(rawObject).readValuesAs(infoClass).next());
        } catch (Exception e) {
            getLogger().warning("S3 Object \"" + bucketName + ":" + key + "\" is not a valid " + infoClass.getName() + ".");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Queries S3 for keys.
     * Long responses might be truncated. This method ensures that all results are returned in a single list.
     *
     * @param bucketName The bucket name.
     * @param prefix     The key prefix. Only keys which match the prefix are returned.
     * @param delimiter  The delimiter. All suffixes after the delimiter are dropped from the keys.
     * @return A list of keys which match the query. This list is never null.
     */
    private List<String> listAllObjects(String bucketName, String prefix, String delimiter) {

        ArrayList<String> results = new ArrayList<>();

        if (bucketName == null) {
            return results;
        }

        try {
            ListObjectsRequest request = new ListObjectsRequest();
            request.setBucketName(bucketName);
            request.setPrefix(prefix);
            request.setDelimiter(delimiter);
            for (
                    ObjectListing listing = s3Client.listObjects(request);
                    listing.getObjectSummaries().size() > 0;
                    listing = s3Client.listNextBatchOfObjects(listing)
                    ) {
                results.addAll(listing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList()));
            }
        } catch (AmazonClientException e) {
            getLogger().warning(e.toString());
            e.printStackTrace();
        }

        return results;
    }
}
