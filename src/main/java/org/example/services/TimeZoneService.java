package org.example.services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TimeZoneService {
    private final Clock clock;

    public TimeZoneService(Clock clock) {
        this.clock = clock;
    }

    public List<OffsetDateTime> countryDateTimes(String countryName) {
        String jsonResponse = this.requestZoneOffsets(countryName);
        return parseDateTime(jsonResponse);
    }

    // TODO: we return OffsetDateTime because we don't have zone information
    //       so that we can create a ZonedDateTime with zone rules (for example DST)
    private List<OffsetDateTime> parseDateTime(String json) {
        // parse `timezones` element
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray zoneOffsetsArray = jsonObject.getAsJsonArray("timezones");

        // create array of strings representation of zone offsets
        Type arrayListType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> zoneOffsets = new Gson().fromJson(zoneOffsetsArray, arrayListType);

        // create DateTimes
        return zoneOffsets.stream()
                .map(utcOffset -> utcOffset.replace("UTC", ""))
                .map(ZoneOffset::of)
                .map(zoneOffset -> Instant.now(this.clock).atOffset(zoneOffset))
                .collect(Collectors.toList());
    }

    abstract String requestZoneOffsets(String countryName);
}
