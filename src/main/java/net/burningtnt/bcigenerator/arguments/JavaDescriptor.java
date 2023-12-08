package net.burningtnt.bcigenerator.arguments;

import java.util.Objects;

public final class JavaDescriptor {
    private final String owner;

    private final String name;

    private final String desc;

    public JavaDescriptor(String owner, String name, String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaDescriptor that = (JavaDescriptor) o;
        return Objects.equals(owner, that.owner) && Objects.equals(name, that.name) && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, desc);
    }

    @Override
    public String toString() {
        return "FieldDescriptor{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
