package com.restapi.converter;

import com.restapi.model.MyGoals;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MyGoalsDTOConverter {

    public static MyGoals DocToMG(Document doc) {

        String autor = (String) doc.get("autor");
        List<String> goals = new ArrayList<String>();

        if (doc.get("goals") != null && !doc.get("goals").toString().isEmpty()) {
            goals = (List<String>) doc.get("goals");
            MyGoals myGoals = new MyGoals(goals, autor);
            return myGoals;
        } else {
            MyGoals myGoals = new MyGoals(new ArrayList<String>(), autor);
            return myGoals;
        }

    }


}
