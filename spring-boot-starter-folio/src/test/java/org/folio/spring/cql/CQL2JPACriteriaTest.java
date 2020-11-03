package org.folio.spring.cql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfiguration.class })
class CQL2JPACriteriaTest {

  @Test
  void toCriteria() {
    String cql = "query=((username=\"A*\" or personal.firstName=\"A*\" or personal.preferredFirstName=\"A*\" or personal.lastName=\"A*\" or personal.email=\"A*\" or barcode=\"A*\" or id=\"A*\" or externalSystemId=\"A*\")) and active=\"true\" and patronGroup=(\"503a81cd-6c26-400f-b620-14c08943697c\" or \"bdc2b6d4-5ceb-4a12-ab46-249b9a68473e\")";
    //final CQL2JPACriteria cql2JPACriteria = new CQL2JPACriteria();
  }
}
