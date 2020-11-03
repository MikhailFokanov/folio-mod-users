package org.folio.modusers.utils;

import org.apache.commons.lang3.StringUtils;
import org.folio.modusers.dto.UserDto;
import org.json.JSONObject;

public class NotificationUtils {

  private static final String DEFAULT_NOTIFICATION_LANG = "en";

  public static String getNotification(UserDto user, String eventConfig) {
    return new JSONObject()
        .put("eventConfigName", eventConfig)
        .put("recipientId", user.getId())
        .put("context", new JSONObject()
            .put("user", new JSONObject(user)))
        .put("text", StringUtils.EMPTY)
        .put("lang", DEFAULT_NOTIFICATION_LANG).toString();
  }

  public static String getNotificationWithExpirationTime(UserDto user, String eventConfigName,
      String generatedLink, long expirationTimeFromConfig) {
    return new JSONObject()
        .put("eventConfigName", eventConfigName)
        .put("recipientId", user.getId())
        .put("context", new JSONObject()
            .put("user", new JSONObject(user))
            .put("link", generatedLink)
            .put("expirationTime", expirationTimeFromConfig))
        .put("text", StringUtils.EMPTY)
        .put("lang", DEFAULT_NOTIFICATION_LANG).toString();
  }
}
