/**
 * POJO to represent a company
 */
package mobile.SEC;

public class Company {
    private final String guid;
    private final String name;

    public Company(String guid, String name) {
        this.guid = guid;
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }
}
