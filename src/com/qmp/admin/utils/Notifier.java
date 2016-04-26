package com.qmp.admin.utils;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Notifier {
	private static void showNotification(String title, String message, NotificationType nType) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(nType);
        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.seconds(3));
	}
	
	public static void notifySuccess(String title, String message){
		showNotification(title, message, NotificationType.SUCCESS);
	}
	
	public static void notifyInfo(String title, String message){
		showNotification(title, message, NotificationType.INFORMATION);
	}
	
	public static void notifyError(String title, String message){
		showNotification(title, message, NotificationType.ERROR);
	}
	
	public static void notifyWarning(String title, String message){
		showNotification(title, message, NotificationType.WARNING);
	}

}
