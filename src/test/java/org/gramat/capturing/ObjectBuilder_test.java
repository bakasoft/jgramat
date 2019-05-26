package org.gramat.capturing;

import org.bakasoft.framboyan.inspect.Inspector;
import org.bakasoft.framboyan.test.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ObjectBuilder_test extends TestCase {

  {
    ObjectBuilder builder = new ObjectBuilder();

    builder.openObject(null); // open container

    value(builder, "title", false, "Object Builder Test", null);

    object(builder, Company.class, "companies", true, () -> {
      value(builder, "id", false, "100", Integer::parseInt);
      value(builder, "name", false, "Company100", null);
      value(builder, "active", false, "true", Boolean::parseBoolean);

      // creating the list explicitly (default list)
      list(builder, null, "users", false, () -> {
        object(builder, User.class, null, true, () -> {
          value(builder, "name", false, "F. Chopin", null);
          value(builder, "email", false, "chopin@example.com", null);
        });
        object(builder, User.class, null, true, () -> {
          value(builder, "name", false, "J. Brahms", null);
          value(builder, "email", false, "brahms@example.com", null);
        });
      });
    });

    object(builder, Company.class, "companies", true, () -> {
      value(builder, "id", false, "200", Integer::parseInt);
      value(builder, "name", false, "Company200", null);
      value(builder, "active", false, "true", Boolean::parseBoolean);

      // creating the list explicitly (typed list)
      list(builder, UserList.class, "users", false, () -> {
        object(builder, User.class, null, true, () -> {
          value(builder, "name", false, "J.S. Bach", null);
          value(builder, "email", false, "bach@example.com", null);
        });
        object(builder, User.class, null, true, () -> {
          value(builder, "name", false, "G.F. Handel", null);
          value(builder, "email", false, "handel@example.com", null);
        });
      });
    });

    object(builder, Company.class, "companies", true, () -> {
      value(builder, "id", false, "300", Integer::parseInt);
      value(builder, "name", false, "Company300", null);
      value(builder, "active", false, "true", Boolean::parseBoolean);

      // creating the list implicitly
      object(builder, User.class, "users", true, () -> {
        value(builder, "name", false, "C. Debusy", null);
        value(builder, "email", false, "debussy@example.com", null);
      });
      object(builder, User.class, "users", true, () -> {
        value(builder, "name", false, "E. Grieg", null);
        value(builder, "email", false, "grieg@example.com", null);
      });
    });

    builder.pushObject(); // close container

    Object result = builder.pop();

    expect(result instanceof Map).toBeTrue();

    Map<?, ?> map = (Map<?, ?>)result;

    expect(map.get("title")).toEqual("Object Builder Test");
    expect(map.get("companies") instanceof List).toBeTrue();

    List<?> companies = (List<?>)map.get("companies");


  }

  private void object(ObjectBuilder builder, Class<?> type, String name, boolean appendMode, Runnable action) {
    builder.openObject(type);

    action.run();

    builder.pushObject();
    builder.popValue(name, appendMode);
  }

  private void list(ObjectBuilder builder, Class<?> type, String name, boolean appendMode, Runnable action) {
    builder.openList(type);

    action.run();

    builder.pushList();
    builder.popValue(name, appendMode);
  }

  private void value(ObjectBuilder builder, String name, boolean appendMode, String value, Function<String, ?> parser) {
    builder.pushValue(value, parser);
    builder.popValue(name, appendMode);
  }

  public static class User {
    private String name;
    private String email;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return name; }
    public void setEmail(String email) { this.email = email; }
  }

  public static class UserList extends ArrayList<User> {}

  public static class Company {
    public int id;
    public String name;
    public boolean active;
    public List<User> users;
  }
}
