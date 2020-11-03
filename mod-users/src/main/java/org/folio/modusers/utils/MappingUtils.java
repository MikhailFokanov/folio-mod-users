package org.folio.modusers.utils;

import java.util.UUID;

public class MappingUtils {

  public static UUID parseUUID(String str) {
    return str == null ? null : java.util.UUID.fromString(str);
  }

  public static String toStr(Object str) {
    return str == null ? null : String.valueOf(str);
  }

}
