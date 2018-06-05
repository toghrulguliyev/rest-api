package com.restapi.converter;

import com.restapi.model.Event;
import org.bson.Document;

public class EventDTOConverter {

    public static Event documentToEvent(Document doc) {

        String mName = (String) doc.get("name");
        String message = (String) doc.get("message");
        //int mDayOfMonth = (int) doc.get("mDayOfMonth");
        String mStartTime = (String) doc.get("startTime");
        String mEndTime = (String) doc.get("endTime");
        String mColor = (String) doc.get("color");
        String autor = (String) doc.get("autor");
        long id = (long) doc.get("id");
        String eventType = (String) doc.get("eventType");

        System.out.println(doc);

        Event event = new Event(message, mName, mStartTime, mEndTime, mColor, autor);

        event.setId(id);
        event.setEventType(eventType);

        String notifyUser = "";
        if (doc.get("notifyUser") == null || doc.get("notifyUser").toString().isEmpty()) {
            notifyUser = "";
            event.setNotifyUser(notifyUser);
        } else if (doc.get("notifyUser") != null && !doc.get("notifyUser").toString().isEmpty()) {
            notifyUser = (String) doc.get("notifyUser").toString();
            event.setNotifyUser(notifyUser);
        }

        String familyId = "";

        if (doc.get("familyId") == null || doc.get("familyId").toString().isEmpty()) {
            return event;
        } else if (doc.get("familyId").toString() != null && !doc.get("familyId").toString().isEmpty()) {
            familyId = (String) doc.get("familyId");
            event.setFamilyId(familyId);
            return event;
        } else {
            return event;
        }
    }
}
