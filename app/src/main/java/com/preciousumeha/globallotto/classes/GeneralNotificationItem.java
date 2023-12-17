package com.preciousumeha.globallotto.classes;

import com.preciousumeha.globallotto.entity.Notification;

public class GeneralNotificationItem extends ListItem {
    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    private Notification notification;
    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
