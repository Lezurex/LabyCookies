package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.alertRules.AlertRule;
import com.voxcrafterlp.statsaddon.objects.alertRules.AlertRuleManager;

import java.util.List;
import java.util.Locale;

public class AlertRulesHandler implements ActionHandler {
    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        AlertRuleManager alertRuleManager = StatsAddon.getInstance().getAlertRuleManager();

        if (pathParts.size() == 2) {
            JsonArray jsonArray = new JsonArray();
            alertRuleManager.getAlertRules().forEach(alertRule -> {
                jsonArray.add(alertRule.serialize());
            });
            return jsonArray.toString();
        } else {
            switch (pathParts.get(2).toLowerCase(Locale.ROOT)) {
                case "new":
                    AlertRule newAlertRule = AlertRule.deserialize(body);
                    alertRuleManager.getAlertRules().add(newAlertRule);
                    alertRuleManager.save();
                    return newAlertRule.serialize().toString();
                case "edit":
                    if (pathParts.size() == 4) {
                        try {
                            int index = Integer.parseInt(pathParts.get(3));
                            if (alertRuleManager.getAlertRules().size() - 1 < index)
                                return "";
                            AlertRule editedAlertRule = AlertRule.deserialize(body);
                            alertRuleManager.getAlertRules().set(index, editedAlertRule);
                            alertRuleManager.save();
                            return editedAlertRule.serialize().toString();
                        } catch (NumberFormatException exception){
                            return "";
                        }
                    } else return "";
                case "delete":
                    if (pathParts.size() == 4) {
                        try {
                            int index = Integer.parseInt(pathParts.get(3));
                            if (alertRuleManager.getAlertRules().size() - 1 < index)
                                return "";
                            alertRuleManager.getAlertRules().remove(index);
                            alertRuleManager.save();
                            return "{\"status\":\"success\"}";
                        } catch (NumberFormatException exception){
                            return "";
                        }
                    } else return "";
            }
        }
        return "";
    }
}
