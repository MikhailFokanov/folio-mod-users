package org.folio.modusers.enums;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public enum UserPropertyInclude {
  GROUPS_INCLUDE("groups", "userGroup"),
  PROXIESFOR_INCLUDE("proxiesfor", "proxyFor"),
  CREDENTIALS_INCLUDE("credentials", null),
  EXPAND_PERMS_INCLUDE("expandPerms", null),
  PERMS_INCLUDE("perms", null),
  SERVICEPOINTS_INCLUDE("servicepoints", null);

  private final String entityName;
  private final String restName;

  UserPropertyInclude(String restName, String entityName) {
    this.restName = restName;
    this.entityName = entityName;
  }

  public static List<UserPropertyInclude> from(List<String> includes) {
    List<UserPropertyInclude> res = new ArrayList<>();
    if (includes == null) {
      return res;
    }
    for (String inc : includes) {
      for (UserPropertyInclude value : values()) {
        if (value.restName.equalsIgnoreCase(inc)) {
          res.add(value);
        }
      }
    }
    return res;
  }
}
