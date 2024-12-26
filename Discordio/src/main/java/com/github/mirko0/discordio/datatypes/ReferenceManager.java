package com.github.mirko0.discordio.datatypes;


import com.github.mirko0.discordio.utils.ExpiringCache;
import java.util.UUID;

public class ReferenceManager {

    private final ExpiringCache<String, Object> references = new ExpiringCache<>(720);

    public String addReference(Object o) {
        String referenceId = UUID.randomUUID().toString();
        references.put(referenceId, o);
        return referenceId;
    }

    public boolean referenceExists(String referenceId) {
        return references.containsKey(referenceId);
    }

    public Object getReference(String referenceId) {
        return references.get(referenceId);
    }


}
